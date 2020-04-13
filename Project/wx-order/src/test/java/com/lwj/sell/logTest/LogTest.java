package com.lwj.sell.logTest;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * create by lwj on 2020/4/8
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class LogTest {
    @Test
    public void test1(){
        log.debug("111");
        log.info("222");
        log.error("333");
    }
}
