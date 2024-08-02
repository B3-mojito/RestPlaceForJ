package com.sparta.restplaceforj.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.exception.ErrorEnum;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // 인가 실패
    // 인증되지 않은 사용자가 인증이 필요한 페이지에 접근 했을 경우 실행
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {


        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(ResponseEnum.FORBIDDEN_ACCESS.getHttpStatus().value());
        response.getWriter().write(new ObjectMapper().writeValueAsString(ResponseEnum.FORBIDDEN_ACCESS.getMessage()));
    }
}
