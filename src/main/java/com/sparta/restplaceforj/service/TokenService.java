package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.jwt.JwtUtil;
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
    private final JwtUtil jwtUtil;

    public User validateAndGetUserFromRefreshToken(HttpServletRequest request) {
        String refreshToken = jwtUtil.getRefreshTokenFromHeader(request);
        if (refreshToken == null || !jwtUtil.validateToken(request, refreshToken)) {
            throw new CommonException(ErrorEnum.INVALID_JWT);
        }

        // 토큰에서 사용자 정보 추출
        Claims claims = jwtUtil.getUserInfoFromToken(refreshToken);
        String email = claims.getSubject();

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
