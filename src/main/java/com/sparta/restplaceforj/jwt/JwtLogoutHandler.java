package com.sparta.restplaceforj.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.provider.JwtProvider;
import com.sparta.restplaceforj.provider.RedisProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j(topic = "로그아웃")
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler, LogoutSuccessHandler {

  private final RedisProvider redisProvider;
  private final JwtProvider jwtProvider;

  @Override
  public void logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

    log.info("로그아웃 시도");

    // accessToken 가져오기
    String accessToken = jwtProvider.getTokenFromHeader(request, JwtProvider.AUTH_ACCESS_HEADER);

    // 유저를 찾고, 유저의 refreshToken을 삭제
    Claims info = jwtProvider.getUserInfoFromToken(accessToken);
    String email = info.getSubject();
    redisProvider.deleteValue(email);

    // SecurityContextHolder 초기화
    SecurityContextHolder.clearContext();
  }

  @Override
  public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    log.info("로그아웃 성공");
    response.setContentType("application/json; charset=UTF-8");
    response.setStatus(ResponseEnum.LOGOUT_SUCCESS.getHttpStatus().value());
    response.getWriter()
        .write(new ObjectMapper().writeValueAsString(ResponseEnum.LOGOUT_SUCCESS.getMessage()));
  }
}

