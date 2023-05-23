package com.lwj.gc.unit5;

import java.util.ArrayList;

/**
 * -Xms50M -Xmx50M
 * -XX:+PrintGC  打印简单GC日志。 类似：-verbose:gc
 * -XX:+PrintGCDetails 打印GC详细信息
 * -XX:+PrintGCTimeStamps 输出GC的时间戳（以基准时间的形式）
 * -XX:+PrintGCDateStamps 输出GC的时间戳（以日期的形式）
 * -XX:+PrintHeapAtGC 在进行GC的前后打印出堆的信息
 */
public class TestGCLog {
    public static void main(String[] args) {
        ArrayList<byte[]> list = new ArrayList<byte[]>();
        for (int i = 0; i < 200; i++) {
            list.add(new byte[1020 * 100]);
        }
    }
}