package com.lwj.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * create by lwj on 2019/11/29
 */
@Configuration
public class NewRedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setEnableTransactionSupport(true);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        //value的序列化器使用protostuff序列化器
        ProtostuffSerializer valueSerializer = new ProtostuffSerializer();
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(valueSerializer);
        return template;
    }
}
