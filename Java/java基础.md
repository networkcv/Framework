异常： 
    http://www.importnew.com/26613.html
    Throwable 
       -- Error 错误    用来指示运行时环境发生的错误
       --Exception 异常
            --IOException
            --RuntimeException  
总体上我们根据Javac对异常的处理要求，将异常类分为2类。
    非检查异常（unckecked exception）：Error 和 RuntimeException 以及他们的子类。javac在编译时，不会提示和发现这样的异常，
    不要求在程序处理这些异常。所以如果愿意，我们可以编写代码处理（使用try…catch…finally）这样的异常，也可以不处理。对于这些异常，
    我们应该修正代码，而不是去通过异常处理器处理 。这样的异常发生的原因多半是代码写的有问题。如除0错误ArithmeticException，
    错误的强制类型转换错误ClassCastException，数组索引越界ArrayIndexOutOfBoundsException，使用了空对象NullPointerException等等。

    检查异常（checked exception）：除了Error 和 RuntimeException的其它异常。javac强制要求程序员为这样的异常做预备处理工作（使用
    try…catch…finally或者throws）。    在方法中要么用try-catch语句捕获它并处理，要么用throws子句声明抛出它，否则编译不会通过。
    这样的异常一般是由程序的运行环境导致的。因为程序可能被运行在各种未知的环境下，而程序员无法干预用户如何使用他编写的程序，
    于是程序员就应该为这样的异常时刻准备着。如SQLException , IOException,ClassNotFoundException 等。



this指的是当前对象
    可以用来解决形参和成员变量同名的问题，还用在构造方法中调用其他构造方法，但是不能用在静态方法中
    成员方法可以直接调用类中其他的成员方法，test1() <=> this.test1() ,平时都
super是super所在类直接父类对象的引用，而不是当前对象的直接父类
    可以通过super.来访问父类中被子类覆盖的方法或属性，还可以用来调用父类构造方法,例如super(10) 调用父类有参构造

static修饰的成员变量和方法，从属于类，普通变量和方法从属于对象。    

静态初始化块，构造方法是用于初始化对象，而静态初始化块是用于初始化类
    执行顺序：先执行父类的静态初始化块，再向下执行子类的静态初始化块
    构造方法执行顺序：同上

父类也叫超类，基类，衍生类；java中单继承，多实现。
子类继承父类，可以得到父类的全部属性和方法(除了构造方法)，但是无法使用父类私有的属性和方法，PS:反射可以使用父类私有属性和方法。

重写
    1.方法参数相同
    2.返回类型和声明异常类型，子类小于等于父类
    3.访问权限，子类大于等于父类

== 代表比较双方是否相同，如果是基本类型则表示值相等，如果是引用类型则表示地址相等，即是同一个对象
equals 提供定义“对象内容相等”的逻辑

多态三个必要条件
    1.继承
    2.重写
    3.父类引用指向子类对象
用该父类的引用调用子类重写的方法，多态就出现了

数组初始化
    1.静态初始化 int [] a={1,2,3};
    2.默认初始化 int [] a=new int[3]; 长度为3 每个元素按照实例变量同样的方式被隐式初始化
    3.动态初始化 int [] a=new int[2]; a[0]=0 a[1]=1

抽象类中不一定有抽象方法，但抽象方法一定在抽象类中，如果继承抽象类，则必须实现抽象类中的抽象方法
抽象类可以包含属性、方法、构造方法，但是构造方法不能用new实例，只能用来被子类调用。

接口中只有常量、抽象方法
    常量定义时 默认为 public static final
    方法定义时 只能是 public abstract
接口之间可以多继承，不同接口之间用逗号隔开

包装类的"=="运算在不遇到算术运算的情况下不会自动拆箱
包装类的equals()方法不处理数据转型



### String常用方法

