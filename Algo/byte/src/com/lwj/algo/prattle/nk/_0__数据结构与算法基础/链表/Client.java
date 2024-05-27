package com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表;

import org.junit.Test;

public class Client {
    @Test
    public void test1(){
        MyLinkedList linkedList = new MyLinkedList();
        linkedList.addNode(3);
        linkedList.addNode(1);
        linkedList.addNode(4);
        linkedList.addNode(2);
//        linkedList.deleteNode(1);
        Node node = linkedList.sortList();
        System.out.println(node.data);
        linkedList.print();
    }
}
