package com.lwj.bytecode;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2020/1/22
 */
public class Test1 {
    public static int b = 1;
    private int a = 1;

    public int getA() throws RuntimeException {
        System.out.println(super.getClass());
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    ReentrantLock lock = new ReentrantLock();

    public void test() {
        lock.lock();
        System.out.println(1);
        lock.unlock();
    }

    public void test1() {
        try {
            RuntimeException runtimeException = new RuntimeException();
            throw runtimeException;
        } catch (NullPointerException e) {
            System.out.println("catch");
        } catch (Exception e) {
            System.out.println("c");
        } finally {
            System.out.println("ok");
        }
    }

    public void test2() throws IOException {
        throw new IOException();
    }
    public void test3(){
        Date date = new Date();
    }


}
