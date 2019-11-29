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
@SpringBootTest(classes = SeckillApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisDaoTest {
    @Autowired()
    private RedisDao<Seckill> redisDao;

    @Test
    void getSeckill() {
        Seckill res = redisDao.getValue("1002");
        System.out.println(res);

    }

    @Test
    void putSeckill() {
        for (int i = 0; i <1000 ; i++) {
            Seckill seckill = new Seckill();
            seckill.setSeckillId(i);
            seckill.setName("test");
            redisDao.set(String.valueOf(seckill.getSeckillId()), seckill);
        }

    }
}