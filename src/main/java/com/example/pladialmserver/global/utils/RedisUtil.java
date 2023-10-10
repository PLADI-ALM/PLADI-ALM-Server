package com.example.pladialmserver.global.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, String> redisTemplate;
    public void setValue(String key, String value, Duration time) {
        redisTemplate.opsForValue().set(key, value, time);
    }

    public void setValue(String key, String value, Long exp, TimeUnit time){
        redisTemplate.opsForValue().set(key, value, exp, time);
    }

    public String getValue(String key){
        return redisTemplate.opsForValue().get(key);
    }

    @Transactional
    public void deleteValue(String key){
        redisTemplate.delete(key);
    }
}
