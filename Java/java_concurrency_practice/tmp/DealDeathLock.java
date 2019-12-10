package tmp;


import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/12/10
 */
public class DealDeathLock {
    @Test
    public void test() {
    }

    public static void main(String[] args) {
        ChopsticksHolder chopsticksHolder = new ChopsticksHolder();
        CyclicBarrier barrier = new CyclicBarrier(5, () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                String name = Thread.currentThread().getName();
                try {
                    while (true) {
                        barrier.await();
                        Chopstick[] apply = chopsticksHolder.apply(name);
                        if (apply == null) {
                            TimeUnit.SECONDS.sleep(1);
                        } else {
                            TimeUnit.MILLISECONDS.sleep(random.nextInt(2) * 1000 + 1000);
                            chopsticksHolder.free(apply[0], apply[1], name);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(i + " ok");
        }

    }

    public static class Chopstick {
        private Integer id;

        public Chopstick(Integer id) {
            this.id = id;
        }
    }

    public static class ChopsticksHolder {
        private final List<Chopstick> list;

        public ChopsticksHolder() {
            ArrayList<Chopstick> list = new ArrayList<>();
            list.add(new Chopstick(0));
            list.add(new Chopstick(1));
            list.add(new Chopstick(2));
            list.add(new Chopstick(3));
            list.add(new Chopstick(4));
            this.list = list;
        }

        public synchronized Chopstick[] apply(String name) {
            Chopstick[] res = null;
            if (list.size() >= 2) {
                res = new Chopstick[2];
                res[0] = list.remove(list.size() - 1);
                res[1] = list.remove(list.size() - 1);
                System.out.println(name + "拿到了" + res[0].id + "，" + res[1].id + "号筷子");
            } else {
                System.out.println(name + "没有拿到了筷子");
            }
            return res;
        }

        public synchronized void free(Chopstick c1, Chopstick c2, String name) {
            list.add(c1);
            list.add(c2);
            System.out.println(name + "归还了" + c1.id + "，" + c2.id + "号筷子");
        }

    }

}
