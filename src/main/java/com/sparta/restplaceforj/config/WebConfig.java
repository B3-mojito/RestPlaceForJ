package com.sparta.restplaceforj.config;

import com.sparta.restplaceforj.provider.JwtProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins(
            "http://mojito-as-lb-1-346761212.ap-northeast-2.elb.amazonaws.com")  // Replace with your frontend URL
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
        .allowedHeaders(JwtUtil.AUTH_ACCESS_HEADER, "Content-Type")
        .exposedHeaders(JwtUtil.AUTH_ACCESS_HEADER)  // Expose the Authorization header
        .allowCredentials(true);
  }
}