package com.sparta.restplaceforj.config;

import com.sparta.restplaceforj.jwt.JwtAccessDeniedHandler;
import com.sparta.restplaceforj.jwt.JwtAuthenticationEntryPoint;
import com.sparta.restplaceforj.jwt.JwtAuthenticationFilter;
import com.sparta.restplaceforj.jwt.JwtAuthorizationFilter;
import com.sparta.restplaceforj.jwt.JwtLogoutHandler;
import com.sparta.restplaceforj.provider.JwtProvider;
import com.sparta.restplaceforj.provider.RedisProvider;
import com.sparta.restplaceforj.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtProvider jwtProvider;
  private final RedisProvider redisProvider;
  private final UserDetailsServiceImpl userDetailsService;
  private final AuthenticationConfiguration authenticationConfiguration;
  private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
  private final JwtLogoutHandler jwtLogoutHandler;

  // 인증처리를 위한 authenticationManager 처리 : username~Token 설정
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  //패스워드 암호화
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  //인증 필터
  @Bean
  public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
    JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider, redisProvider);
    filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
    return filter;
  }

  //인가 필터
  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    return new JwtAuthorizationFilter(jwtProvider, redisProvider, userDetailsService);
  }

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter,
      JwtAuthorizationFilter jwtAuthorizationFilter) throws Exception {
    // csrf 방어 설정
    http.csrf(AbstractHttpConfigurer::disable);

    // jwt 사용 설정
    // SessionCreationPolicy을 사용하지 않음
    http.sessionManagement((sessionManagement) ->
        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    );

    http.authorizeHttpRequests((authorizeHttpRequests) ->
        authorizeHttpRequests
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
            .permitAll() // resources 접근 허용 설정
            .requestMatchers("/v1/users").permitAll()
            .requestMatchers("/v1/users/login").permitAll()
            .requestMatchers("/v1/users/reissue").permitAll()
            .requestMatchers("/v1/plans/{plan-id}/invite").permitAll()
            .requestMatchers("/v1/users/kakao/callback").permitAll()
            .requestMatchers("/swagger-ui/**", "/swagger-ui.html",
                "/v3/api-docs/**", "/swagger/", "/swagger-resources/")
            .permitAll()

            .anyRequest().authenticated() // 그 외 모든 요청 인증처리
    );

    // 로그아웃
    // Spring Security 자체의 Logout 기능: 세션 무효화, 쿠키 삭제, SecurityContextHolder 클리어
    http.logout(logout ->
        logout.logoutUrl("/v1/users/logout")
            //세션 무효화, 인증 토큰 삭제
            .addLogoutHandler(jwtLogoutHandler)
            //로그아웃 성공
            .logoutSuccessHandler(jwtLogoutHandler)
    );

    // 필터 순서 설정 : 인가 필터 > 인증 필터 > Username ~ 필터
    http.addFilterBefore(jwtAuthorizationFilter, JwtAuthenticationFilter.class);
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    //예외 검증
    http.exceptionHandling(exceptionHandling ->
        exceptionHandling
            // AuthenticationEntryPoint 등록
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            // AccessDeniedHandler 등록
            .accessDeniedHandler(jwtAccessDeniedHandler)
    );

    return http.build();
  }
}
