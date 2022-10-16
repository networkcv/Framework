package com.lwj.jvisualvm;

/**
 * jmap是用来生成堆dump文件和查看堆相关的各类信息的命令，例如查看finalize执行队列，heap的详细信息和使用情况。
 * jmap [option] <pid> (连接正在执行的进程)
 *
 * 1.打印共享对象
 * jmap 11666
 *
 * 2.打印heap
 * jmap -heap 11666
 *
 * 3.打印存过这对象信息
 * jmap -histo:live 9668
 *
 * 4.查看类加载情况
 * jmap -clstats 9668
 *
 * 5.-finalizerinfo 打印在f-queue中等待执行finalizer方法的对象
 * jmap -finalizerinfo 9668
 *
 * 6.堆转储
 * jmap -dump:live,format=b,file=d:/dump.bin 9668
 */
public class Demo6_jmap {
    public static void main(String[] args) {
        System.out.println("jmap 指令");
        try {
            Thread.sleep(2000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
