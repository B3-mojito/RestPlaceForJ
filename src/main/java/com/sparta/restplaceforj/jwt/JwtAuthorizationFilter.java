package com.sparta.restplaceforj.jwt;

import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.entity.UserRole;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.security.UserDetailsImpl;
import com.sparta.restplaceforj.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
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

@Slf4j(topic = "Jwt 검증 및 인가")
@RequiredArgsConstructor
// OncePerRequestFilter 상속 -> HttpServlet 사용 가능해짐
public class JwtAuthorizationFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;
  private final UserDetailsServiceImpl userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    String accessToken = jwtUtil.getAccessTokenFromHeader(request);

    if (StringUtils.hasText(accessToken)) {
      if (jwtUtil.validateToken(request, accessToken)) {
        // accessToken이 유효할 때
        authenticateWithAccessToken(accessToken);
      } else {
        // accessToken이 유효하지 않을 때 -> refreshToken 검증
        validateAndAuthenticateWithRefreshToken(request, response);
      }
    }

    filterChain.doFilter(request, response);
  }

  // accessToken이 유효
  public void authenticateWithAccessToken(String token) {
    Claims info = jwtUtil.getUserInfoFromToken(token);

    try {
      setAuthentication(info.getSubject());
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new CommonException(ErrorEnum.NOT_FOUND_AUTHENTICATION_INFO);
    }
  }

  // accessToken이 유효하지 않은 경우, 리프레시 토큰 검증 및 엑세스토큰 재발급
  public void validateAndAuthenticateWithRefreshToken(HttpServletRequest request,
      HttpServletResponse response) {
    String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);
    // 리프레시 토큰이 null이 아니고, 유효한 토큰인지 확인
    if (StringUtils.hasText(refreshToken) && jwtUtil.validateToken(request, refreshToken)) {
      // 유저 객체 가져오기
      Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
      UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
          info.getSubject());
      User user = userDetails.getUser();

      // 유저의 리프레시 토큰 검증
      if (user.validateRefreshToken(refreshToken)) {
        // accessToken 생성
        UserRole role = user.getUserRole();
        String newAccessToken = jwtUtil.createAccessToken(info.getSubject(), role);
        jwtUtil.setHeaderAccessToken(response, newAccessToken);

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
  }

  // 인증 처리
  public void setAuthentication(String username) {
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    Authentication authentication = createAuthentication(username);
    context.setAuthentication(authentication);

    SecurityContextHolder.setContext(context);
  }

  // 인증 객체 생성
  private Authentication createAuthentication(String username) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
  }
}