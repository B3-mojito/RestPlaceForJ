package com.sparta.restplaceforj.util;

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
public class RedisUtil {
    private final StringRedisTemplate stringRedisTemplate;

    // key를 통해 value 가져오기
    public String getValues(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void setValues(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    // key, value, expiration 설정
    @Transactional
    public void setValuesWithTimeout(String key, String value, Long timeout) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofMillis(timeout));
    }

    // key를 통해 value 삭제하기
    @Transactional
    public boolean deleteValue(String key) {
        return stringRedisTemplate.delete(key);
    }

}