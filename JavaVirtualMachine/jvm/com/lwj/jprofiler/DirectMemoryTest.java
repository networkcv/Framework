package com.lwj.jprofiler;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.LinkedList;

/**
 * Date: 2022/9/6
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class DirectMemoryTest {
    /*
      -Xms40m -Xmx40m
      -XX:MetaspaceSize=20m
      -XX:MaxMetaspaceSize=20m
      -XX:+UseConcMarkSweepGC
      -XX:NativeMemoryTracking=detail  如果要跟踪其它部分的内存占用，需要通过来开启这个特性 [off|summary|detail]

     */
    private static final int _1MB = 1024 * 1024 * 10;

    public static void main(String[] args) throws Exception {
        Field unsafeField = Unsafe.class.getDeclaredFields()[0];
        unsafeField.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeField.get(null);
        LinkedList<Object> objects = new LinkedList<>();
        while (true) {
            long l = unsafe.allocateMemory(_1MB);
            ByteBuffer by = ByteBuffer.allocateDirect(_1MB);
            objects.add(by);
//            objects.add(new Object());
        }
    }
}
