package com.lwj.seckill.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class RedisDaoImpl<T> implements RedisDao<T> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean set(String key, Object value) {
        try {
            ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
            opsForValue.set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            // 创建对简单值(Redis术语中的string类型)执行操作的对象
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> T getValue(String key, Class<T> type) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        try {
            result = operations.get(key);
            if (result == null) {
                return null;
            }
            // 将 Object 类型强转成 type 对应的类型
            return type.cast(result);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void removeKey(String key) {
        // 检查 key 是否存在
        if (existsKey(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            removeKey(key);
        }
    }

    @Override
    public void removePattern(String pattern) {
        // 获取所有匹配的键
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }

    }

    @Override
    public <T> T getValue(String key) {
        T result = null;
        try {
            ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
            result = (T) opsForValue.get(key);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
