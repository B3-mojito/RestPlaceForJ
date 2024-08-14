package com.sparta.restplaceforj.jwt;

import static com.sparta.restplaceforj.provider.JwtProvider.BEARER_PREFIX;

import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.entity.UserRole;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.provider.JwtProvider;
import com.sparta.restplaceforj.provider.RedisProvider;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JwtAuthorizationFilter")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final RedisProvider redisProvider;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String accessToken = jwtProvider.getAccessTokenFromHeader(request);
    String refreshToken = jwtProvider.getRefreshTokenFromHeader(request);

    try {
      if (StringUtils.hasText(accessToken)) {
        if (jwtProvider.validateToken(request, accessToken)) {
          log.info("유효한 accessToken");
          authenticateWithAccessToken(accessToken);
        } else {
          log.info("유효하지 않은 accessToken");
          Claims claims = jwtProvider.getUserInfoFromToken(accessToken);
          String email = claims.getSubject();
          validateAndAuthenticateWithRefreshToken(request, response, email);
        }
      }
    } catch (ExpiredJwtException e) {
    }

    filterChain.doFilter(request, response);
  }

  // accessToken이 유효
  public void authenticateWithAccessToken(String token) {
    Claims info = jwtProvider.getUserInfoFromToken(token);

    try {
      setAuthentication(info.getSubject());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CommonException(ErrorEnum.NOT_FOUND_AUTHENTICATION_INFO);
    }
  }

  // accessToken이 유효하지 않은 경우, 리프레시 토큰 검증 및 엑세스토큰 재발급
  public void validateAndAuthenticateWithRefreshToken(HttpServletRequest request,
      HttpServletResponse response,
      String email) {
    log.info("refreshToken 검증 시도");
    String refreshToken = redisProvider.getValues(email)
        .substring(BEARER_PREFIX.length());

    // 리프레시 토큰이 null이 아니고, 유효한 토큰인지 확인
    if (StringUtils.hasText(refreshToken) && jwtProvider.validateToken(request, refreshToken)) {

      log.info("유효한 refreshToken");
      // 유저 객체 가져오기
      Claims info = jwtProvider.getUserInfoFromToken(refreshToken);
      UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
          info.getSubject());
      User user = userDetails.getUser();

      // accessToken 생성
      UserRole role = user.getUserRole();
      String newAccessToken = jwtProvider.createAccessToken(info.getSubject(), role);
      jwtProvider.setHeaderAccessToken(response, newAccessToken);

      try {
        //Athentication 설정
        setAuthentication(info.getSubject());
      } catch (Exception e) {
        log.error(e.getMessage());
        throw new CommonException(ErrorEnum.NOT_FOUND_AUTHENTICATION_INFO);
      }
    } else {
      throw new CommonException(ErrorEnum.INVALID_JWT);
    }
  }

  // 인증 처리
  public void setAuthentication(String email) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = createAuthentication(email);
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);
    log.info("인증처리 완료");
  }

  // 인증 객체 생성
  private Authentication createAuthentication(String email) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}