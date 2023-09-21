## [Arthas命令列表](https://arthas.aliyun.com/doc/commands.html)

### 启动 arthas

```sh
curl -O https://arthas.aliyun.com/arthas-boot.jar
java -jar arthas-boot.jar
```

### dashboard 查看进程信息和内存概要信息

```bash
dashboard
```

```java
$ dashboard
ID     NAME                   GROUP          PRIORI STATE  %CPU    TIME   INTERRU DAEMON
17     pool-2-thread-1        system         5      WAITIN 67      0:0    false   false
27     Timer-for-arthas-dashb system         10     RUNNAB 32      0:0    false   true
11     AsyncAppender-Worker-a system         9      WAITIN 0       0:0    false   true
9      Attach Listener        system         9      RUNNAB 0       0:0    false   true
3      Finalizer              system         8      WAITIN 0       0:0    false   true
2      Reference Handler      system         10     WAITIN 0       0:0    false   true
4      Signal Dispatcher      system         9      RUNNAB 0       0:0    false   true
26     as-command-execute-dae system         10     TIMED_ 0       0:0    false   true
13     job-timeout            system         9      TIMED_ 0       0:0    false   true
1      main                   main           5      TIMED_ 0       0:0    false   false
14     nioEventLoopGroup-2-1  system         10     RUNNAB 0       0:0    false   false
18     nioEventLoopGroup-2-2  system         10     RUNNAB 0       0:0    false   false
23     nioEventLoopGroup-2-3  system         10     RUNNAB 0       0:0    false   false
15     nioEventLoopGroup-3-1  system         10     RUNNAB 0       0:0    false   false
Memory             used   total max    usage GC
heap               32M    155M  1820M  1.77% gc.ps_scavenge.count  4
ps_eden_space      14M    65M   672M   2.21% gc.ps_scavenge.time(m 166
ps_survivor_space  4M     5M    5M           s)
ps_old_gen         12M    85M   1365M  0.91% gc.ps_marksweep.count 0
nonheap            20M    23M   -1           gc.ps_marksweep.time( 0
code_cache         3M     5M    240M   1.32% ms)
Runtime
os.name                Mac OS X
os.version             10.13.4
java.version           1.8.0_162
java.home              /Library/Java/JavaVir
                       tualMachines/jdk1.8.0
                       _162.jdk/Contents/Hom
                       e/jre

```



### thread 获取线程堆栈

```sh
thread 1 | grep 'main('
    at demo.MathGame.main(MathGame.java:17)
```



###  jad 反编译 Class

> JD-GUI 反编译后的文件显示的是最开始的源码
>
> JAD反编译后的文件是处理过语法糖的源代码
>
> javap -c 反编译class文件，显示的是字节码文件

```java
jad com.demo.MathGame
```

```java
ClassLoader:
+-sun.misc.Launcher$AppClassLoader@3d4eac69
  +-sun.misc.Launcher$ExtClassLoader@66350f69

Location:
/tmp/math-game.jar

/*

 * Decompiled with CFR 0_132.
   */
   package demo;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MathGame {
    private static Random random = new Random();
    private int illegalArgumentCount = 0;
public static void main(String[] args) throws InterruptedException {
    MathGame game = new MathGame();
    do {
        game.run();
        TimeUnit.SECONDS.sleep(1L);
    } while (true);
}

public void run() throws InterruptedException {
    try {
        int number = random.nextInt();
        List<Integer> primeFactors = this.primeFactors(number);
        MathGame.print(number, primeFactors);
    }
    catch (Exception e) {
        System.out.println(String.format("illegalArgumentCount:%3d, ", this.illegalArgumentCount) + e.getMessage());
    }
}

public static void print(int number, List<Integer> primeFactors) {
    StringBuffer sb = new StringBuffer("" + number + "=");
    Iterator<Integer> iterator = primeFactors.iterator();
    while (iterator.hasNext()) {
        int factor = iterator.next();
        sb.append(factor).append('*');
    }
    if (sb.charAt(sb.length() - 1) == '*') {
        sb.deleteCharAt(sb.length() - 1);
    }
    System.out.println(sb);
}

public List<Integer> primeFactors(int number) {
    if (number < 2) {
        ++this.illegalArgumentCount;
        throw new IllegalArgumentException("number is: " + number + ", need >= 2");
    }
    ArrayList<Integer> result = new ArrayList<Integer>();
    int i = 2;
    while (i <= number) {
        if (number % i == 0) {
            result.add(i);
            number /= i;
            i = 2;
            continue;
        }
        ++i;
    }
    return result;
}
```
### Watch 查看函数运行时数据

|            参数名称 | 参数说明                                                     |
| ------------------: | :----------------------------------------------------------- |
|     *class-pattern* | 类名表达式匹配                                               |
|    *method-pattern* | 函数名表达式匹配                                             |
|           *express* | 观察表达式，默认值：`{params, target, returnObj}`            |
| *condition-express* | 条件表达式                                                   |
|                 [b] | 在**函数调用之前**观察                                       |
|                 [e] | 在**函数异常之后**观察                                       |
|                 [s] | 在**函数返回之后**观察                                       |
|                 [f] | 在**函数结束之后**(正常返回和异常返回)观察                   |
|                 [E] | 开启正则表达式匹配，默认为通配符匹配                         |
|                [x:] | 指定输出结果的属性遍历深度，默认为 1，最大值是 4             |
|         `[m <arg>]` | 指定 Class 最大匹配数量，默认值为 50。长格式为`[maxMatch <arg>]`。 |

```java
watch demo.MathGame funName {params,target,returnObj} -x
watch demo.MathGame funName {params,target,returnObj} -x 
```

```java
$ watch demo.MathGame primeFactors returnObj
Press Ctrl+C to abort.
Affect(class-cnt:1 , method-cnt:1) cost in 107 ms.
ts=2018-11-28 19:22:30; [cost=1.715367ms] result=null
ts=2018-11-28 19:22:31; [cost=0.185203ms] result=null
ts=2018-11-28 19:22:32; [cost=19.012416ms] result=@ArrayList[
    @Integer[5],
    @Integer[47],
    @Integer[2675531],
]
ts=2018-11-28 19:22:33; [cost=0.311395ms] result=@ArrayList[
    @Integer[2],
    @Integer[5],
    @Integer[317],
    @Integer[503],
    @Integer[887],
]
ts=2018-11-28 19:22:34; [cost=10.136007ms] result=@ArrayList[
    @Integer[2],
    @Integer[2],
    @Integer[3],
    @Integer[3],
    @Integer[31],
    @Integer[717593],
]
ts=2018-11-28 19:22:35; [cost=29.969732ms] result=@ArrayList[
    @Integer[5],
    @Integer[29],
    @Integer[7651739],
]

```

### tt 方法执行数据的时空隧道

记录下指定方法每次调用的入参和返回信息，并能对这些不同的时间下调用进行观测



### options 查看配置