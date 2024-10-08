package com.sparta.restplaceforj.provider;

import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;


@Slf4j(topic = "AuthCodeUtil")
@Component
@RequiredArgsConstructor
public class AuthCodeProvider {

    public static final Long EXPIRATION_TIME = 30 * 60 * 1000L; // 30분 유효기간
    private final RedisProvider redisProvider;

    public String createAuthCode() {
        SecureRandom random = new SecureRandom();
        int code = random.nextInt(900000) + 100000; // 100000 ~ 999999 사이의 숫자
        return String.valueOf(code);
    }

    public String checkAuthCodeInvalidation(String authCode) {
        if (!redisProvider.existData(authCode)) {
            throw new CommonException(ErrorEnum.INVALID_AUTH_CODE);
        }

        // 인증 코드로 유저 이메일 가져오기
        String email = redisProvider.getValues(authCode);

        // redis에서 인증 코드 삭제
        redisProvider.deleteValue(authCode);

        return email;
    }


}