package think_of_java._21_并发._05_线程之间的协作;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/18
 *  使用阻塞队列来控制线程之间协作，没有显式的同步
 *  消除了使用wait和notify所造成的类耦合
 */
public class ToastOMatic {
}

class Toast {
    public enum Status {DRY, BUTTERED, JAMMED}

    private Status status = Status.DRY;
    private final int id;

    public Toast(int id) {
        this.id = id;
    }

    public void butter() {
        status = Status.BUTTERED;
    }

    public void jam() {
        status = Status.JAMMED;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Toast{" +
                "status=" + status +
                ", id=" + id +
                '}';
    }
}

class ToastQueue extends LinkedBlockingQueue<Toast> {
}

class Toaster implements Runnable {
    private ToastQueue toastQueue;
    private int count = 0;

    public Toaster(ToastQueue tq) {
        toastQueue = tq;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                TimeUnit.MILLISECONDS.sleep(500);
                //make toast
                System.out.println("toast" + count + " is ok ");
                Toast toast = new Toast(count++);
                toastQueue.put(toast);
            }
        } catch (InterruptedException e) {
            System.out.println("Toaster InterruptedException");
        }
        System.out.println("Toaster off");
    }
}

//apply butter to toast
class Butter implements Runnable {
    private ToastQueue dryQueue, butterQueue;

    public Butter(ToastQueue dryQueue, ToastQueue butterQueue) {
        this.dryQueue = dryQueue;
        this.butterQueue = butterQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //Blocks until next piece fo toast is available
                Toast t = dryQueue.take();
                t.butter();
                System.out.println(t);
                butterQueue.put(t);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Butter off");
    }
}

//Apply jam to buttered toast
class Jammer implements Runnable {
    private ToastQueue butteredQueue, finishQueue;

    public Jammer(ToastQueue butteredQueue, ToastQueue finishQueue) {
        this.butteredQueue = butteredQueue;
        this.finishQueue = finishQueue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //Blocks until next piece of toast is available
                Toast t = butteredQueue.take();
                t.jam();
                System.out.println(t);
                finishQueue.put(t);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Jam off");
    }
}

//Consume the toast
class Eater implements Runnable {
    private ToastQueue finishQueue;
    private int counner = 0;

    public Eater(ToastQueue finishQueue) {
        this.finishQueue = finishQueue;
    }

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            //Blocks until next piece of toast is available
            try {
                Toast t = finishQueue.take();
                System.out.println("eat " + t.getId());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("eat off");
        }
    }

    public static void main(String[] args) {
        ToastQueue
                dryQueue = new ToastQueue(),
                butterQueue = new ToastQueue(),
                finishedQueue = new ToastQueue();
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Toaster(dryQueue));
        cachedThreadPool.execute(new Butter(dryQueue, butterQueue));
        cachedThreadPool.execute(new Jammer(butterQueue, finishedQueue));
        cachedThreadPool.execute(new Eater(finishedQueue));
        try {
            TimeUnit.SECONDS.sleep(3);
            cachedThreadPool.shutdownNow();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}