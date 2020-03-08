package _21_并发._07_JDK并发包.atomic;

import sun.misc.Unsafe;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * create by lwj on 2019/12/30
 */
public class AtomicIntegerTest {
    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(0);
        for (int j = 0; j < 1000; j++) {
            i.incrementAndGet();
        }

        Unsafe unsafe = Unsafe.getUnsafe();
        System.out.println(unsafe);
    }
}
