package com.lwj.seckill.cache;

import com.lwj.seckill.SeckillApplication;
import com.lwj.seckill.pojo.Seckill;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * create by lwj on 2019/11/28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillApplication.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisDaoTest {
    @Autowired()
    private RedisDao redisDao;

    @Test
    void getSeckill() {
        Seckill seckill = redisDao.getSeckill(1000);
        System.out.println(seckill);

    }

    @Test
    void putSeckill() {
        Seckill seckill = new Seckill();
        seckill.setSeckillId(1000);
        seckill.setName("test");
        redisDao.putSeckill(seckill);
    }
}