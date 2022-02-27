# Spring容器核心流程

## 一、刷新前的准备

```java
// Prepare this context for refreshing.
prepareRefresh();
```

## 二、获取刷新后的BeanFactory

会去通知子类加载 BeanDefinition 并创建了 BeanFactory ，AbstractApplicationContext 类中定义了刷新容器的流程，将刷新BeanFactory 的过程交给子类 **AbstractRefreshableApplicationContext** 来实现。其中创建Bean工厂后加载BeanDefinitions又交给了其具体的子类来实现。

```java
// Tell the subclass to refresh the internal bean factory.
ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
```

```java
//org.springframework.context.support.AbstractApplicationContext#obtainFreshBeanFactory
  protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
		refreshBeanFactory();
		return getBeanFactory();
	}
```

```java
//org.springframework.context.support.AbstractRefreshableApplicationContext#refreshBeanFactory
	protected final void refreshBeanFactory() throws BeansException {
			...
			DefaultListableBeanFactory beanFactory = createBeanFactory();
			beanFactory.setSerializationId(getId());
			customizeBeanFactory(beanFactory);
			loadBeanDefinitions(beanFactory);
			this.beanFactory = beanFactory;
			...	
	}
```

## 三、BeanFactory交给容器使用前的准备

对BeanFactory进行设置，比如设置类加载器、添加一些BeanPostProcessor（Bean的后置处理器）。例如ApplicationContextAwareProcessor，让继承自 ApplicationContextAware 的 Bean 对象都能感知所属的 ApplicationContext，也就是能拿到所属容器。

```java
// Prepare the bean factory for use in this context.
  prepareBeanFactory(beanFactory);
```

```java
//org.springframework.context.support.AbstractApplicationContext#prepareBeanFactory
	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		// Tell the internal bean factory to use the context's class loader etc.
		beanFactory.setBeanClassLoader(getClassLoader());
		...
		// Configure the bean factory with context callbacks.
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
		...
		// Register early post-processor for detecting inner beans as ApplicationListeners.
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));
		...
	}
```

## 四、执行BeanFactory的后置处理

执行BeanFactory的PostProcessor。

```java
// Invoke factory processors registered as beans in the context.
	invokeBeanFactoryPostProcessors(beanFactory);
```

```java
//org.springframework.context.support.AbstractApplicationContext#invokeBeanFactoryPostProcessors
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
		PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(
      beanFactory, getBeanFactoryPostProcessors());
		...
	}
```

**PostProcessorRegistrationDelegate** 这个是 **AbstractApplicationContext** 进行后置处理的委托类，不止是 **调用** **BeanFactoryPostProcessor** 的实现类，**BeanPostProcessor** 的实现类也是由该类来**注册**。

```java
//org.springframework.context.support.PostProcessorRegistrationDelegate#invokeBeanFactoryPostProcessors
public static void invokeBeanFactoryPostProcessors(
			ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

		//首先调用BeanDefinitionRegistryPostProcessors。
		Set<String> processedBeans = new HashSet<String>();

		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
			List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList<BeanFactoryPostProcessor>();
			List<BeanDefinitionRegistryPostProcessor> registryProcessors = new LinkedList<BeanDefinitionRegistryPostProcessor>();

			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
					registryProcessors.add(registryProcessor);
				}
				else {
					regularPostProcessors.add(postProcessor);
				}
			}

			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<BeanDefinitionRegistryPostProcessor>();

      // BeanDefinitionRegistryPostProcessors的执行有优先级，优先执行实现PriorityOrdered接口的。
			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
			String[] postProcessorNames =
					beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);
      //这里是执行BeanDefinitionRegistry的后置处理，BeanDefinitionRegistryPostProcessor接口其实是实现了BeanFactoryPostProcessor接口的。
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			currentRegistryProcessors.clear();

      //前边执行了实现PriorityOrdered接口的，此处再执行实现Ordered接口的，执行前会根据order值进行排序。
			// Next, invoke the BeanDefinitionRegistryPostProcessors that implement Ordered.
			postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				if (!processedBeans.contains(ppName) && beanFactory.isTypeMatch(ppName, Ordered.class)) {
					currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
					processedBeans.add(ppName);
				}
			}
			sortPostProcessors(currentRegistryProcessors, beanFactory);
			registryProcessors.addAll(currentRegistryProcessors);
			invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
			currentRegistryProcessors.clear();

      //这里最后执行其他的不参与排序的BeanDefinitionRegistryPostProcessors。
			// Finally, invoke all other BeanDefinitionRegistryPostProcessors until no further ones appear.
			boolean reiterate = true;
			while (reiterate) {
				reiterate = false;
				postProcessorNames = beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
				for (String ppName : postProcessorNames) {
					if (!processedBeans.contains(ppName)) {
						currentRegistryProcessors.add(beanFactory.getBean(ppName, BeanDefinitionRegistryPostProcessor.class));
						processedBeans.add(ppName);
						reiterate = true;
					}
				}
				sortPostProcessors(currentRegistryProcessors, beanFactory);
				registryProcessors.addAll(currentRegistryProcessors);
				invokeBeanDefinitionRegistryPostProcessors(currentRegistryProcessors, registry);
				currentRegistryProcessors.clear();
			}

			// Now, invoke the postProcessBeanFactory callback of all processors handled so far.
			invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
			invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
		}

		else {
			// Invoke factory processors registered with the context instance.
			invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
		}

		// Do not initialize FactoryBeans here: We need to leave all regular beans
		// uninitialized to let the bean factory post-processors apply to them!
		String[] postProcessorNames =
				beanFactory.getBeanNamesForType(BeanFactoryPostProcessor.class, true, false);

		// Separate between BeanFactoryPostProcessors that implement PriorityOrdered,
		// Ordered, and the rest.
		List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
		List<String> orderedPostProcessorNames = new ArrayList<String>();
		List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
		for (String ppName : postProcessorNames) {
			if (processedBeans.contains(ppName)) {
				// skip - already processed in first phase above
			}
			else if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
				priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
			}
			else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
				orderedPostProcessorNames.add(ppName);
			}
			else {
				nonOrderedPostProcessorNames.add(ppName);
			}
		}

		// First, invoke the BeanFactoryPostProcessors that implement PriorityOrdered.
		sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

		// Next, invoke the BeanFactoryPostProcessors that implement Ordered.
		List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
		for (String postProcessorName : orderedPostProcessorNames) {
			orderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		sortPostProcessors(orderedPostProcessors, beanFactory);
		invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);

		// Finally, invoke all other BeanFactoryPostProcessors.
		List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
		for (String postProcessorName : nonOrderedPostProcessorNames) {
			nonOrderedPostProcessors.add(beanFactory.getBean(postProcessorName, BeanFactoryPostProcessor.class));
		}
		invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);

		// Clear cached merged bean definitions since the post-processors might have
		// modified the original metadata, e.g. replacing placeholders in values...
		beanFactory.clearMetadataCache();
	}
```



