package com.lwj._21_并发._03_共享受限资源;

import org.junit.Test;

/**
 * create by lwj on 2019/11/9
 */
public class SynEvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public int next() {
//    public synchronized int next() {
        currentEvenValue++;
        Thread.yield();
        currentEvenValue++;
        return currentEvenValue;
    }

    @Test
    public void test() {
        EvenChecker.test(new SynEvenGenerator());
    }
}
