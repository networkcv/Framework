package tmp;

import java.util.concurrent.CountDownLatch;

/**
 * create by lwj on 2019/12/7
 */
public class OrderExample {
    int a = 0;
    boolean flag = false;

    public void writer() {
        a = 1;
        flag = true;
    }

    public int reader() {
        if (flag) {
            return a;
        }
        return -1;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100000; i++) {
            OrderExample orderExample = new OrderExample();
            CountDownLatch countDownLatch = new CountDownLatch(2);
            Thread thread = new Thread(() -> {
                countDownLatch.countDown();
                orderExample.writer();
            });
            Thread thread2 = new Thread(() -> {
                countDownLatch.countDown();
                int reader = orderExample.reader();
                if (reader == 0) {
                    System.out.println(reader);
                }
            });
            thread.start();
            thread2.start();
        }
    }

}