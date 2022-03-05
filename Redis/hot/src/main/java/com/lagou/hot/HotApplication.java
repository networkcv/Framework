package com.lagou.hot;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lagou.hot.dao")
public class HotApplication {

    public static void main(String[] args) {

        SpringApplication.run(HotApplication.class, args);
    }

}
