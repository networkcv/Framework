# SpringBean的生命周期

![image-20220208115525757](https://pic.networkcv.top/2022/02/08/image-20220208115525757.png)

![](https://pic.networkcv.top/2022/02/08/image-20220208115631417.png)

```java
package com.lagou.edu.pojo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 *
 * 拦截实例化之后的对象（实例化了并且属性注入了）
 */
@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if("lazyResult".equalsIgnoreCase(beanName)) {
            System.out.println("MyBeanPostProcessor  before方法拦截处理lazyResult");
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if("lazyResult".equalsIgnoreCase(beanName)) {
            System.out.println("MyBeanPostProcessor  after方法拦截处理lazyResult");
        }
        return bean;
    }
}

```

```java
package com.lagou.edu.pojo;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author 应癫
 */
public class Result implements BeanNameAware,BeanFactoryAware,ApplicationContextAware,InitializingBean, DisposableBean {

    private String status;
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("注册我成为bean时定义的id：" + name);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("管理我的beanfactory为：" + beanFactory);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("高级容器接口ApplicationContext：" + applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("实现接口 InitializingBean afterPropertiesSet......");
    }


    public void initMethod() {
        System.out.println("xml文件 'init-method' 指定 initMethod ...");
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("注解@PostConstruct postConstruct");
    }


    @PreDestroy
    public void preDestroy(){
        System.out.println("注解@PreDestroy preDestroy.....");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("实现DisposableBean接口 destroy...");
    }

    public void destroyMethod(){
        System.out.println("xml文件 'destroy-method' 指定 destroyMethod ...");
    }
}

/**
注册我成为bean时定义的id：lazyResult
管理我的beanfactory为：org.springframework.beans.factory.support.DefaultListableBeanFactory@68c9133c: definin
高级容器接口ApplicationContext：org.springframework.context.support.ClassPathXmlApplicationContext@6a472554 
MyBeanPostProcessor  before方法拦截处理lazyResult
注解@PostConstruct postConstruct
实现接口 InitializingBean afterPropertiesSet......
xml文件 'init-method' 指定 initMethod ...
MyBeanPostProcessor  after方法拦截处理lazyResult
注解@PreDestroy preDestroy.....
实现DisposableBean接口 destroy...
xml文件 'destroy-method' 指定 destroyMethod ...
*/
```

