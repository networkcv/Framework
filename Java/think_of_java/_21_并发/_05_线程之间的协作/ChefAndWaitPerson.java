package think_of_java._21_并发._05_线程之间的协作;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * create by lwj on 2019/11/18
 * 厨师和服务员，生产者消费者模型
 */
public class ChefAndWaitPerson {
}

class WaitPerson extends Thread {
    private Restaurant restaurant;

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
                    restaurant.meal = null;
                    restaurant.chef.notifyAll();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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
                }
                System.out.println("chef 做了" + count + "号订单");
                synchronized (restaurant.waitPerson) {
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.notifyAll();
                }
                sleep(1000);
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

    public static void main(String[] args) {
        new Restaurant();
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
