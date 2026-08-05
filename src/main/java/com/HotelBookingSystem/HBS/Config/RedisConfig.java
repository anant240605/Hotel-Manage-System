package com.HotelBookingSystem.HBS.Config;
import com.HotelBookingSystem.HBS.DTO.ConversationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {

        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer();

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(60))
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(serializer)
                )
                .disableCachingNullValues();
    }

    @Bean
    public RedisTemplate<String, ConversationContext> conversationRedisTemplate(
            RedisConnectionFactory connectionFactory) {

        RedisTemplate<String, ConversationContext> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        mapper.disable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        );

        Jackson2JsonRedisSerializer<ConversationContext> serializer =
                new Jackson2JsonRedisSerializer<>(
                        mapper,
                        ConversationContext.class
                );

        template.setKeySerializer(
                new StringRedisSerializer());

        template.setValueSerializer(serializer);
        template.setHashKeySerializer(
                new StringRedisSerializer());

        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();

        return template;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            RedisConnectionFactory connectionFactory){

        RedisTemplate<String,Object> template =
                new RedisTemplate<>();

        template.setConnectionFactory(connectionFactory);

        template.setKeySerializer(
                new StringRedisSerializer());

        template.setValueSerializer(
                new GenericJackson2JsonRedisSerializer());

        template.afterPropertiesSet();

        return template;
    }

}