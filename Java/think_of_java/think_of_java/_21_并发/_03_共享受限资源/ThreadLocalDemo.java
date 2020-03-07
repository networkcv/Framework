package _21_并发._03_共享受限资源;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/11
 * 使用ThreadLocal 存储每个线程的相关联对象的副本
 */
public class ThreadLocalDemo {
    public static class T1 {
        private static ThreadLocal<Integer> tl = new ThreadLocal<>();

        public T1(Integer integer) {
            tl.set(integer);
        }

        public Integer get() {
            return tl.get();
        }

    }

    public static void main(String[] args) {
        List<Thread> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                T1 t = new T1(random.nextInt(100));
                try {
                    TimeUnit.SECONDS.sleep(random.nextInt(3));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(t.get());
            }).start();
        }

    }
}
