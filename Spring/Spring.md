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

调用 BeanFactoryPostProcessor，执行Bean工厂的后置处理器。

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
		//处理后的Bean集合
  	Set<String> processedBeans = new HashSet<String>();		
	  //这里先处理入参中实现了BeanDefinitionRegistryPostProcessor的处理器。这个是BD注册表后置处理接口，它继承了BeanFactoryPostProcessor接口。是在所有常规 Bean 定义信息被加载但是还未被实例化的时候调用。可以添加、修改 Bean 的定义信息。
		if (beanFactory instanceof BeanDefinitionRegistry) {
			BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;
      //常规后置处理器集合
			List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList<BeanFactoryPostProcessor>();
			//全部注册表后置处理器集合
      List<BeanDefinitionRegistryPostProcessor> registryProcessors = new LinkedList<BeanDefinitionRegistryPostProcessor>();
			//过滤出实现了BeanDefinitionRegistryPostProcessor的处理器，bean定义注册表后置处理器
			for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
				if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
					BeanDefinitionRegistryPostProcessor registryProcessor =
							(BeanDefinitionRegistryPostProcessor) postProcessor;
          //执行bean定义注册表后置处理器的处理逻辑
					registryProcessor.postProcessBeanDefinitionRegistry(registry);
          //将处理后的处理器加入注册表后置处理器集合
					registryProcessors.add(registryProcessor);
				}
				else {
          //加入常规后置处理器集合
					regularPostProcessors.add(postProcessor);
				}
			}
			
      //上面处理了入参中的BD注册表后置处理器，下面要处理定义在BD注册表中的后置处理器
			// Do not initialize FactoryBeans here: We need to leave all regular beans
			// uninitialized to let the bean factory post-processors apply to them!
			// Separate between BeanDefinitionRegistryPostProcessors that implement
			// PriorityOrdered, Ordered, and the rest.
      //这里不初始化工厂Bean，我们需要将所有的常规Beans保持未初始化状态，直到BeanFactory的后置处理器调用后。
      //这里的集合是一会要当前要调用的注册表后置处理器集合
			List<BeanDefinitionRegistryPostProcessor> currentRegistryProcessors = new ArrayList<BeanDefinitionRegistryPostProcessor>();


			// First, invoke the BeanDefinitionRegistryPostProcessors that implement PriorityOrdered.
      //优先调用BeanDefinitionRegistryPostProcessors实现类中实现了PriorityOrdered接口的类
      //这里在获取Bean名称的时候已经完成了Bean的创建（工厂Bean除外）
			String[] postProcessorNames =
        beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
			for (String ppName : postProcessorNames) {
				if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
          //这里先将要执行的BD注册表后置处理器优先放入currentRegistryProcessors集合中
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

# Spring汇总

## SpringBean的生命周期

![wecom-temp-eaa9892cad9edb70cdc20c75ca21ad73](https://pic.networkcv.top/2022/03/01/wecom-temp-eaa9892cad9edb70cdc20c75ca21ad73.png)

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

### BeanFactory

BeanFactory是Spring容器中Bean工厂的顶级接口，它的本质是获取Bean的工厂，因此它的接口中定义了工厂的一些能力。

```java
public interface BeanFactory {
	//获取Bean的多种方式
	Object getBean(String name) throws BeansException;
	<T> T getBean(String name, Class<T> requiredType) throws BeansException;
	Object getBean(String name, Object... args) throws BeansException;
	<T> T getBean(Class<T> requiredType) throws BeansException;
	<T> T getBean(Class<T> requiredType, Object... args) throws BeansException;
  //获取Bean的提供者
	<T> ObjectProvider<T> getBeanProvider(Class<T> requiredType);
	<T> ObjectProvider<T> getBeanProvider(ResolvableType requiredType);
  //是否包含对应的Bean
  boolean containsBean(String name);
  //判断Bean是否单例
  boolean isSingleton(String name) throws NoSuchBeanDefinitionException;
  boolean isPrototype(String name) throws NoSuchBeanDefinitionException;
  //判断类型是否匹配，如果是FactoryBean的话会用其产生的Bean来进行类型比对
  //（检查给定名称的getBean调用是否会返回一个可分配给指定目标类型的对象。将别名转换回相应的规范bean名。也会查）
  boolean isTypeMatch(String name, ResolvableType typeToMatch) throws NoSuchBeanDefinitionException;
  boolean isTypeMatch(String name, Class<?> typeToMatch) throws NoSuchBeanDefinitionException;
  //根据beanName获取类型
  Class<?> getType(String name) throws NoSuchBeanDefinitionException;
	Class<?> getType(String name, boolean allowFactoryBeanInit) throws NoSuchBeanDefinitionException;
  //根据beanName获取别名
  String[] getAliases(String name);
```

### FactoryBean

FactoryBean是一种工厂Bean，核心方法是 getObject。 它可以作为工厂来生产对应的Bean实例，将复杂构建逻辑封装起来，不需要让使用方感知到，它也可以修饰对象的生成。

```java
public interface FactoryBean<T> {
	T getObject() throws Exception;
	Class<?> getObjectType();
	default boolean isSingleton() {
		return true;
	}
```

例如 ForkJoinPoolFactoryBean，这个工厂Bean就是为了获取 ForkJoinPool线程池的。

```java
//org.springframework.scheduling.concurrent.ForkJoinPoolFactoryBean
public class ForkJoinPoolFactoryBean implements FactoryBean<ForkJoinPool>, InitializingBean, DisposableBean {

	private int parallelism = Runtime.getRuntime().availableProcessors();

	private ForkJoinPool.ForkJoinWorkerThreadFactory threadFactory = ForkJoinPool.defaultForkJoinWorkerThreadFactory;

	@Nullable
	private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

	private boolean asyncMode = false;

	private int awaitTerminationSeconds = 0;

  ...

  //这部分是创建ForkJoin池的逻辑，做到更加内聚。
	@Override
	public void afterPropertiesSet() {
		this.forkJoinPool = (this.commonPool ? ForkJoinPool.commonPool() :
				new ForkJoinPool(this.parallelism, this.threadFactory, this.uncaughtExceptionHandler, this.asyncMode));
	}

	@Override
	@Nullable
	public ForkJoinPool getObject() {
		return this.forkJoinPool;
	}

	@Override
	public Class<?> getObjectType() {
		return ForkJoinPool.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}
}

```



## @Configuration 和 @Component 区别

概括就是 `@Configuration` 中可以设置带 `@Bean` 注解的方法需不需要被动态代理（默认为true）。

简单来说，方法内的每次调用@Bean注解的方法都回返回同一个实例（前提这个Bean是单例Bean）。

例如下面 userInfo() 中调用 country() 时，返回的总是同一个实例。将@Configuration替换成@Component，则返回的就是不同的country。

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

## Order 和 PriorityOrder

**spring中提供了2个可以给bean排序的接口**，规则如下：

1. 实现了PriorityOrdered的接口优先级比Orderd 高
2. 其次再按照getOrder的方法比较大小，数值越小，优先级越高
3. 实现了Orderd接口的bean比普通bean的优先级高
4. 顺序: PriorityOrdered > Orderd >>普通bean

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

### 核心原理

将未完成创建的对象引用提前暴露出来，来完成循环依赖中一方的对象创建，从而打破循环依赖。

背景：A依赖B ，B依赖A

名词：一级缓存（也就是对象池 singletonObjects） 二级缓存（earlySingletonObjects） 三级缓存（singletonFactories）

创建A——将不完整的A加入三级缓存——填充A的属性发现依赖B——容器（包括一二三级缓存）中拿不到B——创建B——将不完整的B加入三级缓存——填充B的属性发现依赖A——一级缓存和二级缓存中没有，但发现三级缓存里有A的引用——于是返回A对象引用，这样就完成了B对象的创建——B对象创建完成后就可以完成A对象的属性填充——最后完成A对象。

### 为什么是三级缓存？二级缓存能解决吗？



## AOP实现原理

在初始化Bean的过程中 BeanPostProcessor#postProcessAfterInitialization 方法，由 AbstractAutoProxyCreator对原始对象进行包装，产生包含通知的代理类。

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

## Spring的初始化器-ApplicationContextInitializer

这个是Spring在刷新容器前，会去调用的一个接口。

https://blog.csdn.net/pengjunlee/article/details/79394735

### SharedMetadataReaderFactoryContextInitializer

​	注册一个CachingMetadataReaderFactoryPostProcessor处理器。

```java
@Override
public void initialize(ConfigurableApplicationContext applicationContext) {
   BeanFactoryPostProcessor postProcessor = new CachingMetadataReaderFactoryPostProcessor(applicationContext);
   applicationContext.addBeanFactoryPostProcessor(postProcessor);
}
```

**CachingMetadataReaderFactoryPostProcessor**作用：

1. 向BeanDefinitionRegistry中注册一个BD——internalCachingMetadataReaderFactory
2. 设置internalConfigurationAnnotationProcessor的BD属性metadataReaderFactory，指向一个运行时的Bean引用（刚才的internalCachingMetadataReaderFactory包装的RuntimeBeanReference）

```java
//org.springframework.boot.autoconfigure.SharedMetadataReaderFactoryContextInitializer
class SharedMetadataReaderFactoryContextInitializer
		implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

		@Override
		public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
      //向注册表中注册internalCachingMetadataReaderFactory
			register(registry);
      //对配置类的后置处理器进行属性设置
			configureConfigurationClassPostProcessor(registry);
		}
  	...
}
```

### ConfigurationWarningsApplicationContextInitializer

这个初始化器里添加了一个ConfigurationWarningsPostProcessor，这个是BD注册表的后置处理器。

```java
protected static final class ConfigurationWarningsPostProcessor
			implements PriorityOrdered, BeanDefinitionRegistryPostProcessor 
```

这个处理器的作用是拿到所有的 ComponentScan 注解属性，然后检测它是否是 org.springframework 或者 org ，如果是则会打印异常信息 Your ApplicationContext is unlikely to start due to a @ComponentScan of。



## ConfigurationClassParser

该类负责解析Configuration类的BeanDefinition，并收集到ConfigurationClassParser#configurationClasses中。

解析单个Configuration类可能导致任意数量的ConfigurationClass对象，因为一个Configuration类可能使用import注释导入另一个Configuration类。

这个类可以帮助我们将Configuration类的结构与BeanDefinition类的分离开。基于asm的实现，避免了反射和即时类加载，以便在容器中有效地进行懒加载类加载。

```
private final Map<ConfigurationClass, ConfigurationClass> configurationClasses = new LinkedHashMap<>();
```

核心流程 parse方法。先去解析每个注解类，最后执行DeferredImportSelector的实现类。

```java
	public void parse(Set<BeanDefinitionHolder> configCandidates) {
		for (BeanDefinitionHolder holder : configCandidates) {
			BeanDefinition bd = holder.getBeanDefinition();
				if (bd instanceof AnnotatedBeanDefinition) {
          //解析每个注解类
					parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
				}
				else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
					parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
				}
				else {
					parse(bd.getBeanClassName(), holder.getBeanName());
				}
			}
    //执行DeferredImportSelector的实现类
		this.deferredImportSelectorHandler.process();
	}
```

具体的解析流程

```java
protected void processConfigurationClass(ConfigurationClass configClass, Predicate<String> filter) {
		...
    //递归解析配置类以及其父配置类 
		// Recursively process the configuration class and its superclass hierarchy.
		SourceClass sourceClass = asSourceClass(configClass, filter);
		do {
			sourceClass = doProcessConfigurationClass(configClass, sourceClass, filter);
		}
		while (sourceClass != null);
		//保存配置类
		this.configurationClasses.put(configClass, configClass);
	}
```

```java
protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass) throws IOException {
		...
	  // 处理@ComponentScan 注解
		Set<AnnotationAttributes> componentScans = AnnotationConfigUtils.attributesForRepeatable(
				sourceClass.getMetadata(), ComponentScans.class, ComponentScan.class);
		if (!componentScans.isEmpty() &&
				!this.conditionEvaluator.shouldSkip(sourceClass.getMetadata(), ConfigurationPhase.REGISTER_BEAN)) {
			for (AnnotationAttributes componentScan : componentScans) {
				// The config class is annotated with @ComponentScan -> perform the scan immediately
				Set<BeanDefinitionHolder> scannedBeanDefinitions =
						this.componentScanParser.parse(componentScan, sourceClass.getMetadata().getClassName());
				// Check the set of scanned definitions for any further config classes and parse recursively if needed
				for (BeanDefinitionHolder holder : scannedBeanDefinitions) {
					BeanDefinition bdCand = holder.getBeanDefinition().getOriginatingBeanDefinition();
					if (bdCand == null) {
						bdCand = holder.getBeanDefinition();
					}
					if (ConfigurationClassUtils.checkConfigurationClassCandidate(bdCand, this.metadataReaderFactory)) {
						parse(bdCand.getBeanClassName(), holder.getBeanName());
					}
				}
			}
		}
  
        ...
        // 处理@Import注解
        processImports(configClass, sourceClass, getImports(sourceClass), true);

        // 处理@ImportResource注解
        if (sourceClass.getMetadata().isAnnotated(ImportResource.class.getName())) {
            AnnotationAttributes importResource = AnnotationConfigUtils.attributesFor(sourceClass.getMetadata(), ImportResource.class);
            String[] resources = importResource.getStringArray("value");
            Class<? extends BeanDefinitionReader> readerClass = importResource.getClass("reader");
            for (String resource : resources) {
                String resolvedResource = this.environment.resolveRequiredPlaceholders(resource);
                configClass.addImportedResource(resolvedResource, readerClass);
            }
        }

        // 处理@Bean注解，注意是处理注解，不是执行@Bean修饰的方法
        Set<MethodMetadata> beanMethods = sourceClass.getMetadata().getAnnotatedMethods(Bean.class.getName());
        for (MethodMetadata methodMetadata : beanMethods) {
            configClass.addBeanMethod(new BeanMethod(methodMetadata, configClass));
        }

        // 处理Configuration类的父类，外面在调用doProcessConfigurationClass方法的时有迭代处理，确保所有父类的注解都会被处理
        if (sourceClass.getMetadata().hasSuperClass()) {
            String superclass = sourceClass.getMetadata().getSuperClassName();
            if (!superclass.startsWith("java") && !this.knownSuperclasses.containsKey(superclass)) {
                this.knownSuperclasses.put(superclass, configClass);
                // Superclass found, return its annotation metadata and recurse
                return sourceClass.getSuperClass();
            }
        }

        // 再也没有父类了，返回null表示当前Configuration处理完毕
        return null;
    }
```



# SpringBoot汇总

在调用BeanFactoryRegistryPostProcessor 中，找到了ConfigurationClassPostProcessor。

ConfigurationClassPostProcessor同时实现了BeanFactoryPostProcessor接口的BeanFactoryPostProcessor方法和BeanDefinitionRegistryPostProcessor接口的postProcessBeanDefinitionRegistry方法。

先调用postProcessBeanDefinitionRegistry。首先拿到BD注册表中的配置类BD，然后创建配置类解析器ConfigurationClassParser去解析刚才拿到的配置类BD。这个解析类也是解析SpirngBoot自动配置的关键类。

```java
//org.springframework.context.annotation.ConfigurationClassPostProcessor#processConfigBeanDefinitions
public void processConfigBeanDefinitions(BeanDefinitionRegistry registry) {
		List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
		String[] candidateNames = registry.getBeanDefinitionNames();

		for (String beanName : candidateNames) {
      //首先拿到BD注册表中的配置类BD
			BeanDefinition beanDef = registry.getBeanDefinition(beanName);
			if (beanDef.getAttribute(ConfigurationClassUtils.CONFIGURATION_CLASS_ATTRIBUTE) != null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Bean definition has already been processed as a configuration class: " + beanDef);
				}
			}
      //校验候选的beanName是否是配置类
			else if (ConfigurationClassUtils.checkConfigurationClassCandidate(beanDef, this.metadataReaderFactory)) {
				configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
			}
		}
		...	

		// Parse each @Configuration class
    //创建配置类解析器ConfigurationClassParser
		ConfigurationClassParser parser = new ConfigurationClassParser(
				this.metadataReaderFactory, this.problemReporter, this.environment,
				this.resourceLoader, this.componentScanBeanNameGenerator, registry);

		Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
		Set<ConfigurationClass> alreadyParsed = new HashSet<>(configCandidates.size());
		do {
			StartupStep processConfig = this.applicationStartup.start("spring.context.config-classes.parse");
      //解析刚才拿到的配置类BD
			parser.parse(candidates);
			parser.validate();

      ...
		}
		while (!candidates.isEmpty());

	}

```

在该方法的处理过程中，会将解析出的配置类保存到ConfigurationClassParser的属性configurationClasses中。（带有 @SpringBootApplication 注解的核心启动类，就是在这里完成的进行解析，并且基础包路径下的配置类也会保存进来）

解析后，具体的处理操作交给了ConfigurationClassParser的属性deferredImportSelectorHandler（延迟导入选择处理器）来处理。

```java
//org.springframework.context.annotation.ConfigurationClassParser#parse(java.util.Set<org.springframework.beans.factory.config.BeanDefinitionHolder>)
public void parse(Set<BeanDefinitionHolder> configCandidates) {
		for (BeanDefinitionHolder holder : configCandidates) {
			BeanDefinition bd = holder.getBeanDefinition();
			try {
				if (bd instanceof AnnotatedBeanDefinition) {
          //会将解析出的配置类保存到ConfigurationClassParser的属性configurationClasses中
					parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
				}
				else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass()) {
					parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
				}
				else {
					parse(bd.getBeanClassName(), holder.getBeanName());
				}
			}
			catch (BeanDefinitionStoreException ex) {
				throw ex;
			}
			catch (Throwable ex) {
				throw new BeanDefinitionStoreException(
						"Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
			}
		}
		//解析拿到配置类的后续处理
		this.deferredImportSelectorHandler.process();
	}
```

DeferredImportSelectorHandler内部持有一个DeferredImportSelectorHolder集合的属性

```java
	private class DeferredImportSelectorHandler {
		private List<DeferredImportSelectorHolder> deferredImportSelectors = new ArrayList<>();
  	...
  }
```

```java
	private static class DeferredImportSelectorHolder {
		private final ConfigurationClass configurationClass;
		private final DeferredImportSelector importSelector;
  }
```

在deferredImportSelectorHandler的处理逻辑中
