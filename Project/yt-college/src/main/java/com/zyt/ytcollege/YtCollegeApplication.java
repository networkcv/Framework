package com.zyt.ytcollege;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.zyt.ytcollege.dao.mapper")
public class YtCollegeApplication {

	public static void main(String[] args) {
		SpringApplication.run(YtCollegeApplication.class, args);
	}

}
