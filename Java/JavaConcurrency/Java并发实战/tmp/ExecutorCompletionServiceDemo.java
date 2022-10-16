package tmp;

import org.junit.Test;

import java.util.concurrent.*;

/**
 * create by lwj on 2019/11/26
 */
public class ExecutorCompletionServiceDemo {
    @Test
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        Future<Integer> submit = cachedThreadPool.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                TimeUnit.SECONDS.sleep(2);
                return new Integer(1);
            }
        });
        System.out.println(submit.get());
        while (submit.isDone()){
        System.out.println(submit.get());
        break;
        }
    }

    @Test
    public void test2(){
        new Thread(() -> {
          throw   new RuntimeException();
        }).start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
