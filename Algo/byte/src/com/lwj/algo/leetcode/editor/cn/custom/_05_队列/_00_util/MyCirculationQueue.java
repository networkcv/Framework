package com.lwj.algo.leetcode.editor.cn.custom._05_队列._00_util;

/**
 * 循环队列(物理上是数组，逻辑上通过下标模拟环) 实现循环队列  消耗一个存储空间判别空满
 * 判满条件 ：(tail+1)%n==head  也就是tail指针再走一步就指到head指针
 *      例如 n=6 head指向arr[0] tail指向arr[5],此时arr[5]并没有存储元素,调用插入方法时
 * 判空条件：head==tail  也就是tail指针和head指针指向同一个位置
 */
public class MyCirculationQueue {
    private int n = 8;
    private String[] arr = new String[n];
    private int head = 0;
    private int tail = 0;

    public boolean enqueue(String s) {
        if ((tail + 1) % n == head) return false;
        arr[tail]=s;
        tail = (tail + 1) % n;
        return true;
    }

    public String dequque(){
        if(head==tail)  return null;
        String tmp =arr[head];
        head=(head+1)%n;
        return tmp;
    }
}
