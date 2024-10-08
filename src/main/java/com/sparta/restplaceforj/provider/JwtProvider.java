package com.sparta.restplaceforj.provider;

import com.sparta.restplaceforj.entity.UserRole;
import com.sparta.restplaceforj.exception.ErrorEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j(topic = "JwtUtil")
public class JwtProvider {

  // accessToken 토큰 헤더
  public static final String AUTH_ACCESS_HEADER = "Authorization";
  public static final String AUTH_REFRESH_HEADER = "RefreshToken";
  // 사용자 권한 키
  public static final String AUTHORIZATION_KEY = "auth";
  // Token 식별자
  public static final String BEARER_PREFIX = "Bearer ";
  // accessToken 만료 시간 (60분)
  private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;
  // refreshToken 만료 시간 (2주)
  public static final long REFRESH_TOKEN_EXPIRE_TIME = 14 * 24 * 60 * 60 * 1000L;

  @Value("${jwt.secret.key}")
  private String secretKey;
  private Key key;
  private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

  @PostConstruct
  public void init() {
    byte[] bytes = Base64.getDecoder().decode(secretKey);
    key = Keys.hmacShaKeyFor(bytes);
  }

  // accessToken 생성
  public String createAccessToken(String email, UserRole role) {
    Date date = new Date();

    return BEARER_PREFIX + Jwts.builder()
        .setSubject(email) //사용자 식별값
        .claim(AUTHORIZATION_KEY, role) //사용자 권한
        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_EXPIRE_TIME))
        .setIssuedAt(date) //발급일
        .signWith(key, signatureAlgorithm)
        .compact();
  }

  // refreshToken 생성
  public String createRefreshToken(String email) {
    Date date = new Date();

    return BEARER_PREFIX + Jwts.builder()
        .setSubject(email)
        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_EXPIRE_TIME))
        .setIssuedAt(date)
        .signWith(key, signatureAlgorithm)
        .compact();
  }

  public String getTokenFromHeader(HttpServletRequest request, String HEADER) {
    String token = request.getHeader(HEADER);
    if (!(StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX))) {
      return null;
    }
    return token.substring(BEARER_PREFIX.length());

  }

  public boolean validateToken(HttpServletRequest request, String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return true;
    } catch (SecurityException | MalformedJwtException | SignatureException e) {
      log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
      request.setAttribute("jwtException", ErrorEnum.INVALID_JWT);
    } catch (UnsupportedJwtException e) {
      log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
      request.setAttribute("jwtException", ErrorEnum.EXPIRED_ACCESS_TOKEN);
    } catch (IllegalArgumentException e) {
      log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
      request.setAttribute("jwtException", ErrorEnum.INVALID_JWT);
    }
    return false;
  }

  // 토큰에서 사용자 정보 가져오기
  public Claims getUserInfoFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();
  }

  // 헤더에 AccessToken 담기
  public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
    response.setHeader(AUTH_ACCESS_HEADER, accessToken);
  }
}
