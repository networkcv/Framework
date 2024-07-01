package com.lwj.bytecode;

/**
 * Date: 2024/6/30
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class SynchronizedTest {

    public void test1() {
        synchronized (new Object()) {
            System.out.println();
        }
    }

    public synchronized void test2() {
    }
}
