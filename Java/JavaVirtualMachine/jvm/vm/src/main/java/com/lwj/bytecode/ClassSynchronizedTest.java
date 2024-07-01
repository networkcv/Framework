package com.lwj.bytecode;

import java.util.concurrent.TimeUnit;

/**
 * Date: 2024/6/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class ClassSynchronizedTest {

    public void test1() {
        synchronized (ClassSynchronizedTest.class) {
            try {
                System.out.println(1);
                TimeUnit.SECONDS.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static synchronized void test2() {
        System.out.println(2);
    }

    public static void main(String[] args) {
        new ClassSynchronizedTest().test1();
        test2();
    }
}
