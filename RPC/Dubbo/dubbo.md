# SPI

## TelnetHandler

`org.apache.dubbo.remoting.telnet.TelnetHandler` 

实现该接口可以在终端下通过 telnel 访问服务器，查看RPC中已注册的接口及方法列表，查询入参出参的结构就是通过这个实现的。

还可以用来做一些在线运维的工作。

**示例**

```java
@Activate
@Help(parameter = "[-l] [service]", summary = "List services and methods.", detail = "List services and methods.")
public class ListTelnetHandler implements TelnetHandler {
  
  private ServiceRepository serviceRepository = ApplicationModel.getServiceRepository();

  @Override
  public String telnet(Channel channel, String message) {
    return "res"
  }
}
```

SPI配置：META-INF/dubbo/org.apache.dubbo.remoting.telnet.TelnetHandler

```properties
#配置形式：待处理命令=解析器
ls=com.lwj.ListTelnetHandler
```

```shell
telnet 127.0.0.1 20880 
dubbo> ls 
# output
res
```





# Dubbo启动流程入口

扫描到starter包下的 `org.apache.dubbo.spring.boot.autoconfigure.DubboAutoConfiguration`  创建bean对象

在实例化对象后，初始化对象阶段（initializeBean方法）会调用 BeanPostProcessor接口的处理方法

postProcessBeforeInitialization（初始化前处理方法） postProcessAfterInitialization（初始化后处理方法）

```java
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
		Object wrappedBean = bean;
		if (mbd == null || !mbd.isSynthetic()) {
      //初始化前处理方法
			wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
		}

		try {
      //初始化方法
			invokeInitMethods(beanName, wrappedBean, mbd);
		}
		catch (Throwable ex) {
			throw new BeanCreationException(
					(mbd != null ? mbd.getResourceDescription() : null),
					beanName, "Invocation of init method failed", ex);
		}
		if (mbd == null || !mbd.isSynthetic()) {
      //初始化后处理方法
			wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
    }
```

**AbstractAutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization 初始化前处理方法**

![image-20230221214453172](img/dubbo/image-20230221214453172.png)

当前注册的BeanPostProcessor实现有下面四个：

- `ApplicationContextAwareProcessor`，感知ApplicationContext处理器，对实现ApplicationContextAware接口的bean注入ApplicationContext
- `ApplicationListenerDetector`，ApplicationListener探测器，将实现ApplicationListener接口的单例bean加入容器，实现了MergedBeanDefinitionPostProcessor接口，会在填充属性前就将bean名称及是否单例记录下来。
- `WebApplicationContextServletContextAwareProcessor`，
- `DubboConfigEarlyInitializationPostProcessor`，

**DubboAutoConfiguration 实现了ApplicationContextAware。**

在设置ApplicationContext的时候，向容器中注册了两个监听器 `DubboLifecycleComponentApplicationListener`、 `DubboBootstrapApplicationListener`，这两个监听器

```java
public class DubboAutoConfiguration implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor {
  
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext instanceof ConfigurableApplicationContext) {
            ConfigurableApplicationContext context = (ConfigurableApplicationContext)applicationContext;
          	//dubbo生命周期组件监听器
            DubboLifecycleComponentApplicationListener dubboLifecycleComponentApplicationListener = new DubboLifecycleComponentApplicationListener(applicationContext);
            context.addApplicationListener(dubboLifecycleComponentApplicationListener);
          	//dubbo启动引导监听器
            DubboBootstrapApplicationListener dubboBootstrapApplicationListener = new DubboBootstrapApplicationListener(applicationContext);
            context.addApplicationListener(dubboBootstrapApplicationListener);
        }
    }
}
```



```
spring=org.apache.dubbo.config.spring.extension.SpringExtensionFactory
adaptive=org.apache.dubbo.common.extension.factory.AdaptiveExtensionFactory
spi=org.apache.dubbo.common.extension.factory.SpiExtensionFactory
```
