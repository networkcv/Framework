package com.lwj.springbootanalysis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationImportSelector;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootAnalysisApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(SpringBootAnalysisApplication.class, args);
        AutoConfigurationImportSelector bean = context.getBean(AutoConfigurationImportSelector.class);
    }

}
