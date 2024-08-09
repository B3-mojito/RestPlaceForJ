package com.sparta.restplaceforj.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.restplaceforj.dto.KakaoUserInfoDto;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.repository.UserRepository;
import com.sparta.restplaceforj.util.JwtUtil;
import com.sparta.restplaceforj.util.RedisUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;


@Slf4j(topic = "Kakao Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;
    private final RedisUtil redisUtil;

    @Value("${kakao.secret.key}")
    public String kakaoKey;


    public void kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "유저 인포에 접근할 수 있도록 하는 액세스 토큰" 요청
        // https://kauth.kakao.com/oauth/token
        String accessTokenToUserInfo = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        // https://kapi.kakao.com/v2/user/me
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessTokenToUserInfo);

        // 3. 필요 시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. Jwt access 토큰 반환 및 헤더에 토큰 담기
        String createdToken = jwtUtil.createAccessToken(kakaoUser.getEmail(), kakaoUser.getUserRole());
        response.addHeader(JwtUtil.AUTH_ACCESS_HEADER, createdToken);

        // 5. refreshToken 설정
        registerRefreshToken(kakaoUserInfo);

    }

    // 인가 코드 가져오기
    public String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoKey);
        body.add("redirect_uri", "http://localhost:8080/v1/users/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(requestEntity, String.class);

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        return jsonNode.get("access_token").asText();
    }

    public KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com")
                .path("/v2/user/me")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);
        return new KakaoUserInfoDto(id, nickname, email);
    }

    @Transactional
    public User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();

        // Kakao Id로 등록된 유저 객체 가져오기
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        // 가입 이력이 없을 경우 회원가입
        if (kakaoUser == null) {

            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();

            // 자체 회원가입 이메일을 카카오 이메일로 했었을 경우 앞으로 카카오로 로그인할 수 있도록 변경
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);

            // 카카오 이메일로 가입했던 경우
            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.updateKakao(kakaoId);
            } else {
                // 카카오 이메일로 자체 회원 가입했던 이력도 없고, 아예 카카오로 신규 회원가입 하는 경우
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(kakaoUserInfo.getNickname(),
                        kakaoUserInfo.getNickname(),
                        email,
                        encodedPassword,
                        kakaoUserInfo.getId());
            }

            userRepository.save(kakaoUser);
        }
        return kakaoUser;
    }

    // 리프레시 토큰 레디스에 저장
    private void registerRefreshToken(KakaoUserInfoDto kakaoUserInfo) {
        String email = kakaoUserInfo.getEmail();
        String refreshToken = jwtUtil.createRefreshToken(email);

        redisUtil.setValuesWithTimeout(email, refreshToken, jwtUtil.REFRESH_TOKEN_EXPIRE_TIME);
    }


}
