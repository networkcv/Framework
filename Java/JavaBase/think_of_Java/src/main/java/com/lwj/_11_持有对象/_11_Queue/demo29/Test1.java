package com.lwj._11_持有对象._11_Queue.demo29;

import java.util.PriorityQueue;

/**
 * create by lwj on 2020/3/18
 * 如果不实现Comparable接口的话，无法进行比较，自然无法判断优先级
 * 所以在插入第二个元素的时候会报错
 */
public class Test1 implements Comparable<Test1> {
    int i;

    public Test1(int i) {
        this.i = i;
    }

    public static void main(String[] args) {
        PriorityQueue<Test1> queue = new PriorityQueue<>();
        queue.offer(new Test1(2));
        queue.offer(new Test1(1));
        queue.offer(new Test1(3));
        System.out.println(queue);
    }

    @Override
    public int compareTo(Test1 o) {
        return Integer.compare(this.i, o.i);
    }

    @Override
    public String toString() {
        return "Test1{" +
                "i=" + i +
                '}';
    }
}
