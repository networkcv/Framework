package com.lwj.algo.prattle.wk._03_链表._08_删除链表中重复结点;

import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.MyLinkedList;
import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.Node;

/**
 *   删除前：1-> 2 -> 2 -> 3 -> 3 -> 3 -> 4
 *   删除后：1 -> 4
 * create by lwj on 2019/2/25.
 */
public class Test2_包括原结点 {
    public static void main(String[] args) {
        MyLinkedList list=new MyLinkedList();
        //测试用例
        //1223445   122333  1223334 112 11

        list.addNode(1);
        list.addNode(1);
        list.addNode(3);
        list.addNode(3);
        list.addNode(4);
        list.addNode(4);
        list.addNode(5);
//        list.addNode(4);
        Node node = deleteSameNode1(list.getHead());
//        MyLinkedList.printNode(deleteSameNode(null));
        MyLinkedList.printNode(node);
    }

    private static Node deleteSameNode1(Node head) {
        return  null;
    }

    private static Node deleteSameNode(Node head) {
        if(head==null) return  null;
        Node first =new Node(-1);
        first.next=head;
        Node pre=first;
        Node p=head;
        while(p!=null&&p.next!=null){
            if(p.data==p.next.data){
                int val=p.data;
                while(p!=null&&p.data==val){
                    p=p.next;
                }
                pre.next=p;
            }else {
                pre=p;
                p=p.next;
            }
        }
        return first.next;

    }

//    private static Node deleteSameNode1(Node head) {
//        if(head==null)return null;
//        Node tmp=head;
//        Node next=null;
//        Node pre=null;
//        while(tmp!=null&&tmp.next!=null){
//            if(tmp.data==tmp.next.data){
//               next=tmp;
//                while(next.data==next.next.data){
//                    next=next.next;
//                }
//                if(pre==null){
//                    pre=next.next;
//                }
//                pre.next=next.next;
//            pre=tmp;
//            tmp=next.next;
//        }
//        return head;
//    }
}
