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
        .allowedOrigins("https://www.restplaceforj.com",
            "http://mojito-as-lb-1-346761212.ap-northeast-2.elb.amazonaws.com")
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
        .allowedHeaders(JwtProvider.AUTH_ACCESS_HEADER, "Content-Type")
        .exposedHeaders(JwtProvider.AUTH_ACCESS_HEADER)
        .allowCredentials(true);
  }
}