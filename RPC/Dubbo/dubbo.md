# Dubbo介绍

dubbo是一款微服务开发框架，它抽象了一套RPC接口的定义、暴露、调用和治理的规范。

dubbo不止是RPC调用框架，在服务治理方面它还包含了服务发现、负载均衡以及流量管控等。

**在信息交换方面**，dubbo支持多种通信协议，如 HTTP/2、REST、gRPC、JsonRPC、Thrift、Hessian2 等几乎所有主流的通信协议，同时还可以支持私有协议。

Dubbo 提供了两款内置高性能 Dubbo2、Triple (兼容 gRPC) 协议实现

- Dubbo2 协议是在 TCP 传输层协议之上设计的二进制通信协议
- Triple 则是基于 HTTP/2 之上构建的支持流式模式的通信协议，并且 Triple 完全兼容 gRPC 。

**在服务治理方面**，dubbo解决了无状态服务节点动态变化、外部化配置、日志跟踪、可观测性、流量管理、高可用性、数据一致性等一系列问题

- **地址发现**，Dubbo 服务发现具备高性能、支持大规模集群、服务级元数据配置等优势，默认提供 Nacos、Zookeeper、Consul 等多种注册中心适配，与 Spring Cloud、Kubernetes Service 模型打通，支持自定义扩展。

- **负载均衡**，Dubbo 默认提供加权随机、加权轮询、最少活跃请求数优先、最短响应时间优先、一致性哈希和自适应负载等策略

- **流量路由**，Dubbo 支持通过一系列流量规则控制服务调用的流量分布与行为，基于这些规则可以实现基于权重的比例流量分发、灰度验证、金丝雀发布、按请求参数的路由、同区域优先、超时配置、重试、限流降级等能力。

- **链路追踪**，Dubbo 官方通过适配 OpenTelemetry 提供了对 Tracing 全链路追踪支持，用户可以接入支持 OpenTelemetry 标准的产品如 Skywalking、Zipkin 等。另外，很多社区如 Skywalking、Zipkin 等在官方也提供了对 Dubbo 的适配。

- **可观测性**，Dubbo 实例通过 Prometheus 等上报 QPS、RT、请求次数、成功率、异常次数等多维度的可观测指标帮助了解服务运行状态，通过接入 Grafana、Admin 控制台帮助实现数据指标可视化展示。

Dubbo 服务治理生态还提供了对 **API 网关**、**限流降级**、**数据一致性**、**认证鉴权**等场景的适配支持。



# Dubbo 和 Spring Cloud 对比

Spring Cloud 的优势在于：

- 同样都支持 Spring 开发体系的情况下，Spring Cloud 得到更多的原生支持
- 对一些常用的微服务模式做了抽象如服务发现、动态配置、异步消息等，同时包括一些批处理任务、定时任务、持久化数据访问等领域也有涉猎。
- 基于 HTTP 的通信模式，加上相对比较完善的入门文档和演示 demo 和 starters，让开发者在第一感觉上更易于上手

Spring Cloud 的问题有：

- 只提供抽象模式的定义不提供官方稳定实现，开发者只能寻求类似 Netflix、Alibaba、Azure 等不同厂商的实现套件，而每个厂商支持的完善度、稳定性、活跃度各异
- 有微服务全家桶却不是能拿来就用的全家桶，demo 上手容易，但落地推广与长期使用的成本非常高
- 欠缺服务治理能力，尤其是流量管控方面如负载均衡、流量路由方面能力都比较弱
- 编程模型与通信协议绑定 HTTP，在性能、与其他 RPC 体系互通上存在障碍
- 总体架构与实现只适用于小规模微服务集群实践，当集群规模增长后就会遇到地址推送效率、内存占用等各种瓶颈的问题，但此时迁移到其他体系却很难实现
- 很多微服务实践场景的问题需要用户独自解决，比如优雅停机、启动预热、服务测试，再比如双注册、双订阅、延迟注册、服务按分组隔离、集群容错等

而以上这些点，都是 **Dubbo 的优势**所在：

- 完全支持 Spring & Spring Boot 开发模式，同时在服务发现、动态配置等基础模式上提供与 Spring Cloud 对等的能力。
- 是企业级微服务实践方案的整体输出，Dubbo 考虑到了企业微服务实践中会遇到的各种问题如优雅上下线、多注册中心、流量管理等，因此其在生产环境的长期维护成本更低
- 在通信协议和编码上选择更灵活，包括 rpc 通信层协议如 HTTP、HTTP/2(Triple、gRPC)、TCP 二进制协议、rest等，序列化编码协议Protobuf、JSON、Hessian2 等，支持单端口多协议。
- Dubbo 从设计上突出服务服务治理能力，如权重动态调整、标签路由、条件路由等，支持 Proxyless 等多种模式接入 Service Mesh 体系
- 高性能的 RPC 协议编码与实现，
- Dubbo 是在超大规模微服务集群实践场景下开发的框架，可以做到百万实例规模的集群水平扩容，应对集群增长带来的各种问题
- Dubbo 提供 Java 外的多语言实现，使得构建多语言异构的微服务体系成为可能

