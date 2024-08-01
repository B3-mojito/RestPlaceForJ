package com.sparta.restplaceforj.jwt;


import com.sparta.restplaceforj.util.JwtUtil;
import com.sparta.restplaceforj.util.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler, LogoutSuccessHandler {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    @Override
    public void logout(
        HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        log.info("로그아웃 시도");

        // accessToken 가져오기
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);

        // 유저를 찾고, 유저의 refreshToken을 null로 set
        Claims info = jwtUtil.getUserInfoFromToken(accessToken);
        String email =info.getSubject();
        redisUtil.deleteValue(email);

        // SecurityContextHolder 초기화
        SecurityContextHolder.clearContext();
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
        log.info("로그아웃 성공");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write("로그아웃 성공");
  }
}