​    代码的复用性很高，System Arrays 
​    replace contains  contentEquals 支持操作字符序列

      CharSequence 字符序列接口 常用实现类有String StringBuilder StringBuffer
        int length()   char charAt(int index)  public String toString();
        CharSequence subSequence(int start, int end)
    
    replace 和 replaceAll 两者都是可以全部替换 replaceAll的形参名称是regex 所以支持正则表达式 all意思应该是更全面的意思
    equals 和 contentEquals equals 只能判断字符串，contenEquals可以判断字符序列
    public boolean contentEquals(CharSequence cs) {}    判断 字符序列 内容是否相同
    
    public String replace(CharSequence target, CharSequence replacement) {  
        return Pattern.compile(target.toString(), Pattern.LITERAL).matcher(
                this).replaceAll(Matcher.quoteReplacement(replacement.toString()));
    }
    
        public boolean contains(CharSequence s) {  是否包含指定 字符序列
        return indexOf(s.toString()) > -1;
    }
    
    ～～public native String intern();  在调用”ab”.intern()方法的时候会返回”ab”，但是这个方法会首先检查字符串池中是否有”ab”这个字符串，
    如果存在则返回这个字符串的引用，否则就将这个字符串添加到字符串池中，然会返回这个字符串的引用。


    public char charAt(int index) {     返回指定索引的字符
        if ((index < 0) || (index >= value.length)) {
            throw new StringIndexOutOfBoundsException(index);
        }
        return value[index];
    }
    
    indexOf(int ch,[int fromIndex]) {}  判断字符串中是否包含 数字对应的ASCll码字符，如包含返回索引，否则返回-1
    
    public int indexOf(String str, [int fromIndex]) {    判断字符串中是否包含 指定字符，如包含返回索引，否则返回-1
        return indexOf(value, 0, value.length,
                str.value, 0, str.value.length, fromIndex);
    }
    
    public boolean isEmpty() {  判断是否为空
        return value.length == 0;
    }
    
    public static String valueOf(int i) {   将输入值转为字符串
        return Integer.toString(i);
    }
    
     public boolean startsWith(String prefix) { 判断是否以什么字符开始
        return startsWith(prefix, 0);
    }
    
    public String trim() {}     去掉前后空格
    
    public String[] split(String regex, [int limit]) {}   按regex分割成String数组，limit默认为0，是分成几块
    
    public String substring(int beginIndex, [int endIndex]) {}  分割字符串包左不包右 
    
    public String concat(String str) {}     向当前字符串对象末尾追加str
    
    public char[] toCharArray() {}      变成字符数组   
    
    public void getChars(int srcBegin, int srcEnd, char dst[], int dstBegin) {}
    从当前字符数组的 srcBegin 开始到 srcEnd （包左不包右）保存到 dst 字符数组中，从 dstBegin 开始存放
    底层调用System.arraycopy(value, srcBegin, dst, dstBegin, length: srcEnd - srcBegin);

### StirngBuileder动态扩容 

​    StringBuilder.append("a")  AbstaractStringBuilder中有 字符数组value int类型的计数器count 用来统计value的使用情况
​    字符数组长度默认为16，在添加前计算要添加到str的长度，然后ensureCapacityInternal(minimumCapacity) 确保容量的方法，
​    会将count计数器和str长度相加作为追加后的长度传入minimumCapacity，然后由minimumCapacity和当前数组长度进行比较，
​    如果追加后的长度>当前数组长度就调用扩容方法  expandCapacity(minimumCapacity) ，扩容的方式是 int newCapacity=value.length * 2 + 2;
​    如果新定义的newCapacity(新容量) 比 minimumCapacity(追加后长度)小，则newCapacity=minimumCapacity，
​    然后value=Arrays.copyOf(value, newCapacity); 对字符数组进行动态扩充，先建一个新数组，然后进行数据迁移
​    进行以上操作后，此时的value进行完成扩容，str.getChars(0, len, value, count);  进行追加，然后将count计数器更新。
​    完成后返回当前对象。

StringBuilder    public AbstractStringBuilder reverse() {}  字符串反转

Arrays public static String toString(Object[] a) {} 打印一维数组

### 包装类

​    public static int parseInt(String s){}   将字符串转为int
​    public static Integer valueOf(String s) throws NumberFormatException {}    将字符串转为Integer
​     public int intValue() {}   将当前Integer 转为int返回
​    相当把基本类型的值作为成员变量封装在对象中，产生这个对象的类就是包装类。
​    自动拆箱、装箱的原理是jdk1.5之后对编译器进行如下优化，
​     Integer i=129 <=> Integer i=new Integer(129); 
​     int i=new Integer(129) <=> int i=(new Intgeger(129)).intValue();
笔试题：   方便频繁使用-128～127之间的整数，会有一块缓存区域存放-128～127之间的数字，所以比地址相等，注意只存放整数
​    Integer i1 = 127;
​    Integer i2 = 127;
​    System.out.println(i1==i2);   //true
​    Integer i3 = 128;
​    Integer i4 = 128;
​    System.out.println(i3==i4);   //false

