package com.lwj.gc.unit5;

import java.util.ArrayList;

public class TestGCLog04 {
    private static final int _1MB = 1024 * 1024;

    /**
     * -XX:SurvivorRatio=8 -XX:+UseParallelGC -XX:+UseParallelOldGC  -XX:+PrintGCDetails -Xloggc:D://logs/gc.log
     */
    public static void main(String[] args) {
        ArrayList<byte[]> list = new ArrayList<byte[]>();
        for (int i = 0; i < 2000; i++) {
            byte[] arr = new byte[1024 * 1024];
            list.add(arr);
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


