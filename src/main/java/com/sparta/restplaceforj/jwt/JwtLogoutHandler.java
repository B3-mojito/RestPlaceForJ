package com.sparta.restplaceforj.jwt;

import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.repository.UserRepository;
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

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void logout(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("로그아웃 시도");

    // accessToken, refreshToken 가져오기
    String accessTokenValue = jwtUtil.getAccessTokenFromHeader(request);
    String refreshTokenValue = jwtUtil.getRefreshTokenFromHeader(request);

    // 유저를 찾고, 유저의 refreshToken을 null로 set
    String email = jwtUtil.getUserInfoFromToken(refreshTokenValue).getSubject();
    User findUser = userRepository.findByEmailOrThrow(email);
    findUser.setRefreshToken(null);
    userRepository.save(findUser);

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

