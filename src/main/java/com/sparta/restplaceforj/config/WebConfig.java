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
        .allowedOrigins("http://localhost:3000")  // Replace with your frontend URL
        .allowedOrigins("http://15.164.59.0:8080")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders(JwtUtil.AUTH_ACCESS_HEADER, "Content-Type")
        .exposedHeaders(JwtUtil.AUTH_ACCESS_HEADER)  // Expose the Authorization header
        .allowCredentials(true);
  }
}