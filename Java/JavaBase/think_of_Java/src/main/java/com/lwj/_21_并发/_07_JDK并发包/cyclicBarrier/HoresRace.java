package com.lwj._21_并发._07_JDK并发包.cyclicBarrier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * create by lwj on 2019/11/19
 */
public class HoresRace {
    static final int FINISH_LINE = 50;
    private List<Horse> horses = new ArrayList<>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;
    boolean flag = true;

    public HoresRace(int nHorse, final int pause) {
        barrier = new CyclicBarrier(nHorse, () -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < FINISH_LINE; i++) {
                sb.append("=");
            }
            sb.append("终点");
            System.out.println(sb);
            for (Horse horse : horses) {
                System.out.println(horse.tracks());
            }
            if (flag) {
                try {
                    System.out.println("各选手已就位，比赛即将开始");
                    TimeUnit.MILLISECONDS.sleep(500);
                    System.out.println("3");
                    TimeUnit.MILLISECONDS.sleep(500);
                    System.out.println("2");
                    TimeUnit.MILLISECONDS.sleep(500);
                    System.out.println("1");
                    TimeUnit.MILLISECONDS.sleep(500);
                    System.out.println("开始");
                    flag = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (Horse horse : horses) {
                if (horse.getStrides() >= FINISH_LINE) {
                    System.out.println("恭喜" + horse.id + "号马" + " 拿下本场比赛胜利!");
                    System.out.println("do you want player again ?");
                    exec.shutdownNow();
                    return;
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(pause);
            } catch (InterruptedException e) {
                System.out.println("barrier-action sleep interrupted");
            }
        });
        for (int i = 0; i < nHorse; i++) {
            Horse horse = new Horse(barrier);
            horses.add(horse);
            exec.execute(horse);
        }
    }

    public static void main(String[] args) {
        int nHorses = 7;
        int pause = 500;
        new HoresRace(nHorses, pause);
    }
}

class Horse implements Runnable {
    private static int counter = 1;
    public final int id = counter++;
    private int strides = 0;
    private static Random rand = new Random(47);
    private static CyclicBarrier barrier;
    boolean flag = true;


    public Horse(CyclicBarrier cyclicBarrier) {
        barrier = cyclicBarrier;
    }

    public synchronized int getStrides() {
        return strides;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                synchronized (this) {
                    //给不同马加速
                    if (id == 3) {
                        strides += rand.nextInt(5);
                    } else {
                        strides += rand.nextInt(4);
                    }
                }
                barrier.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Horse{" +
                "id=" + id +
                '}';
    }

    public String tracks() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            sb.append("*");
        }
        sb.append(id);
        return sb.toString();
    }
}
