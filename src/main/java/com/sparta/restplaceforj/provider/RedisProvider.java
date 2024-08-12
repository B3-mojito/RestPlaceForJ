package com.sparta.restplaceforj.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Slf4j
@Transactional(readOnly = true)
@Component
@RequiredArgsConstructor
public class RedisProvider {
    private final StringRedisTemplate stringRedisTemplate;

    // key를 통해 value 가져오기
    public String getValues(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // key, value, expiration 설정
    public void setValuesWithTimeout(String key, String value, Long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMillis(timeout));
    }

    // key를 통해 value 삭제하기
    public boolean deleteValue(String key) {
        return stringRedisTemplate.delete(key);
    }

    // key를 통해 value가 있는지 확인
    public boolean existData(String key) {
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }
}