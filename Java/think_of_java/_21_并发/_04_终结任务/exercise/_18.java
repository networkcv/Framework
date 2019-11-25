package _21_并发._04_终结任务.exercise;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/15
 * 安全退出一个sleep阻塞中的方法
 */
public class _18 {
    public static class NotTask {
        static void longTimeMethod() {
            try {
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("sleep Interrupt");
            }
            System.out.println("Exit longTimeMethod() ");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            NotTask.longTimeMethod();
        });
        thread.start();
        TimeUnit.SECONDS.sleep(2);
        thread.interrupt();
    }
}