### 泛型类：声明时使用泛型

​    T type 表示泛型
​    K V  Key value
​    E Element
注意：
​    1.泛型只能使用引用类型，不能使用基本类型
​    2.泛型声明时不能使用在静态属性  
​    3.泛型方法<>返回类型前面    public static <T> void test(T t){}
​    4.只能访问对象的信息，不能修改信息   访问信息 syso(t)   修改信息 t.set()   不知道t是否有set方法

### 线程的五大状态

​    新建：new Thread()后处于新建状态
​    就绪：start方法启动线程，等待cpu分给时间片来调用run方法
​    运行：获得时间片后，run方法被调用
​    阻塞：可能因为I/O，线程调用sleep方法 或 访问加锁的公共资源 时，进入阻塞状态，将时间片让给其他就绪线程使用
​        1.join 合并线程
​        2.yield 暂停自己的线程 static 但不是绝对意义上的暂停，当cpu再次请求调度该线程的时候，状态从阻塞转为就绪，获取时间片后执行线程
​        3.sleep 休眠 暂停当前线程 不会释放锁， 排他锁 抱着对象休眠 ，可以用来倒计时，或者模拟网络延时
​    死亡：run方法调用完成自然死亡，一个未捕获的异常终止了run方法导致线程提前死亡，可以使用isAlive方法，判断线程是否在可运行状态（就绪运行阻塞），另外stop方法可以提前结束线程

    isAlive()   判断线程是否还活着，即线程是否还未终止
    getPriority()   获取线程优先级数值  优先级是获取cpu时间片的概率
    setPriority()   设置线程优先级数值  10最大 1最小 5正常
    getName()   获取线程名称
    setName()   设置线程名称
    currentThread() 获取当前正在运行的线程对象，取得自己本身
    
    线程同步与锁定 synchronized
    同步：多线程对同一份资源的共同访问，造成资源的不安全性，为了资源的准确和安全所以需要加入同步
        一、同步块
            synchronized(引用类型|this|类.class){}
        二、同步方法
            synchronized
        三、死锁：过多的同步容易造成死锁
            解决办法：生产者消费者模式 信号灯法(建立标志位) 管程法(使用容器)
            wait()  等待，释放锁/资源  sleep() 等待 不释放资源/锁
            notify() notifyall()   唤醒
            wait 和 notify 在同步下使用notify
    参考：
    https://blog.csdn.net/peter_teng/article/details/10197785

### 注解  Annotation JDK5以后出现

​    @Override   重写父类方法
​    @Deprecated   不建议使用这个方法，可能被废弃
​    @SuppressWarnings("all")   压制警告，可以加在类或方法上，参数可以设置为all

    元注解：用来解释注解的注解
    @Target  用于描述注解的使用范围
        @Target(value={ElementType.TYPE,ElementType.METHOD})
    @Retention  表示需要在什么级别保存该注解信息，用于描述注解的生命周期
        SOURCE CLASS RUNTIME(在运行时有效，可以被反射机制读取)
    
    如果注解只有一个value属性，赋值时可以直接写值例如 test(value=1)<=>test(1)

### 反射 

​    对象是表示或封装一些数据的，一个类被加载后，JVM会创建一个对应该类的Class对象，类的整个结构信息会放到对应的Class对象中。
​    Class对象就像一面镜子一样，通过这面镜子可以看到对应类的全部信息
​    一个类只对应一个Class 字节码文件

    获取字节码的三种方法
    Class clazz=user.getClass();
    Class clazz=Class.forName("com.lwj.pojo.User");
    Class clazz=User.class;
    
    动态加载类、动态获取类的信息（属性、方法、构造器）
        进行安全检查的反射方法调用大概是普通方法调用耗时的30倍
        不进行安全检查的反射方法调用大概是普通方法调用耗时的5倍
        setAccessbile(true)
    动态构造对象
    动态调用类和对象的任意方法、构造器
        在调用main方法的时候 m.invoke(null,(Object)new String[]{"ss","ff"}) 因为main方法是静态方法，所以第一个参数可以为null，
        如果不进行强制转换，编译器解析的时候会把数组当作可变参数来解析，["ss","ff"]解析为"ss","ff"，
    动态调用和处理属性
    获取泛型信息
        method.getGenericParameterType()    返回参数的泛型
        method.getGenericReturnType()   返回返回值的泛型
    处理注解
        获取注解 getAnnotations()/getAnnotation(class)
