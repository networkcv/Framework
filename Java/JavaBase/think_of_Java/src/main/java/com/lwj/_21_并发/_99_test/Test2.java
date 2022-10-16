package com.lwj._21_并发._99_test;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2020/6/5
 */
public class Test2 {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            throw new RuntimeException();
        });
        thread.start();
        System.out.println();
    }
}
