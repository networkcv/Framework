package tmp;

/**
 * create by lwj on 2019/12/12
 * 测试synchronized 是否可以绑定多个条件变量
 * 结论：synchronized 中的条件变量是绑定在锁上，所以只能有一个条件变量
 */
public class SynchronizedConditionTest {
    public static void main(String[] args) {
        Object lock = new Object();
        Object o = new Object();
        new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}
