package com.sparta.restplaceforj.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.exception.ErrorEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

  // 인증이 실패했을 떄 실행된다.
  // ROLE_ADMIN 권한이 있는 사용자가 필요한 요청에 ROLE_USER 권한을 가진 사용자가 접근했을 때 실행된다.
  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {

      response.setContentType("application/json; charset=UTF-8");
      response.setStatus(ResponseEnum.INVALID_ACCESS.getHttpStatus().value());
      response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseEnum.INVALID_ACCESS.getMessage()));
  }
}
