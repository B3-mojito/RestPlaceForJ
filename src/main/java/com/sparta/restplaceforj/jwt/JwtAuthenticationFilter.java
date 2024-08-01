package com.sparta.restplaceforj.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.restplaceforj.dto.LoginRequestDto;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.entity.UserRole;
import com.sparta.restplaceforj.repository.UserRepository;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.util.RedisUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, RedisUtil redisUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.redisUtil = redisUtil;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/v1/users/login");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");
        try {
            //json 형태의 String 데이터를 LoginRequestDto로 변환
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
                    LoginRequestDto.class);

      List<GrantedAuthority> authorities = Collections.singletonList(
          new SimpleGrantedAuthority(UserRole.USER.toString()));

      return getAuthenticationManager().authenticate(
          new UsernamePasswordAuthenticationToken(
              requestDto.getEmail(),
              requestDto.getPassword(),
              authorities
          )
      );
    } catch (IOException e) {
      log.error("로그인 시도(attemptAuthentication) 예외 발생 {}", e.getMessage());
      throw new RuntimeException(e.getMessage());
    }
  }

    // 로그인 성공시 처리
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 및 jwt 생성");

        //principal -> userDetail -> userDetailsImpl
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();

        //토큰에 넣을 accountId, role 추출
        String email = user.getEmail();
        UserRole role = user.getUserRole();

        //accessToken, refreshToken 생성
        String accessToken = jwtUtil.createAccessToken(email, role);
        String refreshToken = jwtUtil.createRefreshToken(email);

        //refreshToken 저장
        redisUtil.setValues(jwtUtil.AUTH_REFRESH_HEADER+email, refreshToken);

        //헤더에 토큰 담기
        response.addHeader(JwtUtil.AUTH_ACCESS_HEADER, accessToken);

        //응답
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write("로그인 성공");
    }


    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");
        response.setStatus(401);
        response.getWriter().write("로그인 실패");
    }
}
