package com.lwj.jvisualvm;

import java.util.*;

/**
 *   -Xms100m -Xmx100m -XX:+UseSerialGC
 */
public class Demo7_JConsole01 {
    /**
     * 内存占位符对象，一个OOMObject大约占8K
     */
    static class OOMObject {
        public byte[] placeholder = new byte[8 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        List<OOMObject> list = new ArrayList<OOMObject>();
        for (int i = 0; i < num; i++) {
            // 稍作延时，令监视曲线的变化更加明显
            Thread.sleep(200);
            list.add(new OOMObject());
        }
        System.gc();
    }

    public static void main(String[] args) throws Exception {
        fillHeap(1000000);
        System.gc();
    }
}
