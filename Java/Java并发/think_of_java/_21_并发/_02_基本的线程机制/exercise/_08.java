package think_of_java._21_并发._02_基本的线程机制.exercise;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/8
 * 守护线程创建的线程也是守护线程
 */
public class _08 {
    public static class DaemonThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r);
            //设置创建的线程为后台守护线程
            //可以通过注释这行代码来检测，如果只剩Daemon线程，jvm会自动关闭
//            thread.setDaemon(true);
            return thread;
        }
    }

    public static void main(String[] args) {
        //通过自定义的DaemonThreadFactory来创建线程
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool(new DaemonThreadFactory());
        for (int i = 0; i < 3; i++) {
            cachedThreadPool.execute(() -> {
                try {
                    //创建的后台线程要执行的任务是，先休眠2s，再打印自己是否是后台线程
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println(Thread.currentThread().getName() + " : " + Thread.currentThread().isDaemon());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        cachedThreadPool.shutdown();
    }

}
