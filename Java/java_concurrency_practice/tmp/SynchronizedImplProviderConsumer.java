package tmp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/12
 * 使用synchronized实现生产者消费者模型
 */
public class SynchronizedImplProviderConsumer {
    private final List<Object> list = new ArrayList<>(5);

    void provider() {
        while (true) {
            synchronized (list) {
                while (list.size() == 1) {
                    try {
                        list.wait();
                        System.out.println(Thread.currentThread().getName()+ "被唤醒");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                list.add(new Object());
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + "完成生产");
                list.notify();
            }
        }
    }

    void consumer() {
        while (true) {
            synchronized (list) {
                while (list.size() == 0) {

                    try {
                        list.wait();
                        System.out.println(Thread.currentThread().getName()+ "被唤醒");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                list.remove(0);
                System.out.println(Thread.currentThread().getName() + "完成消费");
                Thread.yield();
                list.notify();
            }
        }
    }

    public static void main(String[] args) {
        SynchronizedImplProviderConsumer synchronizedImplProviderConsumer = new SynchronizedImplProviderConsumer();
        Thread thread = new Thread(() -> {
            synchronizedImplProviderConsumer.consumer();
        }, "消费者1");
        Thread thread1 = new Thread(() -> {
            synchronizedImplProviderConsumer.consumer();
        }, "消费者2");
        Thread thread2 = new Thread(() -> {
            synchronizedImplProviderConsumer.provider();
        }, "生产者");
        thread.start();
        thread1.start();
        thread2.start();
    }
}
