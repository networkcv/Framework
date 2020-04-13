package com.lwj.sell;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan(basePackages = "com.lwj.sell.dataobject.mapper")
@EnableCaching
public class WxOrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(WxOrderApplication.class, args);
    }

}
