package com.lwj.seckill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * create by lwj on 2019/11/28
 */
@Configuration
public class RedisConfig {
    //1.创建JedisPollConfig对象，在该对象中完成连接池的配置
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        System.out.println("wait："+ config.getMaxIdle());
        //最大空闲数
//        config.setMaxIdle(10);
        //最小空闲数
        config.setMinIdle(5);
        //最大连接数
        config.setMaxTotal(20);
        return config;
    }

    //2.创建JedisConnectionFactory 配置redis连接信息
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig config) {
        System.out.println("ok："+ config.getMaxIdle());
        JedisConnectionFactory factory = new JedisConnectionFactory();
        //配置连接池信息
        factory.setPoolConfig(config);
        factory.setHostName("192.168.3.193");
        factory.setPort(6379);
        return factory;
    }

    //3.创建RedisTemplate，用于执行redis操作的方法，value的序列化器使用protostuff序列化器
    @Bean
    public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory factory) {
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
