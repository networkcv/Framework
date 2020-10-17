## 1.Lambda表达式

函数式风格的编程，让代码更加的简洁、高效。

### 1.1 Lambda的基础语法

- 举例：（o1，o2）`->` Integer.compare(o1,o2);
- 格式：
  - `->` lambda操作符
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



### 1.2 Java 内置的四大函数式接口

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

  



### 1.3 方法引用

使用场景：当要传递给Lambda体的操作，已经有实现的方法时，可以使用方法引用！

方法引用可以看作是Lambda表达式的深层次表达，方法引用也可以看作是函数式接口的是一个实例 。

格式：使用操作符`::` 将类（或对象）与方法名分割开来。

- 情况一	对象：：实例方法名
- 情况二	类：：静态方法名
- 情况三	类：：实例方法名

要求：

- 实现接口的抽象方法的参数列表和返回值类型，必须与方法引用的方法参数列表和返回值类型保持一致（针对情况一和情况二）
- 情况三有些特殊，返回值类型必须相同，但参数个数可以相差一个。

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
        //情况一
        Consumer<String> consumer1 = System.out::println;
        consumer1.accept("b");
        //情况一
        Consumer<Integer> consumer2 = new LambdaTest3()::getInteger;
        consumer2.accept(1);
        //情况二
        Consumer<Integer> consumer3 = LambdaTest3::printInteger;
        consumer3.accept(2);
        //情况三
        Comparator<String> comparator0 = new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        };
        Comparator<String> comparator = (s1, s2) -> s1.compareTo(s2);
        Comparator<String> comparator2 = String::compareTo;
        System.out.println(comparator2.compare("1", "2"));

        Function<Object, String> function0 = new Function<Object, String>() {
            public String apply(Object o) {
                return o.toString();
            }
        };
        Function<Object, String> function = o -> o.toString();
        Function<Object, String> function2 = Objects::toString;
        System.out.println(function2.apply("123"));
    }
}
```



### 1.4 构造器引用

和方法引用类似，函数式接口的抽象方法的形参列表和构造器的形参列表一致，抽象方法的返回值类型即为构造器所属类的类型。

```java
    static class Student {
        private String name;
        private Integer age;

        public Student() {
            System.out.println(" Student() ");
        }

        public Student(String name) {
            System.out.println(" Student(String) ");
            this.name = name;
        }

        public Student(String name, Integer age) {
            System.out.println(" Student(String，Integer) ");
            this.name = name;
            this.age = age;
        }
    }

    @Test
    public void test() {
        //无参 有返回值 和Supplier 中的T get()方法类似
        Supplier<Student> supplier = () -> new Student();
        Supplier<Student> supplier2 = Student::new;
        supplier.get();
        supplier2.get();
        //有一个参数 有返回值，和Function 中的T apply(S) 方法类似
        Function<String, Student> function = s -> new Student(s);
        Function<String, Student> function2 = Student::new;
        function.apply("tom");
        function2.apply("tom");
        //有两个参数 有返回值，和BiFunction 中的
        BiFunction<String, Integer, Student> biFunction = (s, i) -> new Student(s, i);
        BiFunction<String, Integer, Student> biFunction2 = Student::new;
        biFunction.apply("tom", 1);
        biFunction2.apply("tom", 1);
    }
```



### 1.5 数组引用

可以把数组类型看作一个特殊的类，写法与构造器引用类似

```java
    public void test() {
        //由于构建数组时，必须指定数组长度
        Function<Integer, String[]> function = integer -> new String[integer];
        Function<Integer, String[]> function2 = String[]::new;
    }
