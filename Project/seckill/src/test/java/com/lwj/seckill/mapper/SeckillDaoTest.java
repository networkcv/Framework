package com.lwj.seckill.mapper;

import com.lwj.seckill.SeckillApplication;
import com.lwj.seckill.pojo.Seckill;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * create by lwj on 2019/11/26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SeckillDaoTest {
    @Autowired
    private SeckillDao seckillDao;

    @Test
    void test(){
        Seckill test = seckillDao.test();
        System.out.println(test);
    }

    @Test
    void reduceNumber() {
    }

    @Test
    void queryById() {
    }

    @Test
    void queryAll() {
        List<Seckill> seckills = seckillDao.queryAll(0, 5);
        System.out.println(seckills);
    }
}