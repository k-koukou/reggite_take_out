package com.itkoukou.reggie.confing;


import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Controller;
/**
 * @className RedisConfig
 * @userName 李院斌
 * @DATA 2023-02-21 18:54
 * @nickName 辻弎
 **/
@Controller
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public RedisTemplate<Object , Object> redisTemplate(RedisConnectionFactory factory){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(factory);
        //设置键的序列化方式
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //设置值的序列化方式
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
        return  redisTemplate;
    }
}
