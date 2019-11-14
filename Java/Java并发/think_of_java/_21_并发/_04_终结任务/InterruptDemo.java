package think_of_java._21_并发._04_终结任务;

/**
 * create by lwj on 2019/11/14
 */
public class InterruptDemo {
    static Thread thread;

    public static void main(String[] args) {
        thread = new Thread(() -> {
            boolean a=true;
            while (a){}
            System.out.println("1");
        });
        thread.start();
        thread.interrupt();
    }
}