如果您的目标是构建企业级应用，并期待在未来的持久维护中能够更省心、更稳定，我们建议你能更深入的了解 Dubbo 的使用和其提供的能力。

# Dubbo 与 gRPC

Dubbo 与 gRPC 最大的差异在于两者的定位上：

- **gRPC 定位为一款 RPC 框架**，Google 推出它的核心目标是定义云原生时代的 rpc 通信规范与标准实现；
- **Dubbo 定位是一款微服务开发框架**，它侧重解决微服务实践从服务定义、开发、通信到治理的问题，因此 Dubbo 同时提供了 RPC 通信、与应用开发框架的适配、服务治理等能力。

Dubbo 不绑定特定的通信协议，即 Dubbo 服务间可通过多种 RPC 协议通信并支持灵活切换。因此，你可以在 Dubbo 开发的微服务中选用 gRPC 通信，**Dubbo 完全兼容 gRPC，并将 gRPC 设计为内置原生支持的协议之一**。

如果您看中基于 HTTP/2 的通信协议、基于 Protobuf 的服务定义，并基于此决定选型 gRPC 作为微服务开发框架，那很有可能您会在未来的微服务业务开发中遇到障碍，这主要源于 gRPC 没有为开发者提供以下能力：

- 缺乏与业务应用框架集成的开发模式，用户需要基于 gRPC 底层的 RPC API 定义、发布或调用微服务，中间可能还有与业务应用开发框架整合的问题
- 缺乏微服务周边生态扩展与适配，如服务发现、限流降级、链路追踪等没有多少可供选择的官方实现，且扩展起来非常困难
- 缺乏服务治理能力，作为一款 rpc 框架，缺乏对服务治理能力的抽象

因此，gRPC 更适合作为底层的通信协议规范或编解码包，而 Dubbo 则可用作微服务整体解决方案。**对于 gRPC 协议，我们推荐的使用模式 Dubbo + gRPC 的组合**，这个时候，gRPC 只是隐藏在底层的一个通信协议，不被微服务开发者感知，开发者基于 Dubbo 提供的 API 和配置开发服务，并基于 dubbo 的服务治理能力治理服务，在未来，开发者还能使用 Dubbo 生态和开源的 IDL 配套工具管理服务定义与发布。

如果我们忽略 gRPC 在应用开发框架侧的空白，只考虑如何给 gRPC 带来服务治理能力，则另一种可以采用的模式就是在 Service Mesh 架构下使用 gRPC，这就引出了我们下一小节要讨论的内容：Dubbo 与 Service Mesh 架构的关系。



### Dubbo调用链路

[客户端]----------------->[Dubbo客户端]----------------->[网络]----------------->[服务提供者]
  │                                        │                                │                                │
  [业务请求]                          │                                │                                [业务处理]
  [Dubbo请求包装]              │                                │                                [Dubbo响应包装]
  [服务查找]                          │                                │                                [网络传输]
  [本地调用]                          │                                │                                [远程调用]
  [网络通信]                          │                                │                                [结果返回]
  [反序列化]                          │                                │                                [结果反序列化]
  [业务处理]                          │                                │                                [业务处理]
  [序列化]                              │                                │                                [Dubbo响应]
  [结果返回]                          │                                │                                [Dubbo客户端]
  [客户端]                              │                                │                                [业务响应]

调用过程：

1. **客户端调用**：客户端发起业务请求。
2. **Dubbo请求包装**：Dubbo客户端将业务请求包装成Dubbo请求。
3. **服务查找**：Dubbo客户端根据配置和注册中心信息查找服务提供者。
4. **本地调用**：Dubbo客户端发起本地调用。
5. **网络通信**：Dubbo客户端通过网络将请求发送到服务提供者。
6. **远程调用**：服务提供者接收到请求后，进行远程调用。
7. **业务处理**：服务提供者处理业务请求。
8. **Dubbo响应包装**：服务提供者将业务处理结果包装成Dubbo响应。
9. **网络传输**：服务提供者通过网络将Dubbo响应发送回客户端。
10. **反序列化**：Dubbo客户端反序列化Dubbo响应。
11. **业务处理**：Dubbo客户端处理业务响应。
12. **序列化**：服务提供者序列化业务处理结果。
13. **结果返回**：Dubbo客户端将业务处理结果返回给客户端。





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

