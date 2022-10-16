package com.lwj.jvisualvm;

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
 * jstata -class 11666
 * jstata -compiler 11666
 *
 */
public class Demo3_jstat {
    public static void main(String[] args) {
        System.out.println("jinfo 指令");
        try {
            Thread.sleep(2000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
