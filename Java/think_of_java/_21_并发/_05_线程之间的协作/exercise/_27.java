package think_of_java._21_并发._05_线程之间的协作.exercise;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2019/11/18
 * 厨师和服务员，生产者消费者模型
 * 使用显式锁，ReentrantLock和Condition
 */
public class _27 {
    @Test
    public void test() {
        new Restaurant();
        try {
            TimeUnit.SECONDS.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
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

    class WaitPerson extends Thread {
        private Restaurant restaurant;

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        public WaitPerson(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    try {
                        lock.lock();
                        while (restaurant.meal == null)
                            condition.await();
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    } finally {
                        lock.unlock();
                    }
                    System.out.println("waitPerson 取走" + restaurant.meal.getOrderNum() + "号订单");
                    try {
                        restaurant.chef.lock.lock();
                        restaurant.meal = null;
                        restaurant.chef.condition.signalAll();
                    } finally {
                        restaurant.chef.lock.unlock();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class Chef extends Thread {
        Restaurant restaurant;

        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        private int count = 0;

        public Chef(Restaurant restaurant) {
            this.restaurant = restaurant;
        }

        @Override
        public void run() {
            try {
                while (!Thread.interrupted()) {
                    try {
                        lock.lock();

                        while (restaurant.meal != null)
                            condition.await();
                        if (++count == 5) {
                            System.out.println("out of food");
                            restaurant.exec.shutdownNow();
                        }
                    } finally {
                        lock.unlock();
                    }
                    System.out.println("chef 做了" + count + "号订单");
                    try {

                        restaurant.waitPerson.lock.lock();
                        restaurant.meal = new Meal(count);
                        restaurant.waitPerson.condition.signalAll();
                        sleep(1000);
                    } finally {
                        restaurant.waitPerson.lock.unlock();
                    }
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }

        }
    }

    class Restaurant {
        Meal meal;
        ExecutorService exec = Executors.newCachedThreadPool();
        WaitPerson waitPerson = new WaitPerson(this);
        Chef chef = new Chef(this);

        public Restaurant() {
            exec.execute(waitPerson);
            exec.execute(chef);
        }
    }


}