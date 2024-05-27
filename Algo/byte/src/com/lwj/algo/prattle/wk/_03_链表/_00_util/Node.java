package com.lwj.algo.prattle.wk._03_链表._00_util;

public class Node {
   public Node next =null;
   public  int data;

    public Node(int value) {
        this.data = value;
    }

    @Override
    public String toString() {
        return "Node{" +
                "data=" + data +
                '}';
    }
}
