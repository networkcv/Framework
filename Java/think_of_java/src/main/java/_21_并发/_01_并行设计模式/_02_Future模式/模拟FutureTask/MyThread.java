package _21_并发._01_并行设计模式._02_Future模式.模拟FutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/1
 */
public class MyThread implements Callable<String> {
    public String call() {
        return Thread.currentThread().getName() + " ok";
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> task = new FutureTask<>(new MyThread());
//        System.out.println(task.get());
        new Thread(task).start();
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(task.get());

        FutureData data = new FutureData(new MyThread());
        System.out.println(data.get());
        Thread thread = new Thread(data);
        thread.start();
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println(data.get());
    }
}
