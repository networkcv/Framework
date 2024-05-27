package com.lwj.algo.prattle.wk._03_链表._08_删除链表中重复结点;

import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.MyLinkedList;
import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.Node;

/**
 *  删除前：1-> 2 -> 2 -> 3 -> 3 -> 3 -> 4
 *  删除后：1 -> 2 -> 3 -> 4
 * create by lwj on 2019/2/25.
 */
public class Test1_不包括原结点 {
    public static void main(String[] args) {
        MyLinkedList list=new MyLinkedList();

        list.addNode(1);
        list.addNode(2);
        list.addNode(2);
        list.addNode(3);
        list.addNode(3);
        list.addNode(3);
        list.addNode(4);
//        MyLinkedList.printNode(deleteSameNode(list.getHead()));
//        MyLinkedList.printNode(deleteSameNode(null));
    }

    private static Node deleteSameNode(Node head) {
        if(head==null)return null;
        Node tmp=head;
        Node next=null;
        while(tmp!=null&&tmp.next!=null){
            if(tmp.data==tmp.next.data){
                next=tmp;
                while(next.data==next.next.data){
                    next=next.next;
                }
                tmp.next=next.next;
            }
            tmp=tmp.next;
        }
        return head;
    }

}
