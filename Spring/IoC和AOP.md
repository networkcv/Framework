# IoC 和 DI

## 控制反转 IoC

IoC Inversion of Control (控制反转/反转控制)，简单来说它是一种技术思想，描述的是对象创建和管理如何解耦。

**为什么叫做控制反转**

控制：指的是对象创建（实例化、管理）的权利 

反转：控制权交给外部环境了（spring框架、IoC容器）

**IoC思想下开发⽅式**

我们不⽤⾃⼰去new对象了，⽽是由IoC容器（Spring框架）去帮助我们实例化对象并且管理它，我们需要使⽤哪个对象，去问IoC容器要即可。

IoC容器不仅仅负责创建对象实例，而是接管了对象对完整生命周期，包括构造这些对象时需要注入哪些其他对象，也包括该对象会被注入到其他对象中，这些对象的销毁也是由 Spring 来管理的。

## 依赖注入 DI

DI：Dependancy Injection（依赖注⼊），DI是IoC的一种实现方式。

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

## 控制反转和依赖注入的关系

IoC 和 DI 并不等同。控制反转是一种思想，依赖注入是一种实现方式，IoC还有其他的实现方式，如ServiceLocator。

## Spring如何完成依赖注入

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

## 依赖注入的多种实现方式

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

# AOP

AOP: Aspect oriented Programming ⾯向切⾯编程/⾯向⽅⾯编程

OOP三⼤特征：封装、继承和多态

OOP是纵向的继承关系，将子类公共代码抽取到父类，是纵向代码的抽取。而AOP是解决横向代码抽取的问题。

## **AOP在解决什么问题**

在不改变原有业务逻辑情况下，增强横切逻辑代码，根本上解耦合，避免横切逻辑代码重复

## **为什么叫做⾯向切⾯编程**

「切」：指的是横切逻辑，原有业务逻辑代码我们不能动，只能操作横切逻辑代码，所以⾯向横切逻辑

「⾯」：横切逻辑代码往往要影响的是很多个⽅法，每⼀个⽅法都如同⼀个点，多个点构成⾯，有⼀个 ⾯的概念在⾥⾯
