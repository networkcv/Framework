package com.lwj._01_singleton_pattern;

/**
 * create by LiuWangJie on 2018/7/25
 */
public class MyThread extends Thread {
    private static int num = 0;
    private String name;

    public MyThread(){
        num++;
    }

    public MyThread(String name){
        this.name = name;
    }

    public void run() {
//      System.out.println("主动创建的第"+num+"个线程");
        God god = God.getGod();
        System.out.println("name:"+name+" 子线程ID:"+Thread.currentThread().getId());
        System.out.println("name:"+name+" god:"+god);
    }


}
