## Lambda表达式

### Lambda的基础语法

- 举例：（o1，o2）->	Integer.compare(o1,o2);
- 格式：
  - ​	 `->` lambda操作符
  - 左边：lambda形参列表，其实就是接口中的抽象方法的形参列表
  - 右边：lambda体，重写抽象方法的方法体
- Lambda表达式本质：作为一个函数式接口（@FunctionalInterface）的实例

- 语法格式一：无参，无返回值

```java
	public void test()  {
        Runnable runnable0 = new Runnable() {
            @Override
            public void run() {
                System.out.println(0);
            }
        };
        runnable0.run();
        Runnable runnable1 = () -> {
            System.out.println(2);
        };
        runnable1.run();
    }
```

- 语法格式二：Lambda 需要一个参数，但没有返回值

```java
    public void test2() {
        Consumer<String> consumer1 =new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println(s);
            }
        };
        consumer1.accept("consumer1");
        
        Consumer<String> consumer2 = (String s) -> System.out.println(s);
        consumer2.accept("consumer2");
    }
```

- 语法格式三：使用泛型的话，数据类型可以省略，由编译器可以推断出

```java
    public void test2() {
        Consumer<String> consumer2 = (s) -> System.out.println(s);
        consumer2.accept("consumer2");
    }
```

- 语法格式四：Lambda 若只有一个参数时，参数的小括号也可以省略

```java
    public void test2() {
        Consumer<String> consumer2 = s-> System.out.println(s);
        consumer2.accept("consumer2");
    }
```

- 语法格式五：Lambda 需要两个以上的参数，多条执行语句，并且可以有返回值

```java
	public void test1() {
        Comparator<Integer> comparator0 = new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(o1, o2);
            }
        };
        System.out.println(comparator0.compare(0,1));
        
        Comparator<Integer> comparator1=(o1, o2) -> {
            return Integer.compare(o1,o2);
        };
        System.out.println(comparator1.compare(0,1));
    }
```

- 语法格式六：当Lambda 体只有一条语句时，return 与大括号若有，都可以省略

```java
	public void test1() {
        Comparator<Integer> comparator1=(o1, o2) -> Integer.compare(o1,o2);
        System.out.println(comparator1.compare(0,1));
    }
```



### Java 内置的四大函数式接口

- 消费型接口 Consumer<T>	void accept(T t)

  ```java
      public void consumer(Integer integer, Consumer<Integer> consumer) {
          consumer.accept(integer);
      }
  
      @Test
      public void test() {
          consumer(100, s -> System.out.println("花费"+s));
      }
  // Output 花费100
  ```

  

- 供给型接口 Supplier<T> T get()

  ```java
      //供给型接口 Supplier<T> T get()
      public Integer getTime(Supplier<Integer> suppliers) {
          return suppliers.get();
      }
  
      @Test
      public void test2() {
          System.out.println(getTime(()->2));
  
          Supplier<String> sp = () -> "1";
          System.out.println(sp.get());
      }
  ```

  

- 函数型接口 Function<T,R> R apply(T t)

- 断定型接口 Predicate<T> boolean test(T t)

  ```java
      public List<String> filterString(List<String> list, Predicate<String> predicate){
          List<String> res=new ArrayList<>();
          for (String s : list) {
              if (predicate.test(s)){
                  res.add(s);
              }
          }
          return res;
      }
      @Test
      public void test2(){
          List<String> list= Arrays.asList("1","2","3","11");
          List<String> res = filterString(list, s -> s.startsWith("1"));
          System.out.println(res);
      }
  //Output 	[1, 11]
  ```

  

## 方法引用和构造器引用

**方法引用**

使用场景：当要传递给Lambda体的操作，已经有实现的方法时，可以使用方法引用！

方法引用可以看作是Lambda表达式的深层次表达，方法引用也可以看作是函数式接口的是一个实例 。

格式：使用操作符`::` 将类（或对象）与方法名分割开来。

- 情况一	对象：：实例方法名
- 情况二	类：：静态方法名
- 情况三	类：：实例方法名

要求：实现接口的抽象方法的参数列表和返回值类型，必须与方法引用的方法参数列表和返回值类型保持一致（针对情况一和情况二）

```java
public class LambdaTest3 {  
    public void getInteger(Integer i) {
        System.out.println("i: " + i);
    }
    public static void printInteger(Integer i) {
        System.out.println("i: " + i);
    }

    @Test
    public void test() {
        Consumer<String> consumer = (s) -> System.out.println(s);
        consumer.accept("a");
        Consumer<String> consumer1 = System.out::println;
        consumer1.accept("b");
        Consumer<Integer> consumer2 = new LambdaTest3()::getInteger;
        consumer2.accept(1);
        Consumer<Integer> consumer3 = LambdaTest3::printInteger;
        consumer2.accept(2);
    }
}
```

**构造器引用**