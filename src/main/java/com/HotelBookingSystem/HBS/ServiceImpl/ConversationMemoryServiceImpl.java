package com.HotelBookingSystem.HBS.ServiceImpl;
import com.HotelBookingSystem.HBS.DTO.ConversationContext;
import com.HotelBookingSystem.HBS.Services.ConversationMemoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
@Service
@RequiredArgsConstructor
public class ConversationMemoryServiceImpl implements ConversationMemoryService {
    @Qualifier("conversationRedisTemplate")
    private final RedisTemplate<String,ConversationContext> redisTemplate;

    private String getKey(
            String sessionId){

        return "conversation:" + sessionId;

    }

    @Override
    public void save(
            String sessionId,
            ConversationContext context){

        redisTemplate.opsForValue()
                .set(

                        getKey(sessionId),

                        context,

                        Duration.ofMinutes(30)

                );

    }

    @Override
    public ConversationContext get(String sessionId){
        return redisTemplate.opsForValue()
                .get(getKey(sessionId));

    }

    @Override
    public void delete(
            String sessionId){

        redisTemplate.delete(
                getKey(sessionId));

    }
}
