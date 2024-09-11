package com.sparta.restplaceforj.service;

import static com.sparta.restplaceforj.provider.JwtProvider.BEARER_PREFIX;

import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.provider.JwtProvider;
import com.sparta.restplaceforj.provider.RedisProvider;
import com.sparta.restplaceforj.repository.UserRepository;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TokenService {

  private final UserRepository userRepository;
  private final JwtProvider jwtProvider;
  private final RedisProvider redisProvider;

  /**
   * refreshToken 검증, 유저 정보 가져오기 메서드
   *
   * @param request
   * @return User
   */
  public User validateAndGetUserFromRefreshToken(HttpServletRequest request) {
    // refreshToken 가져오기
    String refreshToken = jwtProvider.getTokenFromHeader(request, JwtProvider.AUTH_REFRESH_HEADER);
    if (refreshToken == null) {
      throw new CommonException(ErrorEnum.INVALID_JWT);
    }

    // refreshToken 유저 정보 가져오기
    Claims claims = jwtProvider.getUserInfoFromToken(refreshToken);
    String email = claims.getSubject();

    // redis 에서 refreshToken 가져오기
    String redisRefreshToken = redisProvider.getValues(email)
        .substring(BEARER_PREFIX.length());
    if (redisRefreshToken == null || !jwtProvider.validateToken(request, redisRefreshToken)) {
      throw new CommonException(ErrorEnum.INVALID_JWT);
    }

    // 데이터베이스에서 사용자 조회
    return userRepository.findByEmailOrThrow(email);
  }

  /**
   * accessToken 재발급 및 설정
   *
   * @param response
   * @param user
   * @return void
   */
  public void updateToken(HttpServletResponse response, User user) {
    // 새로운 access token 발급
    String newAccessToken = jwtProvider.createAccessToken(user.getEmail(), user.getUserRole());

    // 새로운 access token을 response 헤더에 설정
    jwtProvider.setHeaderAccessToken(response, newAccessToken);
  }
}
