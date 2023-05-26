

本文讲解 spring 容器刷新全流程，基于 spring.boot 2.0.3版本 & spring-core 5.0.7 版本，为了更直观的理解，会对部分代码进行删减。

# 容器刷新总流程

```java
//org.springframework.context.support.AbstractApplicationContext#refresh
public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			// 一：Prepare this context for refreshing.
			prepareRefresh();

			// 二：Tell the subclass to refresh the internal bean factory.
			ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

			// 三：Prepare the bean factory for use in this context.
			prepareBeanFactory(beanFactory);

			try {
				// 四：Allows post-processing of the bean factory in context subclasses.
				postProcessBeanFactory(beanFactory);

				// 五：Invoke factory processors registered as beans in the context.
        // 调用工厂处理器，将bean注册到容器中
				invokeBeanFactoryPostProcessors(beanFactory);

				// 六：Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				// 七：Initialize message source for this context.
				initMessageSource();

				// 八：Initialize event multicaster for this context.
				initApplicationEventMulticaster();

				// 九：Initialize other special beans in specific context subclasses.
				onRefresh();

				// 十：Check for listener beans and register them.
				registerListeners();

				// 十一：Instantiate all remaining (non-lazy-init) singletons.
        // 完成Bean工厂初始化，实例化所有剩余的（非懒加载的）单例对象
				finishBeanFactoryInitialization(beanFactory);

				// 十二：Last step: publish corresponding event.
				finishRefresh();
			}

			catch (BeansException ex) {
				if (logger.isWarnEnabled()) {
					logger.warn("Exception encountered during context initialization - " +
							"cancelling refresh attempt: " + ex);
				}

				// Destroy already created singletons to avoid dangling resources.
				destroyBeans();

				// Reset 'active' flag.
				cancelRefresh(ex);

				// Propagate exception to caller.
				throw ex;
			}

			finally {
				// Reset common introspection caches in Spring's core, since we
				// might not ever need metadata for singleton beans anymore...
				resetCommonCaches();
			}
		}
	}
```



# 五、invokeBeanFactoryPostProcessors

调用工厂处理器，将bean注册到容器中

```java
//org.springframework.context.support.AbstractApplicationContext#invokeBeanFactoryPostProcessors
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
  	
		PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

		// Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
		// (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
		if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}
	}
```



# 十一、finishBeanFactoryInitialization

实例化所有剩余的（非懒加载的）单例对象 来完成Bean工厂初始化。

```java
//org.springframework.context.support.AbstractApplicationContext#finishBeanFactoryInitialization
protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
		// Initialize conversion service for this context.
		if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
				beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
			beanFactory.setConversionService(
					beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
		}

		// Register a default embedded value resolver if no bean post-processor
		// (such as a PropertyPlaceholderConfigurer bean) registered any before:
		// at this point, primarily for resolution in annotation attribute values.
		if (!beanFactory.hasEmbeddedValueResolver()) {
			beanFactory.addEmbeddedValueResolver(strVal -> getEnvironment().resolvePlaceholders(strVal));
		}

		// Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
		String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
		for (String weaverAwareName : weaverAwareNames) {
			getBean(weaverAwareName);
		}

		// Stop using the temporary ClassLoader for type matching.
		beanFactory.setTempClassLoader(null);

		// Allow for caching all bean definition metadata, not expecting further changes.
		beanFactory.freezeConfiguration();

		// Instantiate all remaining (non-lazy-init) singletons.
  	// 实例化所有剩余的（非懒加载的）单例对象
		beanFactory.preInstantiateSingletons();
	}  
```

```java
//org.springframework.beans.factory.support.DefaultListableBeanFactory#preInstantiateSingletons
public void preInstantiateSingletons() throws BeansException {
 		// 这里copy了 beanDefinitionNames 的副本来进行后续遍历，因为在bean的实例化过程中可能会去注册新的 beanDefinition
  	// 而在迭代器遍历时，添加元素会报 ConcurrentModificationException 异常
		List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
  	// beanDeinitionNames是通过 ConfigurationClassPostProcessor 扫描配置文件或注解等方式，将我们定义的bean包装为beanDefinition，然后注册到 BeanDefinitionRegistry，这才有的 beanDefinitionNames 列表

  	// 创建bean的先后取决于它在 beanDefinitionNames 中的顺序
		for (String beanName : beanNames) {
        RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
        if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
            if (isFactoryBean(beanName)) {
                  Object bean = getBean(FACTORY_BEAN_PREFIX + beanName);
                  final FactoryBean<?> factory = (FactoryBean<?>) bean;
                  boolean isEagerInit;
                  if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
                    isEagerInit = AccessController.doPrivileged((PrivilegedAction<Boolean>)
                            ((SmartFactoryBean<?>) factory)::isEagerInit,
                        getAccessControlContext());
                  }
                  else {
                    isEagerInit = (factory instanceof SmartFactoryBean &&
                        ((SmartFactoryBean<?>) factory).isEagerInit());
                  }
                  if (isEagerInit) {
                    getBean(beanName);
                  }
            }
            else {
              getBean(beanName);
            }
        }
		}

		// Trigger post-initialization callback for all applicable beans...
		for (String beanName : beanNames) {
			Object singletonInstance = getSingleton(beanName);
			if (singletonInstance instanceof SmartInitializingSingleton) {
				final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
				if (System.getSecurityManager() != null) {
					AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
						smartSingleton.afterSingletonsInstantiated();
						return null;
					}, getAccessControlContext());
				}
				else {
					smartSingleton.afterSingletonsInstantiated();
				}
			}
		}
	}
```

