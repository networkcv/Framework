package com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表;

public class Node {
   public  Node next =null;
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
