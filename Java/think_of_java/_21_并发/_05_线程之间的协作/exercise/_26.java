package _21_并发._05_线程之间的协作.exercise;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * create by lwj on 2019/11/18
 * 加入BusyBoy，由waitParson通知清理餐桌
 */
public class _26 {
    @Test
    public void test() {
        new Restaurant();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class BusyBoy extends Thread {
        private Restaurant restaurant;

        public BusyBoy(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try {
                    synchronized (this) {
                        if (restaurant.waitPerson.cleaned) {
                            wait();
                        }
                    }
                    System.out.println("BusyBoy clean "+restaurant.waitPerson.meal.getOrderNum()+"号留下的trash");
                    restaurant.waitPerson.cleaned = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    class WaitPerson extends Thread {
        private Restaurant restaurant;
        private Meal meal;
        public boolean cleaned = true;

        public WaitPerson(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (this) {
                        while (restaurant.meal == null)
                            wait();
                    }
                    System.out.println("waitPerson 取走" + restaurant.meal.getOrderNum() + "号订单");
                    synchronized (restaurant.chef) {
                        meal = restaurant.meal;
                        restaurant.meal = null;
                        restaurant.chef.notifyAll();
                    }
                    sleep(500);
                    System.out.println("waitPerson 通知 busyBoy 进行clean");
                    synchronized (restaurant.busyBoy) {
                        cleaned = false;
                        restaurant.busyBoy.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("WaitPerson InterruptedException");
            }
        }


    }

    class Chef extends Thread {
        Restaurant restaurant;
        private int count = 0;

        public Chef(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    synchronized (this) {
                        while (restaurant.meal != null)
                            wait();
                    }
                    if (++count == 5) {
                        System.out.println("out of food");
                        restaurant.exec.shutdownNow();
                        return;
                    }
                    System.out.println("chef 做了" + count + "号订单");
                    synchronized (restaurant.waitPerson) {
                        restaurant.meal = new Meal(count);
                        restaurant.waitPerson.notifyAll();
                    }
                    sleep(500);
                }
            } catch (InterruptedException exception) {
                System.out.println("Chef InterruptedException");
            }
        }
    }

    class Restaurant {
        Meal meal;
        ExecutorService exec = Executors.newCachedThreadPool();
        WaitPerson waitPerson = new WaitPerson(this);
        Chef chef = new Chef(this);
        BusyBoy busyBoy = new BusyBoy(this);

        public Restaurant() {
            exec.execute(waitPerson);
            exec.execute(chef);
            exec.execute(busyBoy);
        }


    }


    class Meal {
        private final int orderNum;

        public int getOrderNum() {
            return orderNum;
        }

        public Meal(int orderNum) {
            this.orderNum = orderNum;
        }

        @Override
        public String toString() {
            return "Meal{" +
                    "orderNum=" + orderNum +
                    '}';
        }
    }
}