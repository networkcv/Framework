package com.lwj.seckill.service.Impl;

import com.lwj.seckill.SeckillApplication;
import com.lwj.seckill.dto.Exposer;
import com.lwj.seckill.dto.SeckillExecution;
import com.lwj.seckill.pojo.Seckill;
import com.lwj.seckill.service.SeckillService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * create by lwj on 2019/11/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SeckillApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
class SeckillServiceImplTest {

    @Autowired
    private SeckillService seckillService;

    @Test
    void getSeckillList() {
        List<Seckill> seckillList = seckillService.getSeckillList();
        log.info(seckillList.toString());
    }

    @Test
    void getById() {
        Seckill byId = seckillService.getById(1000);
        log.info(byId.toString());
    }

    @Test
    void exportSeckillUrl() {
        Exposer exposer = seckillService.exportSeckillUrl(1000);
        log.info(exposer.toString());
        //exposed=true, md5=00ed7603a536afda8f7affb94d7657ec,
        //seckillId=1000, now=0, start=0, end=0)


    }

    @Test
    void executeSeckill() {
        SeckillExecution seckillExecution = seckillService.executeSeckill(1000, 15229265351l, "00ed7603a536afda8f7affb94d7657ec");
        log.info(seckillExecution.toString());
        //(exposed=true, md5=00ed7603a536afda8f7affb94d7657ec, seckillId=1000, now=0, start=0, end=0)

    }
}