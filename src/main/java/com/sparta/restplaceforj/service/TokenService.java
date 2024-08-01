package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.jwt.JwtUtil;
import com.sparta.restplaceforj.repository.UserRepository;
import com.sparta.restplaceforj.util.RedisUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.sparta.restplaceforj.jwt.JwtUtil.BEARER_PREFIX;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    public User validateAndGetUserFromRefreshToken(HttpServletRequest request) {
        // accessToken 가져오기
        String accessToken = jwtUtil.getAccessTokenFromHeader(request);
        if (accessToken == null) {
            throw new CommonException(ErrorEnum.INVALID_JWT);
        }

        // accessToken에서 유저 정보 가져오기
        Claims claims = jwtUtil.getUserInfoFromToken(accessToken);
        String email = claims.getSubject();

        // refreshToken 가져오기
        String refreshToken = redisUtil.getValues(email)
                .substring(BEARER_PREFIX.length());
        if (refreshToken == null || !jwtUtil.validateToken(request, refreshToken)) {
            throw new CommonException(ErrorEnum.INVALID_JWT);
        }

        // 데이터베이스에서 사용자 조회
        return userRepository.findByEmailOrThrow(email);
    }

    public void updateToken(HttpServletResponse response, User user) {
        // 새로운 access token 발급
        String newAccessToken = jwtUtil.createAccessToken(user.getEmail(), user.getUserRole());

        // 새로운 access token을 response 헤더에 설정
        jwtUtil.setHeaderAccessToken(response, newAccessToken);
    }
}
