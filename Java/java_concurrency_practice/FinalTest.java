package Java.java_concurrency_practice;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * create by lwj on 2019/11/24
 * 不加 final 修饰 也可能会出现线程安全问题，修改不局限于修改内容，可能还会修改引用
 */
public class FinalTest {

    //    public final AtomicInteger count = new AtomicInteger(0);
    public AtomicInteger count = new AtomicInteger(0);

    public void increment() {
        count.incrementAndGet();
    }

    public int getC() {
        return count.get();
    }

    public static void main(String[] args) throws InterruptedException {
        FinalTest finalTest = new FinalTest();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                finalTest.increment();
            }
        });
        Thread thread1 = new Thread(() -> {
            finalTest.count = new AtomicInteger(0);
        });
        thread.start();
        thread1.start();
        thread.join();
        thread1.join();
        System.out.println(finalTest.getC());

        TreeMap treeMap = new TreeMap();
    }
}

