package _99_other;

import java.lang.ref.*;

/**
 * create by lwj on 2020/3/31
 */
public class 引用类型 {
    public static void main(String[] args) {
        Object counter = new Object();
        ReferenceQueue refQueue = new ReferenceQueue();
        WeakReference<Object> p = new WeakReference<>(counter, refQueue);

        counter = null;
        System.gc();
        try {
            Reference remove = refQueue.remove(3000L);
            if (remove != null) {
                System.out.println("ok");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