### 动态编译

​    JavaCompiler       
​    JavaCompiler compiler=ToolProvider.getSystem.javaCompiler();
​    int result=compiler.run(null,null,null,sourceFile);
​    第一个参数：为java编译器提供参数 为InputStream
​    第二个参数：得到Java编译器输出的参数，为OutputStream
​    第三个参数：接收编译器的错误信息，也是输出流
​    第四个参数：可变参数，（是一个String数组）能传入一个或多个Java源文件
​    返回值：0表示编译成功，非0表示编译失败
字节码操作 Javassist

### JVM核心机制

​    类加载机制
​        加载
​            将class文件字节码内容加载到内存中，并将class文件中的静态数据结构转换成方法区中的运行时数据结构，就是在方法区生成一个运行时数据（方法区类数据）
​            在堆中生成一个代表这个类的java.lang.Class对象，作为方法区类数据的唯一访问入口，
​        链接
​            验证
​                确保加载的类信息符合JVM规范，没有安全方面的问题
​            准备
​                正式为类变量(static变量)分配内存并设置类变量初始值(这里的值是数据类型默认值)的阶段，这些内存都将在方法区中进行分配
​            解析
​                虚拟机常量池内的符号引用替换为直接引用的过程
​        初始化
​            初始化阶段是执行类构造器<clinit>()方法的过程，类构造器<clinit>()方法是由编译器自动收集类中的所有类变量的赋值动作和静态语句块中的语句合并产生的
​            当初始化一个类的时候，如果发现其父类还没有进行过初始化，则需要先触发其父类的初始化
​            虚拟机会保证一个类的<clinit>()方法在多线程环境中被正确加锁和同步
​            当访问一个java类的静态域时，只有真正声明这个域的类才会被初始化

### 1、为什么重写equals还要重写hashcode

​    参考：https://blog.csdn.net/javazejian/article/details/51348320
​        equals方法是Object类的基本方法，用比较两个对象引用地址的方式来检测两个对象是否相同，

    Object中equals()方法  源码如下：
        public boolean equals(Object obj) {   return (this == obj);     }
        '=='运算符 两边如果是对象的话，比较的是对象的引用地址是否相同，如果是基础类型的话，则是比较基础类型的值是否相同，包括boolean类型
        所以默认情况下也就是从超类Object继承而来的equals方法与‘==’是完全等价的，比较的都是对象的内存地址，但我们可以重写equals方法，使其按照我们的需求的方式进行比较，如String类重写了equals方法，使其比较的是字符的序列，而不再是内存地址。
    
    Object中 hashCode()方法  源码如下：
        public native int hashCode();     
        在Object类中， hashcode是本地方法，由c/c++实现，hashCode方法返回的int值是通过Object对象的地址计算出来的，对于不同的对象，由于地址不同，所获取的哈希码自然也不会相等。
    
    Java API中有如下规定:
        hashcode()为true,equals()不一定为true   hashcode返回的是根据对象计算出的一个整型的哈希码，类似于md5生成的校验码，基本上是不可逆的，但不同的对象可能会生成相同的哈希码，虽然这个几率比较低，但依然可能发生
        equals()为true,hashcode()一定为true   
    
    只重写equals，不重写hashcode    在使用集合类，例如map 将没有重写hashcode的对象作为键时，那么会根据该对象的内存地址计算哈希值，然后取模定位到要存储的散列表中，取值时需要用内存地址一模一样的对象才能取到值(不考虑不同对象哈希值相同)
    只重写hashcode,不重写equals     如果发生散列冲突的话，hashcode可以正常定位，但是通过equals比较内存，地址会永远返回fasle，造成无法取出
    
    String类重写的equals()方法：

### 2、说一下map的分类和常见的情况

