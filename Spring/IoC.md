https://www.jianshu.com/p/07af9dbbbc4b

https://www.cnblogs.com/xdp-gacl/p/4249939.html

https://zhuanlan.zhihu.com/p/70071870

```java
public class Human {
    ...
    Father father;
    ...
    public Human() {
        father = new Father();
    }
}
```

Human 类中用到了 Father 类的对象，我们就可以说 Human 类依赖 Father 类。

仔细看这段代码我们会发现存在一些问题：

1. 如果现在要改变 father 生成方式，如需要用new Father(String name)初始化 father，需要修改 Human 代码；
2. 如果想测试不同 Father 对象对 Human 的影响很困难，因为 father 的初始化被写死在了 Human 的构造函数中；
3. 如果new Father()过程非常缓慢，单测时我们希望用已经初始化好的 father 对象 Mock 掉这个过程也很困难。

```java
public class Human {
    ...
    Father father;
    ...
    public Human(Father father) {
        this.father = father;
    }
}
```

上面代码中，我们将 father 对象作为构造函数的一个参数传入。在调用 Human 的构造方法之前外部就已经初始化好了 Father 对象。像这种非自己主动初始化依赖，而通过外部来传入依赖的方式，我们就称为依赖注入。
 现在我们发现上面 1 中存在的两个问题都很好解决了，简单的说依赖注入主要有两个好处：

1. 解耦，将依赖之间解耦。
2. 因为已经解耦，所以方便做单元测试，尤其是 Mock 测试。

###### 控制反转和依赖注入的关系

我们已经分别解释了控制反转和依赖注入的概念。有些人会把控制反转和依赖注入等同，但实际上它们有着本质上的不同。

- **控制反转**是一种思想
- **依赖注入**是一种设计模式

### 小结

IoC框架使用依赖注入作为实现控制反转的方式，但是控制反转还有其他的实现方式，例如说[ServiceLocator](https://link.jianshu.com?t=http://martinfowler.com/articles/injection.html#UsingAServiceLocator)，所以不能将控制反转和依赖注入等同。

IoC 控制反转 ，控制是指对象的创建，反转是指依赖对象的获取方式。传统应用程序中我们在对象中主动控制创建获取依赖对象，而IoC则是由容器来帮忙创建及注入依赖对象。

为何是反转？因为由容器帮我们查找及注入依赖对象，对象只是被动的接受依赖对象。

IoC 是依赖倒置原则的一种代码设计的思路

Spring IoC 就是 Spring 的 IoC 容器接管了 Spring 中对象的生命周期，包括构造这些对象时需要注入哪些其他对象，也包括该对象会被注入到其他对象中，这些对象的销毁也是由 Spring 来管理的。

Spring IoC 不止帮我们做了这些，我们在 Spring 的配置文件定义这些依赖关系。

```java
<beans>
    <bean id="car" class="spring.tutorial.Car">
        <property name="framework">
            <ref local="framework1"/>
        </property>
    </bean>
    <bean id="framework1" class="spring.tutorial.Framework">
        <property name="name">
            <value>my framkework</value>
        </property>
    </bean>
</beans>
```

spring 会在程序启动时，先去读取这些配置文件，然后进行解析，它会首先创建那些没有依赖其他类的对象，然后再通过反射，根据用户的配置的依赖注入方式，将这些对象（framework）注入依赖它们的对象中。

再后来，由于配置xml配置文件写起来不够方便，而且有时候想知道哪个类被注入了还需要查看配置文件，

Spring 加入了注解配置，启动时进行注解扫描，将类上标有@Bean @Component @Controller @Services @Repstory @Configuration 等注解的对象都会由 Spring 来创建对象。 而我们常用的 成员变量的注解 @Autowired 会自动注入，这个自动也是有一定能要求的，它会根据类型（默认）和名称去 Spring 容器中挑选合适的 Bean 注入，如果可选的 Bean 有多个或者一个都没有就会报错。

@Quilify 会指定具体名称的Bean进行注入

@Primary 这个注解是使用在 注入类上的，表示在没有指定具体类的情况下，该类会被优先注入。

以上这些注解都是 Spring 提供的，所以需要搭配 Spring 容器使用。

@Resource  这个是由 J2EE 提供的注解，默认通过 name 属性匹配，找不到再按 type 匹配。功能不如 @Autowired 灵活和强大。

@inject 这个注解是第三方jar包提供的注入注解，适合非Spring项目使用。

### 依赖注入的多种实现方式

- 构造器注入

  ```java
  class Car{
  	private Framework framework;
      Car(Framework framework){
          this.framework=framework;
      }
      ...
  }
  class Framework{
      ...
  }
  ```

  ```java
  class Client{
  	public static void main(String [] args){
  		Framework framework = new Framework();
      	Car car = new Car(framework);    
      }
  }
  ```

- Setter注入

  ```java
  class Car{
  	private Framework framework;
  	public void setFramework(Framework framework){
          this.framework=framework;
      }
      ...
  }
  class Framework{
      ...
  }
  ```

  ```java
  class Client{
  	public static void main(String [] args){
  		Framework framework = new Framework();
      	Car car = new Car();    
          car.setFramework(framkework);
      }
  }
  ```

- 接口注入

  ```java
  interface Aware{
      void aware(Framework framework);
  }
  class Car implements Aware{
  	private Framework framework;
      
      @overwrite
  	public void aware(Framework framework){
          this.framework=framework;
      }
      ...
  }
  class Framework{
      ...
  }
  ```

  ```java
  class Client{
  	public static void main(String [] args){
  		Framework framework = new Framework();
      	Car car = new Car();    
          car.aware(framkework);
      }
  }
  ```

从以上三个例子可以看出，依赖注入的本质是通过调用被注入类（Car）的方法来将需要进行注入类（Framework）作为参数传入，我们当然也可以直接将成员变量声明为 public，不过这样就违背了面向对象封装的特性了。





