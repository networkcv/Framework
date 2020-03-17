package _10_内部类._06_匿名内部类;

import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2020/3/17
 * 局部变量的final问题
 */
public class Test1 {

   static class Inner {
        void i() {
            test2();
        }
    }

    public static void test2() {
        int i = 1;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(500);
//                    i=2;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(i);
            }
        }).start();
        System.out.println(i);
    }

    public void test3() {
        try {
            TimeUnit.SECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Test1 test1 = new Test1();
        test1.test2();
        test1.test3();
    }
}
