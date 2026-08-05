package com.HotelBookingSystem.HBS.Services;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisLockService {
    @Qualifier("redisTemplate")

    private final RedisTemplate<String, Object> redisTemplate;

    private static final Duration LOCK_TIMEOUT =
            Duration.ofSeconds(30);

    public boolean acquireLock(String lockKey) {

        Boolean success =
                redisTemplate.opsForValue()
                        .setIfAbsent(
                                lockKey,
                                "LOCKED",
                                LOCK_TIMEOUT
                        );

        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String lockKey) {

        redisTemplate.delete(lockKey);

    }

}