在设置ApplicationContext的时候，向容器中注册了两个监听器 `DubboLifecycleComponentApplicationListener`、 `DubboBootstrapApplicationListener`

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

创建 `DubboBootstrapApplicationListener` 

```java
public class DubboBootstrapApplicationListener extends OnceApplicationContextEventListener implements Ordered {

    public static final String BEAN_NAME = "dubboBootstrapApplicationListener";

    private final DubboBootstrap dubboBootstrap;

    public DubboBootstrapApplicationListener(ApplicationContext applicationContext) {
        super(applicationContext);
        this.dubboBootstrap = DubboBootstrap.getInstance();
        DubboBootstrapStartStopListenerSpringAdapter.applicationContext = applicationContext;
    }
}
```



```java
//DubboBootstrap#getInstance
public class DubboBootstrap {
  
	  // executorRepository 是DubboBootstrap的属性，会在 new DubboBootstrap（）时触发创建
    private final ExecutorRepository executorRepository = getExtensionLoader(ExecutorRepository.class).getDefaultExtension();
  
    public static DubboBootstrap getInstance() {
        if (instance == null) {
            synchronized (DubboBootstrap.class) {
                if (instance == null) {
                    instance = new DubboBootstrap();
                }
            }
        }
        return instance;
    }

    private DubboBootstrap() {
        configManager = ApplicationModel.getConfigManager();
        environment = ApplicationModel.getEnvironment();

        DubboShutdownHook.getDubboShutdownHook().register();
        ShutdownHookCallbacks.INSTANCE.addCallback(DubboBootstrap.this::destroy);
    }
  
}
```

这里分析一下 `getExtensionLoader(ExecutorRepository.class).getDefaultExtension();`

获取 `ExecutorRepository`  的扩展加载器，然后获取默认扩展。但在获取扩展器之前需要 先获取到 `ExtensionFactory` 扩展加载器的默认扩展。最后返回的是 `ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension()`

```java
//org.apache.dubbo.common.extension.ExtensionLoader

//1.ExtensionLoader#getExtensionLoader
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) { //type is ExecutorRepository.class
		    //org.apache.dubbo.common.threadpool.manager.ExecutorRepository
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
          	//第一次会走这里去 new ExtensionLoader<T>(type)
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

//2.ExtensionLoader#ExtensionLoader 
    private ExtensionLoader(Class<?> type) { //ExecutorRepository.class
        this.type = type;
        objectFactory = (type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
    }


//3.ExtensionLoader#getExtensionLoader 再次走到这个方法 入参变成了 ExtensionFactory.class
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) { 
		    //org.apache.dubbo.common.extension.ExtensionFactory
        ExtensionLoader<T> loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        if (loader == null) {
          	//ExtensionFactory也继续走这里
            EXTENSION_LOADERS.putIfAbsent(type, new ExtensionLoader<T>(type));
            loader = (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
        }
        return loader;
    }

//4.ExtensionLoader#ExtensionLoader
    private ExtensionLoader(Class<?> type) { //ExtensionFactory.class
        this.type = type;
      	//boolean条件判断为true返回null，返回了一个objectFactory为null的 ExtensionLoader
        objectFactory = (type == ExtensionFactory.class ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getAdaptiveExtension());
    }


//5.接下来走步骤2中的getAdaptiveExtension方法 调用的对象是 ExtensionFactory类型的ExtensionLoader
//ExtensionLoader#getAdaptiveExtension 获取
    public T getAdaptiveExtension() {
        Object instance = cachedAdaptiveInstance.get();
        if (instance == null) {
            if (createAdaptiveInstanceError != null) {
                throw new IllegalStateException("Failed to create adaptive instance: " +
                        createAdaptiveInstanceError.toString(),
                        createAdaptiveInstanceError);
            }

            synchronized (cachedAdaptiveInstance) {
                instance = cachedAdaptiveInstance.get();
                if (instance == null) {
                    try {
                     		// 创建自适应的扩展
                        instance = createAdaptiveExtension();
                        cachedAdaptiveInstance.set(instance);
                    } catch (Throwable t) {
                        createAdaptiveInstanceError = t;
                        throw new IllegalStateException("Failed to create adaptive instance: " + t.toString(), t);
                    }
                }
            }
        }

        return (T) instance;
    }

    private T createAdaptiveExtension() {
        try {
						//getAdaptiveExtensionClass方法获结果是 AdaptiveExtensionFactory
            return injectExtension((T) getAdaptiveExtensionClass().newInstance());
        } catch (Exception e) {
            throw new IllegalStateException("Can't create adaptive extension " + type + ", cause: " + e.getMessage(), e);
        }
    }

   private Class<?> getAdaptiveExtensionClass() {
	     //加载扩展类
        getExtensionClasses();
        if (cachedAdaptiveClass != null) {
            return cachedAdaptiveClass;
        }
        return cachedAdaptiveClass = createAdaptiveExtensionClass();
    }

		//加载扩展类 step1
    private Map<String, Class<?>> getExtensionClasses() {
        Map<String, Class<?>> classes = cachedClasses.get();
        if (classes == null) {
            synchronized (cachedClasses) {
                classes = cachedClasses.get();
                if (classes == null) {
                    classes = loadExtensionClasses();
                    cachedClasses.set(classes);
                }
            }
        }
        return classes;
    }

		//加载扩展类 step2，同步加载当前ExtensionLoader类对应的扩展类文件
    private Map<String, Class<?>> loadExtensionClasses() {
        cacheDefaultExtensionName();

        Map<String, Class<?>> extensionClasses = new HashMap<>();
				
      //加载策略在类创建的时候已初始化
        for (LoadingStrategy strategy : strategies) {
          //ExtensionLoader#loadDirectory  type是当前扩展类加载器的类型
            loadDirectory(extensionClasses, strategy.directory(), type.getName(), strategy.preferExtensionClassLoader(),strategy.overridden(), strategy.excludedPackages());
            loadDirectory(extensionClasses, strategy.directory(), type.getName().replace("org.apache", "com.alibaba"),strategy.preferExtensionClassLoader(), strategy.overridden(), strategy.excludedPackages());
        }
        return extensionClasses;
    }


//加载扩展类 step3，加载结果保存到了extensionClasses这个map当中 
private void loadDirectory(Map<String, Class<?>> extensionClasses, String dir, String type,
                               boolean extensionLoaderClassLoaderFirst, boolean overridden, String... excludedPackages) {
		        String fileName = dir + type;
            ClassLoader classLoader = findClassLoader();
            ...
            urls = classLoader.getResources(fileName);
            ...
            loadResource(extensionClasses, classLoader, resourceURL, overridden, excludedPackages);
    }

//org.apache.dubbo.common.extension.ExtensionFactory 	spi
spring=org.apache.dubbo.config.spring.extension.SpringExtensionFactory
adaptive=org.apache.dubbo.common.extension.factory.AdaptiveExtensionFactory
spi=org.apache.dubbo.common.extension.factory.SpiExtensionFactory

```