```



## 2.Stream  API

Collection是用存放数据的，Stream是用来对数据进行操作。

Stream  API 定义在 java.util.steam，Stream是Java8 中处理集合的关键抽象概念，它可以指定你希望对集合进行的操作，可以执行非常复杂的查找、过滤和映射数据等操作。使用Stream API对集合数据进行操作，就类似于使用SQL执行的数据库查询。也可以使用Stream API来并行执行操作。简而言之，Stream API提供了一种高效且易于使用的处理数据的方式。

关系型数据库中的数据可以通过SQL语句上的技巧来过滤相关数据，而NoSQL数据库则需要先将数据取出来，在Java层面去处理。

Stream和Collection的区别：Collection 是一种静态的内存数据结构，而Stream是有关计算的，需要通过CPU计算来实现。

- Stream 自身是不会存储数据的。和迭代器类似。
- Stream 不会改变源对象，他们会返回一个持有结果的新Stream。
- Stream 延迟执行的，在执行终止操作的时候，才会执行前边中间操作链，并产生结果，之后不会再被使用。

Stream 执行流程：

1. Stream 的实例化
2. 一系列的中间操作链（过滤、映射。。）
3. 终止操作

### 2.1 Stream 的实例化

#### 2.1.1 通过集合创建

```java
public interface Collection<E> extends Iterable<E> {
	default Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }
    default Stream<E> parallelStream() {
        return StreamSupport.stream(spliterator(), true);
    }
}
```

#### 2.1.2 通过数组创建

```java
//调用Arrays的 public static Stream<T> stream(T[] array)  返回一个流
IntStream stream = Arrays.stream(new int[]{1, 2, 3});
Stream<String> stream1 = Arrays.stream(new String[]{"1", "2"});
```

#### 2.1.3 通过Stream的 of()方法

```java
Stream<Integer> integerStream = Stream.of(1, 2, 3);
```

#### 2.1.4 创建无限流

```java
//遍历前10个偶数
//迭代
//public static<T> Stream<T> iterate(final T seed,final UnaryOperator<T> f)
//遍历前10个偶数
Stream.iterate(0,t->t+2).limit(10).forEach(System.out::println);

//生成
//public static<T> Stream<T> generate(Supplier<T> s)
Stream.generate(Math::random).limit(10).forEach(System.out::println);
```



### 2.2 Stream的中间操作

#### 2.2.1 筛选与切片

```java
    @Test
    public void test() {
        // filter(Predicate p) 从流中排除某些元素，排除的规则为Predicate类型的函数，过滤出Predicate函数中的元素
        Stream<String> stream = Stream.of("1", "2", "3", "4");
        stream.filter(s -> s.equals("1")).forEach(System.out::println);

        // limit(n) 截断流，使其元素不超过给定数量
        Stream<String> stream2 = Stream.of("1", "2", "3", "4");
        stream2.limit(3).forEach(s -> System.out.print(s + " "));
        System.out.println();

        //skip(n) 跳过元素，返回一个扔掉前n个元素的流，元素不足的话，会返回空流
        Stream<String> stream3 = Stream.of("1", "2", "3", "4");
        stream3.skip(3).forEach(System.out::println);

        //distinct() 筛选去重，通过流所产生元素的hashCode()和equals()去除重复元素
        ArrayList<Student> students = new ArrayList<>();
        BiFunction<String, Integer, Student> biFunction = Student::new;
        students.add(biFunction.apply("tom", 18));
        students.add(biFunction.apply("jack", 19));
        students.add(biFunction.apply("ros", 20));
        students.add(biFunction.apply("tom", 20));
        students.add(biFunction.apply("jack", 19));
        students.stream().forEach(System.out::println);
        System.out.println("去重后");
        students.stream().distinct().forEach(System.out::println);
    }
```



#### 2.2.2 映射

```java
    @Test
    public void test2() {
        //map(Function f) 接收一个函数作为参数，作为映射规则，将元素转换成其他形式或提取信息，
        //该函数会被应用到每个元素上，并将其映射成一个新的元素。
        ArrayList<Student> students = new ArrayList<>();
        BiFunction<String, Integer, Student> biFunction = Student::new;
        students.add(biFunction.apply("tom", 18));
        students.add(biFunction.apply("jack", 19));
        students.add(biFunction.apply("ros", 20));
        students.add(biFunction.apply("tom", 20));
        students.add(biFunction.apply("jack", 19));
        //筛选出名字长度大于3的姓名
        students.stream().map(Student::getName).filter(s->s.length()>3).forEach(System.out::println);
    }