​     参考： https://tech.meituan.com/2016/06/24/java-hashmap.html
​    (1) HashMap：它根据键的hashCode值存储数据，大多数情况下可以直接定位到它的值，因而具有很快的访问速度，但遍历顺序却是不确定的。 HashMap最多只允许一条记录的键为null，允许多条记录的值为null。
​                 HashMap非线程安全，即任一时刻可以有多个线程同时写HashMap，可能会导致数据的不一致。如果需要满足线程安全，可以用 Collections的synchronizedMap方法使HashMap具有线程安全的能力，或者使用ConcurrentHashMap。

    (2) Hashtable：Hashtable是遗留类，很多映射的常用功能与HashMap类似，不同的是它承自Dictionary类，并且是线程安全的，任一时间只有一个线程能写Hashtable，并发性不如ConcurrentHashMap，
                   因为ConcurrentHashMap引入了分段锁。Hashtable不建议在新代码中使用，不需要线程安全的场合可以用HashMap替换，需要线程安全的场合可以用 ConcurrentHashMap 替换。
    
    (3) LinkedHashMap：LinkedHashMap是HashMap的一个子类，保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的，也可以在构造时带参数，按照访问次序排序。
    
    (4) TreeMap：基于红黑树(red-black tree)数据结构实现, 按 key 排序.TreeMap实现SortedMap接口，能够把它保存的记录根据键排序，默认是按键值的升序排序，也可以指定排序的比较器，当用Iterator遍历TreeMap时，得到的记录是排过序的。
                 如果使用排序的映射，建议使用TreeMap。在使用TreeMap时，key必须实现Comparable接口或者在构造TreeMap传入自定义的Comparator，否则会在运行时抛出java.lang.ClassCastException类型的异常。
                 我们平时可以使用String类作为key的原因是，String 已经实现了Comparble接口
    
    对于上述四种Map类型的类，要求映射中的key是不可变对象。不可变对象是该对象在创建后它的哈希值不会被改变。如果对象的哈希值发生变化，Map对象很可能就定位不到映射的位置了。

### JVM内存划分

java虚拟机内存可以分为三个区域：栈stack、堆heap、方法区method area
    栈stack
        1.jvm为每个线程创建一个栈，，栈中存放该线程执行方法的信息（实际参数、局部变量） 
        2.是描述方法执行的内存模型，每个方法被调用都会创建一个栈帧（存储局部变量、操作数等）
        3.所以栈是私有的，不同线程不能共享
        4.存储特点是“先进后出，后进先出”
        5.系统自动分配的连续内存空间，速度快
    堆heap 
        1.堆用于存储创建好的对象和数组（数组也是对象）
        2.JVM只有一个堆，被所有线程共享
        3.堆是一个不连续的内存空间，分配灵活，速度慢
    方法区
        1.JVM只有一个方法区，被所有线程共享
        2.方法区实际也是堆，只是用于存储类，常量相关的信息
        3.用于存放程序中永远不变或唯一的内容，（类信息[Class对象]，静态变量，字符串常量等）

JVM启动的时候会根据当前线程在内存中创建一个栈，在内存中创建方法区并加载 字节码文件、静态变量、静态方法、字符串常量
然后在静态方法中寻找main方法，开始程序的入口，并为main方法在栈中创建一个栈帧，存储局部变量、操作数、方法出口等

### 垃圾回收机制

​    寻找需要回收的对象，垃圾回收算法
​        1.引用计数器
​        2.引用可达法
​    通用的分代垃圾回收机制：
​        根据不同的生命周期将对象划分为 年轻代，年老代，持久代。
​        JVM将堆内存划分为 Eden Survivor Tenured/Old空间
​        年轻代在 Eden Survivor1 Survivor2   年老代在 Tenured/Old   持久代在方法区
​    垃圾回收过程：
​        1.新创建的对象，绝大多数都会存储在Eden中
​        2.当Eden满了(达到一定比例)不能创建新对象，则触发垃圾回收，通过算法将无用对象清理掉
​          然后剩余对象复制到某个Survivor，如Survivor1，同时清空Eden区
​        3.当Eden再次满了，会对Survivor1进行清理，然后将剩余的对象存到另一个Survivor中，如Survivor2
​          同时将Eden区中不能清空的对象，也复制到Survivor2中，保证Eden和Survivor1,均被清空
​        4.重复多次（默认15次）Survivor中没有被清理的对象，则会复制到老年代Tenured/Old
​        5.当Tenured/Old满了，则会触发一个一次完整地垃圾回收（FullGC），清理年轻代，老年代，成本较高，会对系统性能产生影响

开发中容易造成内存泄漏：
    1.创建大量无用对象
    2.静态集合类的使用，静态集合类所包含的对象不能被释放
    3.各种连接对象(IO流对象，数据库连接对象，网络连接对象)未关闭
    4.释放对象时没有删除对应当监听器

