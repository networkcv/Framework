package com.lwj.bytecode;


import java.io.IOException;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

/**
 * create by lwj on 2020/1/22
 */
public class Test2 {
    public static int b = 1;
    private int a = 1;
    final int c = 3;

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

    public void test3() {
        Date date = new Date();
    }


    public void test4() {
        int a = 0;
        {
            int b = 0;
            b = b + 1;
        }
        int c = 1;
    }

    private static class Student {
        private int age;


        public Student(int age) {
            this.age = age;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }

    public Student test5() {
        Student student = null;
        try {
            student = new Student(1);
            return student;
        } finally {
            student.setAge(2);
        }
    }

    public void test6(){
        System.out.println(test5().getAge());
    }
}
