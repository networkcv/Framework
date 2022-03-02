package com.lwj.springbootanalysis.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Date: 2022/3/1
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Component
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student implements ApplicationContextAware, BeanPostProcessor {
    private ApplicationContext applicationContext;

    private String name = "lwj";

    @Resource
    private Age age;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization " + bean + " + " + beanName);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization " + bean + " + " + beanName);
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }
}
