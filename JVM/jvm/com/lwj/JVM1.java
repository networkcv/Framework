package com.lwj;

import org.junit.Test;

/**
 * create by lwj on 2019/12/28
 */
public class JVM1 {
    @Test
    public void test3(){
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            Object o = new Object();
        }

    }
    @Test
    public void test() {
        int a=0x11;
        System.out.println(a);
        System.out.println(Runtime.getRuntime().totalMemory() / 1024 / 1024);
        System.out.println(Runtime.getRuntime().freeMemory() / 1024 / 1024);
    }

    public static void main(String[] args) {
        long s = System.currentTimeMillis();
        for (int i = 0; i < 1000000000; i++) {
            n();
        }
        long e = System.currentTimeMillis();
        System.out.println(e-s);

    }

    public static void n() {
        byte[] bytes = new byte[2];
        bytes[0]=1;
    }
}
