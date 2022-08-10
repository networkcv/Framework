package com.lwj.jvisualvm;

import java.io.IOException;

/**
 * jstat [option] VMID [interval] [count] 查看JVM运行时的状态信息，包括内存状态、垃圾回收等
 *
 *-class class loader的行为统计
 *-gc 垃圾回收堆的行为统计
 *-gccapacity 各个垃圾回收代容量(young,old,perm)和他们相应的空间统计
 *-gcutil 垃圾回收统计概述
 *-gcnew 新生代行为统计
 *-gcnewcapacity 新生代与其相应的内存空间的统计
 *-gcold 年老代和永生代行为统计
 *-gcoldcapacity 年老代行为统计
 * 案例:
 * jstat -gcutil 11666 1000 3
 *jstat -gc 11666 1000 3
 *
  jvm 参数: -Xms20M -Xmx20M -Xmn10M -XX:+UseSerialGC  -XX:+PrintGCDetails -verbose:gc
 */
public class Demo4_jstat {
    public static void main(String[] args) throws IOException {
        final int _1M = 1024 * 1024 ;
        byte[] b1 = new byte[2 * _1M];
        System.out.println("创建b1...");
        System.in.read();

        byte[] b2 = new byte[2 * _1M];
        System.out.println("创建b2...");
        System.in.read();

        byte[] b3 = new byte[2 * _1M];
        System.out.println("创建b3...");
        System.in.read();

    }
}