//Output jack jack 
```

```java
    public void test3() {
        //flatMap(Function f) 接收一个函数作为参数，将流中的每个值都换成另一个流，然后把流合并
        Stream<String> stream = Stream.of("12", "34", "56", "78");
        stream.flatMap(StreamTest2::getCharacter).forEach(System.out::println);
    }

    public static Stream<Character> getCharacter(String s) {
        ArrayList<Character> characters = new ArrayList<>();
        for (Character c : s.toCharArray()) {
            characters.add(c);
        }
        return characters.stream();
    }
//Output 1  2  3  4  5  6  7  8 
```

#### 2.2.3 排序 

```java
    @Test
    public void test4() {
        Stream<Integer> integerStream = Stream.of(321, 54, 35, 43, 6, 546, 24, 132, 2, 21);
        integerStream.sorted().forEach(s -> System.out.print(s + " "));
        System.out.println();
        //Output 2 6 21 24 35 43 54 132 321 546 

        students.stream().sorted((s1, s2) -> Integer.compare(s1.getAge(), s2.getAge())).forEach(System.out::println);
        students.stream().sorted(Comparator.comparingInt(Student::getAge)).forEach(System.out::println);
        students.stream().sorted((s1, s2) -> {
            return s1.getAge() < s2.getAge() ? -1 : 0;
        }).forEach(System.out::println);
        //Output 按照Student年龄的从小到大顺序打印输出
    }
```

### 2.3 终止操作

#### 2.3.1 匹配与查找

```java   
    public void test() {
//        allMatch(Predicate p) 检查是否匹配所有元素
//        anyMatch(Predicate p) 检查是否至少匹配一个元素
//        noneMatch(Predicate p) 检查是否没有匹配的元素

//        findFirst() 返回第一个元素
//        findAny()   返回任意元素

//        count()     返回元素中的总个数
//        Max(Comparator c)   返回流中最大元素
//        Min(Comparator c)   返回流中最小元素
//        forEach(Consumer c)   使用集合的遍历操作
    }
```

#### 2.3.2 归约

```java
    @Test
    public void test1() {
        //执行终止操作后，流就关闭了，无法再进行其他中间操作，
        Stream<Integer> limit1 = Stream.iterate(1, o -> o + 1).limit(10);
        Stream<Integer> limit2 = Stream.iterate(1, o -> o + 1).limit(10);
//        reduce(BinaryOperator accumulator)
//        reduce(T identity, BinaryOperator accumulator) 将流中的元素反复结合起来，得到一个值
//        identity 初始的值

        BinaryOperator<Integer> binaryOperator = (i1, i2) -> i1 + i2;
        Integer sum = limit1.reduce(0, binaryOperator);
        Integer sum1 = limit2.reduce(0, Integer::sum);

        System.out.println(sum.equals(sum1));
    }
```

#### 2.3.3 收集

```java
    @Test
    public void test3() {
        //collect(Collector c) 将流转换为其他形式，接收一个Collector接口的实现，
        // 用于给Stream中元素做汇总的方法
        Stream<Integer> integerStream = Stream.of(1, 2, 3, 4, 5, 6);
        List<Integer> collect = integerStream.collect(Collectors.toList());
        collect.forEach(System.out::println);
    }
```

## 3.Optional类

解决NullPointException的问题。

### 3.1 创建Optional类对象的方法

- Optional.of(T t)	创建一个包装 t 的实例，t不能为null。

- Optional.empty()	创建一个空的Optional实例。

- Optional.ofNullable(T t)	创建一个包装 t 的实例，t可以为null。

### 3.2 判断Optional容器中是否包含对象

- boolean isPresent() 判断是否包含对象。
- void ifPresent(Consumer <? super T> consumer ) 如果有值，就执行Consumer接口的实现代码，并且该值会作为参数传给它。

### 3.3  获取Optional容器的对象

- T get()	返回对象包含的值，没有则抛出异常。

- orElse(T other)	如果之前的Optional实例中封装的value是非空，则返回内部的value，如果内部的value为空，则返回方法参数中的other。