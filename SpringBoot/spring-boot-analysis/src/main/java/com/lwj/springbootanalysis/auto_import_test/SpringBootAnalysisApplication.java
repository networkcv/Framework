package com.lwj.springbootanalysis.auto_import_test;

import com.lwj.springbootanalysis.base.Age;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootAnalysisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootAnalysisApplication.class, args);
        System.out.println(context.getBeanFactory().getBean(Age.class));
    }

}
