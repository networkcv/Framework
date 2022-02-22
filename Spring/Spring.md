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

```java
import com.lagou.edu.LagouBean;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class IocTest {

	/**
	 *  Ioc 容器源码分析基础案例
	 */
	@Test
	public void testIoC() {
		// ApplicationContext是容器的高级接口，BeanFacotry（顶级容器/根容器，规范了/定义了容器的基础行为）
		// Spring应用上下文，官方称之为 IoC容器（错误的认识：容器就是map而已；准确来说，map是ioc容器的一个成员，
		// 叫做单例池, singletonObjects,容器是一组组件和过程的集合，包括BeanFactory、单例池、BeanPostProcessor等以及之间的协作流程）

		/**
		 * Ioc容器创建管理Bean对象的，Spring Bean是有生命周期的
		 * 构造器执行、初始化方法执行、Bean后置处理器的before/after方法、：AbstractApplicationContext#refresh#finishBeanFactoryInitialization
		 * Bean工厂后置处理器初始化、方法执行：AbstractApplicationContext#refresh#invokeBeanFactoryPostProcessors
		 * Bean后置处理器初始化：AbstractApplicationContext#refresh#registerBeanPostProcessors
		 */

		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		LagouBean lagouBean = applicationContext.getBean(LagouBean.class);
		System.out.println(lagouBean);
	}



	/**
	 *  Ioc 容器源码分析基础案例
	 */
	@Test
	public void testAOP() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		LagouBean lagouBean = applicationContext.getBean(LagouBean.class);
		lagouBean.print();
	}
}

```

# SpringBean的创建调用链

- 创建 BeanFactory，并加载BeanDefinition

- 从 AbstractRefreshableApplicationContext 获取 BeanFactory

- 添加 ApplicationContextAwareProcessor，让继承自 ApplicationContextAware 的 Bean 对象都能感知所属的 ApplicationContext

- 执行BeanFactoryPostProcessor 修改BeanDefinition

- 注册BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作

- 初始化事件发布者

- 提前实例化单例对象（AbstractAutowireCapableBeanFactory）

  - 从 DefaultSingletonBeanRegistry 中的 singletonObjects 中获取单例对象

  - 取到的话，如果实现 FactoryBean，则从 FactoryBean取 getObject 代理后的Bean，不然直接返回

  - 取不到时，获取 BeanDifinition，进行 Bean 创建

    - 根据实例化策略创建Bean，Java反射或者Cglib创建

    - Bean 的属性填充，如果 A 依赖 B，则先对 B 进行实例化

    - 执行 Bean 的初始化方法和 BeanPostProcessor 的前置和后置处理方法

      - 执行Aware方法，填充对应的容器对象 

      - 执行 BeanPostProcessor before 处理

        applicationContext的填充是通过 BeanPostProcessor 来实现的

      - 执行Bean 自定义的初始化方法（xml指定或者 InitializingBean 接口）

      - 执行 BeanPostProcessor after 处理 

    - 注册实现了 DisposableBean 接口的非单例 Bean 对象，注册销毁的钩子，

      这里有个有趣的设计。ConfigurableBeanFactory 中定义了销毁单例对象的方法入口 destroySingletons，AbstractBeanFactory 实现了 ConfigurableBeanFactory接口，但destroySingletons 的具体实现是由 AbstractBeanFactory 的父类 DefaultSingletonBeanRegistry 来完成的。

    - 将单例对象添加到 DefaultSingletonBeanRegistry 的 singletonObjectsMap 

  - 创建后，如果实现 FactoryBean，则从 FactoryBean取 getObject 代理后的Bean ，不然直接返回





# @Configuration 和 @Component 区别

一句话概括就是 `@Configuration` 中所有带 `@Bean` 注解的方法都会被动态代理，因此调用该方法返回的都是同一个实例。

下面 userInfo() 中调用 country() 时，大家会认为这里的 Country 和上面 @Bean 方法返回的 Country 可能不是同一个对象，因此可能会通过下面的方式来替代这种方式：

@Autowired
private Country country;

实际上不需要这么做（后面会给出需要这样做的场景），直接调用 country() 方法返回的是同一个实例。

```java
@Configuration
public class MyBeanConfig {

    @Bean
    public Country country(){
        return new Country();
    }

    @Bean
    public UserInfo userInfo(){
        return new UserInfo(country());
    }

}

```

`@Component` 注解并没有通过 cglib 来代理`@Bean` 方法的调用，因此像下面这样配置时，就是两个不同的 country。

```java
@Component
public class MyBeanConfig {

    @Bean
    public Country country(){
        return new Country();
    }

    @Bean
    public UserInfo userInfo(){
        return new UserInfo(country());
    }

}
```

# 三级缓存解决循环依赖

A依赖B ，B依赖A

一级缓存（也就是对象池 singletonObjects） 二级缓存（earlySingletonObjects） 三级缓存（singletonFactories）

创建A——将不完整的A加入三级缓存——填充A的属性发现依赖B——容器（包括一二三级缓存）中拿不到B——创建B——将不完整的B加入三级缓存——填充B的属性发现依赖A——一级缓存和二级缓存中没有，但发现三级缓存里有A的引用——于是返回A对象引用，这样就完成了B对象的创建——B对象创建完成后就可以完成A对象的属性填充——最后完成A对象。



# 往容器加入Bean的几种方式

1. @Bean

2. @Import

3. @Configuration  

4. 实现ImportSelector

   ```java
   public class MyImportSelector implements ImportSelector {
       @Override
       public String[] selectImports(AnnotationMetadata importingClassMetadata) {
           return new String[]{"import_test.JavaBeanB"};
       }
   }
   ```

   

5. 实现ImportBeanDefinitionRegistrar接口

   ```java
   public class MyImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
       @Override
       public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
           RootBeanDefinition rootBeanDefinition = new RootBeanDefinition(JavaBeanA.class);
           registry.registerBeanDefinition("javaBeanA", rootBeanDefinition);
       }
   }
   ```

   