### ExtensionLoader的加载策略

在创建 dubbo 的 ExtensionLoader时，在初始化属性 strategies 的时候会通过 JDK 的SPI加载 加载策略，这些策略定义了dubbo SPI 的扫描目录和策略等

```java
public class ExtensionLoader<T> {
  ...
	private static volatile LoadingStrategy[] strategies = loadLoadingStrategies();
  
    private static LoadingStrategy[] loadLoadingStrategies() {
        return stream(load(LoadingStrategy.class).spliterator(), false)
                .sorted()
                .toArray(LoadingStrategy[]::new);
    }
}

//java.util.ServiceLoader#load(java.lang.Class<S>)
 public static <S> ServiceLoader<S> load(Class<S> service) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        return ServiceLoader.load(service, cl);
    }

org/apache/dubbo/dubbo/2.7.18.1/dubbo-2.7.18.1.jar!/META-INF/services
文件名：/org.apache.dubbo.common.extension.LoadingStrategy
内容：	
org.apache.dubbo.common.extension.DubboInternalLoadingStrategy
org.apache.dubbo.common.extension.DubboLoadingStrategy
org.apache.dubbo.common.extension.ServicesLoadingStrategy	
  
这三个策略对应的目录如下，优先级也是从高到低  
META-INF/dubbo/internal/
META-INF/dubbo/
META-INF/services/
```



```java
//org.apache.dubbo.common.context.FrameworkExt SPI
config=org.apache.dubbo.config.context.ConfigManager
environment=org.apache.dubbo.common.config.Environment
repository=org.apache.dubbo.rpc.model.ServiceRepository
```

## SpringExtensionFactory

SpringExtensionFactory的作用在于dubbo的SPI机制中的依赖注入，当有扩展类需要注入其他bean的时候，可能会从SpringExtensionFactory这个类中通过applicationContext获取Bean对象。

## ServiceClassPostProcessor

处理 @DubboServie 标记类，包装成 ServiceBean 存入beanDefination 中。



## [dubbo SPI @Adaptive注解使用方法与原理解析 简单易懂](https://blog.csdn.net/qq_41960425/article/details/109348241?spm=1001.2101.3001.6650.3&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-3-109348241-blog-120812534.pc_relevant_default&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-3-109348241-blog-120812534.pc_relevant_default&utm_relevant_index=4)
