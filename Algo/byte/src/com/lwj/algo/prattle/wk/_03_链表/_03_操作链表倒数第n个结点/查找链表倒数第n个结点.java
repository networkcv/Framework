package com.lwj.algo.prattle.wk._03_链表._03_操作链表倒数第n个结点;

import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.MyLinkedList;
import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.Node;

public class 查找链表倒数第n个结点 {
    public static void main(String[] args) {
        MyLinkedList list =new MyLinkedList();
        list.addNode(1);
        list.addNode(2);
        list.addNode(3);
//        Node node = FindKthToTail(list.getHead(), -1);
//        Node node = FindKthToTail(list.getHead(), 0);
//        Node node = FindKthToTail(list.getHead(), 1);
//        Node node = FindKthToTail(list.getHead(), 3);
        Node node = FindKthToTail(list.getHead(), 4);
        System.out.println(node.data);
    }
    public static Node FindKthToTail(Node head, int n) {
        if(n<=0)
            throw new RuntimeException("输入错误");
        Node slow=head;
        Node quick=head;
        while(n>0&&quick!=null){
            quick=quick.next;
            n--;
        }
        if(n>0)
            throw new RuntimeException("输入错误");
        while(quick!=null){
            quick=quick.next;
            slow=slow.next;
        }
        return slow;


    }
}
