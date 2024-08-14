package com.sparta.restplaceforj.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j(topic = "Log Aop")
public class LogAop {

  @Before("execution(* com.sparta.restplaceforj.controller..*(..))")
  public void logBefore(JoinPoint joinPoint) {
    // 현재 요청에 대한 ServletRequestAttributes를 가져옴
    ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    HttpServletRequest request = attributes.getRequest();

    String userIpAddress = request.getRemoteAddr();

    // Request URL과 HTTP Method를 가져와서 로그로 출력
    log.info("Request URL: [ {} ] {} - User IP: {}",
        request.getMethod(), request.getRequestURL().toString(), userIpAddress);
  }
}