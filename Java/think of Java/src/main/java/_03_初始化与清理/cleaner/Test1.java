package _03_初始化与清理.cleaner;

import sun.misc.Cleaner;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2020/4/1
 */
public class Test1 {

    public static void main(String[] args) throws InterruptedException {
        Object obj = new Object();
        Cleaner cleaner = Cleaner.create(obj, () -> {
            System.out.println("1");
            System.out.println(Thread.currentThread().getName());
        });
        obj = null;
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main1(String[] args) throws InterruptedException {
        Object obj = new Object();
        Object obj1 = obj;
        Cleaner cleaner = Cleaner.create(obj1, () -> {
            System.out.println("1");
            System.out.println(Thread.currentThread().getName());
        });
        // 由于上面有两个强引用指向，所以需要都置为null，才会被GC掉
        obj = null;
        obj1 = null;
        System.gc();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
