package _21_并发._02_基本的线程机制;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * create by lwj on 2019/11/9
 *
 * 由于线程的本质特性，无法在当前线程捕获到从其他线程中逃逸的异常，一旦异常逃逸出run方法，它就会向外传播到控制台
 * 而我们通常需要记录异常日志,所以就需要对线程做运行时的异常处理
 * JDK5 之后可以使用Executor来解决这个问题,通过给 Thread实例 设置一个(实现 UnCatchExceptionHandler接口)未捕获异常处理器
 * 也可以使用 Thread.setDefaultUnCatchExceptionHandler() 来配置默认的未捕获异常处理器
 */
public class UnCatchExceptionHandlerDemo {
    @Test
    public void test() {
        try {
            new Thread(() -> {
                throw new RuntimeException();
            }).start();
        } catch (Exception e) {
            //不能捕获到其他线程的异常，所以下面这行代码不会被打印
            System.out.println("出错了");
        }
    }

    @Test
    //通过对线程实例设置unCaughtExceptionHandler,
    public void test2() {
        Thread thread = new Thread(() -> {
            throw new RuntimeException();
        });
        thread.setUncaughtExceptionHandler(new MyUnCatchExceptionHandler());
        thread.start();
    }

    @Test
    //通过在ThreadFactory中设置defaultUnCatchExceptionHandler
    public void test3() {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new MyThreadFactory());
        cachedThreadPool.execute(() -> {
            throw new RuntimeException();
        });
    }


    public static class MyThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            Thread.setDefaultUncaughtExceptionHandler(new MyUnCatchExceptionHandler());
            return thread;
        }
    }

    public static class MyUnCatchExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            System.out.println("this is MyUnCatchExceptionHandler ");
            System.out.println("catch " + e + " 记录日志");
        }
    }
}
