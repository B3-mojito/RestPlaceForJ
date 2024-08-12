package com.sparta.restplaceforj.config;

import com.sparta.restplaceforj.util.JwtUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOrigins("http://localhost:3000",
            "http://15.164.59.0:8080")  // Replace with your frontend URL
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
        .allowedHeaders(JwtUtil.AUTH_ACCESS_HEADER, "Content-Type")
        .exposedHeaders(JwtUtil.AUTH_ACCESS_HEADER)  // Expose the Authorization header
        .allowCredentials(true);
  }
}