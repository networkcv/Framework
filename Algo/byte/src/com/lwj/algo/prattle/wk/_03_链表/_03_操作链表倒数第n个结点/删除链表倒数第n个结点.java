package com.lwj.algo.prattle.wk._03_链表._03_操作链表倒数第n个结点;

import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.MyLinkedList;
import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.Node;

public class 删除链表倒数第n个结点 {
    public static void main(String[] args) {
        MyLinkedList list = new MyLinkedList();
        list.addNode(1);
        list.addNode(2);
        list.addNode(3);
        list.addNode(4);
//        Node node = delKthToTail(list.getHead(), -1);
//        Node node = delKthToTail(list.getHead(), 0);
//        Node node = delKthToTail(list.getHead(), 1);
//        Node node = delKthToTail(list.getHead(), 4);
        Node node = delKthToTail(list.getHead(), 5);
        MyLinkedList.printNode(node);
    }

    public static Node delKthToTail(Node head, int n) {
        if (n <= 0)
            return head;
        Node pre = head;
        Node quick = head;
        while (quick != null && n > 0) {    //退出条件  1.quick为空 2.n==0 3.quick为空的同时n==0
            quick = quick.next;
            n--;
        }
        /*
            1.quick==null n>0   n超出链表长度
            2.quick!=null n=0   要删除的不是第一个结点
            3.quick==null n=0   要删除的是第一个结点
         */
        if (n > 0)   //n超出链表长度
            return head;
        if (n == 0 && quick == null)     //要删除的是第一个结点
            return head.next;

        while (quick.next != null) {    //要删除的不是第一个结点
            pre = pre.next;
            quick = quick.next;
        }
        pre.next = pre.next.next;
        return head;
    }

}
