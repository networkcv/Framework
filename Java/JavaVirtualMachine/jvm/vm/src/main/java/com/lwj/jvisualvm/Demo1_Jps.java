package com.lwj.jvisualvm;

/**
 * jps -q 显示进程id
 * jps -l 输出jar包路径，类全名
 * jps -m 输出主类名,及传入main方法的参数
 * jps -v 输出主类名,及输出JVM参数
 *
 */
public class Demo1_Jps {

    public static void main(String[] args) {
        System.out.println("jps 指令");
        try {
            Thread.sleep(2000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
