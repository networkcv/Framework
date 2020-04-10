package _99_other.包装类;

public class BaseTypeThreadUnSafe {
    int i = 0;

    public void increment() {
        i++;
    }

    public static void main(String[] args) throws InterruptedException {
        BaseTypeThreadUnSafe bean = new BaseTypeThreadUnSafe();
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                bean.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                bean.increment();
            }
        });
        thread.start();
        thread2.start();
        thread.join();
        thread2.join();
        System.out.println(bean.i);
    }
}