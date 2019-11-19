package think_of_java._21_并发._05_线程之间的协作.exercise;

import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * create by lwj on 2019/11/18
 * 使用两个单独的组装线来创建图有花生黄油和果冻的吐司三明治
 * 一个用于花生黄油，第二个用于果冻，然后把两条线合并
 */
public class _29 {


    @Test
    public void test() {
        //
        BlockingQueue<Sandwich> sandwichQueue = new ArrayBlockingQueue<>(5);
        BlockingQueue<Sandwich> okSandwichQueue = new ArrayBlockingQueue<>(5);
        AtomicInteger count = new AtomicInteger();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        //生产三明治的线程
        cachedThreadPool.execute(() -> {
            try {
                while (!Thread.interrupted()) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    Sandwich sandwich = new Sandwich(count.incrementAndGet());
                    sandwichQueue.put(sandwich);
                    System.out.println("sandwich" + count.get() + " is production");
                }
            } catch (InterruptedException e) {
                System.out.println("provide sandwich interrupted");
            }
            System.out.println("provide sandwich off");

        });
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //涂黄油的线程
        cachedThreadPool.execute(() -> {
            try {
                while (!Thread.interrupted()) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    Sandwich sandwich = sandwichQueue.take();
                    if (sandwich.getStatus() == Sandwich.Status.DRY) {
                        sandwich.butter();
                        sandwichQueue.put(sandwich);
                        System.out.println("finish apply butter to sandwich" + sandwich.getCount());
                    } else {
                        sandwich.ok();
                        okSandwichQueue.put(sandwich);
                        System.out.println("finish apply butter to sandwich" + sandwich.getCount());
                        System.out.println("sandwich" + sandwich.getCount() + " is ok");
                    }
                }
            } catch (Exception e) {
                System.out.println("apply butter interrupted");
            }
            System.out.println("apply butter off");

        });

        //加果冻的线程
        cachedThreadPool.execute(() -> {
            try {
                while (!Thread.interrupted()) {
                    TimeUnit.MILLISECONDS.sleep(500);
                    Sandwich sandwich = sandwichQueue.take();
                    if (sandwich.getStatus() == Sandwich.Status.DRY) {
                        sandwich.jelly();
                        sandwichQueue.put(sandwich);
                        System.out.println("finish apply jelly to sandwich" + sandwich.getCount());
                    } else {
                        sandwich.ok();
                        okSandwichQueue.put(sandwich);
                        System.out.println("finish apply jelly to sandwich" + sandwich.getCount());
                        System.out.println("sandwich" + sandwich.getCount() + " is ok");
                    }
                }
            } catch (Exception e) {
                System.out.println("apply jelly interrupted");
            }
            System.out.println("apply jelly off");
        });

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        cachedThreadPool.shutdownNow();
    }
}

class Sandwich {
    public enum Status {DRY, BUTTER, JELLY, OK}

    private int count;

    public int getCount() {
        return count;
    }

    public Sandwich(int count) {
        this.count = count;
    }

    private Status status = Status.DRY;

    public void butter() {
        this.status = Status.BUTTER;
    }

    public void jelly() {
        this.status = Status.JELLY;
    }

    public void ok() {
        this.status = Status.OK;
    }


    public Status getStatus() {
        return status;
    }

}