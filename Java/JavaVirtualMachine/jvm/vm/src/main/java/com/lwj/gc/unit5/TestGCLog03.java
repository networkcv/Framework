package com.lwj.gc.unit5;

public class TestGCLog03 {
    private static final int _1MB = 1024 * 1024;

    /**
     * VM参数：（参数序号对应实验序号）
     *  -Xms20M -Xmx20M -Xmn10M -XX:+UseParNewGC -XX:+PrintGCDetails -XX:PretenureSizeThreshold=3145728
     */
    public static void testPreteureSizeThreshold() {
        byte[] allocation;
        allocation = new byte[4 * _1MB];
    }

    public static void main(String[] args) {
        testPreteureSizeThreshold();
    }
}


