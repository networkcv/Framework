package com.lwj.gc.unit3;

import java.util.ArrayList;

/**
 * -XX:+PrintCommandLineFlags  查看程序使用的默认JVM参数
 * -XX:+UseSerialGC  设置垃圾回收器为年轻代垃圾回收器使用serial ,老年代使用serial Old
 *
 * -XX:+UseParNewGC          并行垃圾回收器,收集新生代
 * -XX:+UseConcMarkSweepGC  CMS 并行垃圾回收器,可以收集老年代
 *
 * -XX:+UseParallelGC   使用parallel scavenge 垃圾回收器
 * -XX:MaxGCPauseMillis  设置暂停时间,不推荐
 * -XX:+UseAdaptiveSizePolicy  自适应配置策略 (Eden、Suvisor区的比例，晋升老年代的对象年龄等)
 */
public class SerialGC {
    public static void main(String[] args) {
        ArrayList<byte[]>  list = new ArrayList<byte[]>();
        while (true){
            byte[] arr = new byte[1024];
            list.add(arr);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
