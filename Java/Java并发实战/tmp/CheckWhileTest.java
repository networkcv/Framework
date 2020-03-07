package tmp;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/11
 */
public class CheckWhileTest {

    static ArrayList<Object> list = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        Object lock = new Object();
        new Thread(() -> {
            synchronized (lock) {
                while (list.size() == 0) {
                    System.out.println("check size " + list.size());
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("check size again " + list.size());
                list.remove(0);
            }
        }).start();;
        TimeUnit.SECONDS.sleep(2);
        synchronized (lock) {
            lock.notify();
        }
    }

}