## 五、注册Bean的后置处理

这里只是注册这些BeanPostProcessor，具体的调用是在初始化Bean中。

```java
// Register bean processors that intercept bean creation.
	registerBeanPostProcessors(beanFactory);
```

## 六、初始化Bean

这里其实只会初始化所有非延时加载的单例Bean。

# Spring常见知识点

## SpringBean的生命周期

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

## SpringBean的创建调用链

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



## BeanFactory 和 FactoryBean的区别

## @Configuration 和 @Component 区别

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

## Order 和 PriorityOrder

**spring中提供了2个可以给bean排序的接口**，规则如下：

1. 实现了PriorityOrdered的接口优先级比Orderd 高
2. 其次再按照getOrder的方法比较大小，数值越小，优先级越高
3. 实现了Orderd接口的bean比普通bean的优先级高
4. 顺序: PriorityOrdered >> Orderd >> 普通bean

参考 https://www.nanxhs.com/article/111

spring中2个接口的定义如下

```java
public interface Ordered {

    /**
     * 最小优先级的的数值
     * @see java.lang.Integer#MIN_VALUE
     */
    int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

    /**
     * 最大优先级的数值
     * @see java.lang.Integer#MAX_VALUE
     */
    int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

    /**
     * 返回优先级数值
     */
    int getOrder();
}
/**
* 此接口继承与Orderd接口
*/
public interface PriorityOrdered extends Ordered {
}
```



## spring提供的排序比较器类OrderComparator

```java
public class OrderComparator implements Comparator<Object> {
    ...
    @Override
    public int compare(@Nullable Object o1, @Nullable Object o2) {
        return doCompare(o1, o2, null);
    }

    private int doCompare(@Nullable Object o1, @Nullable Object o2, @Nullable OrderSourceProvider sourceProvider) {
        boolean p1 = (o1 instanceof PriorityOrdered);
        boolean p2 = (o2 instanceof PriorityOrdered);
        if (p1 && !p2) {
            return -1;
        }
        else if (p2 && !p1) {
            return 1;
        }

        int i1 = getOrder(o1, sourceProvider);
        int i2 = getOrder(o2, sourceProvider);
        return Integer.compare(i1, i2);
    }

    public static void sort(List<?> list) {
            if (list.size() > 1) {
                list.sort(INSTANCE);
            }
    }
    ....
}
```



## 三级缓存解决循环依赖

A依赖B ，B依赖A

一级缓存（也就是对象池 singletonObjects） 二级缓存（earlySingletonObjects） 三级缓存（singletonFactories）

创建A——将不完整的A加入三级缓存——填充A的属性发现依赖B——容器（包括一二三级缓存）中拿不到B——创建B——将不完整的B加入三级缓存——填充B的属性发现依赖A——一级缓存和二级缓存中没有，但发现三级缓存里有A的引用——于是返回A对象引用，这样就完成了B对象的创建——B对象创建完成后就可以完成A对象的属性填充——最后完成A对象。



## 往容器加入Bean的几种方式

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


# SpringBoot常见知识点
