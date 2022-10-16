package com.lwj.jvisualvm;


public class Deom8_JVisualVM2 {
    /**
     * 线程死锁等待演示
     */
    static class SynAddRunalbe implements Runnable {
        int a, b;
        public SynAddRunalbe(int a, int b) {
            this.a = a;
            this.b = b;
        }

        public void run() {
            synchronized (Integer.valueOf(a)) {
                synchronized (Integer.valueOf(b)) {
                    System.out.println(a + b);
                }
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(new Demo7_JConsole03.SynAddRunalbe(1, 2)).start();
            new Thread(new Demo7_JConsole03.SynAddRunalbe(2, 1)).start();
        }
    }
}
