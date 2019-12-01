package tmp;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/1
 */
public class ThreadTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<String> task = new FutureTask<>(() -> {
            return "ok";
        });
//        Thread thread = new Thread(task);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
        thread.setPriority(thread.MIN_PRIORITY);
        thread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(task.get());
    }
}
