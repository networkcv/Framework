package com.lwj._21_并发._01_并行设计模式._02_Future模式.模拟FutureTask;

import java.util.concurrent.Callable;

/**
 * create by lwj on 2019/12/1
 */
public class FutureData implements Runnable, MyFuture {
    Callable<Object> callable;
    private Object result;

    public FutureData(Callable callable) {
        this.callable = callable;
    }

    public Object get() {
        return result;
    }

    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
