package tmp;

import org.junit.Test;

/**
 * create by lwj on 2019/12/19
 * 唤醒另外一个线程
 */
public class AwakenOtherThread {
    @Test
    public void test(){
        Object lock = new Object();
        new Thread(()->{
            synchronized (lock){
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("B被唤醒了");
                lock.notify();
            }
        }).start();
        new Thread(()->{
            synchronized (lock){
                for (int i=0;i<10;i++){
                    if (i==5){
                        lock.notify();
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println("A print "+i);
                }
            }
        }).start();


    }
}
