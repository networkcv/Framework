package com.lwj.seckill.cache;

import com.lwj.seckill.pojo.Seckill;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * create by lwj on 2019/11/28
 */

@Slf4j
@Component
public class RedisDao {
    @Autowired
    private RedisTemplate<String, Object> template;

    public Seckill getSeckill(long seckillId) {
        Seckill seckill = null;
        try {
            seckill = (Seckill) template.opsForValue().get(String.valueOf(seckillId));
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return seckill;
    }

    public int putSeckill(Seckill seckill) {
        try {
            template.opsForValue().set(String.valueOf(seckill.getSeckillId()), seckill);
            return 1;
        } catch (Exception e) {
            log.error(e.getMessage());
            return 0;
        }

    }
}

