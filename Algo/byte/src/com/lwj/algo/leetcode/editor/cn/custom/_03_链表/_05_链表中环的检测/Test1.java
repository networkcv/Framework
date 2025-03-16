package com.lwj.algo.leetcode.editor.cn.custom._03_链表._05_链表中环的检测;//package com.lwj.algo.prattle.wk._03_链表._05_链表中环的检测;
//
//import com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表.Node;
//
///**
// * create by lwj on 2019/2/21
// */
//public class Test1 {
//    public static void main(String[] args){
//        Node node1=new Node(1);
//        Node node2=new Node(2);
//        Node node3=new Node(3);
//        Node node4=new Node(4);
//        node1.next=node2;
////        node2.next=node1;
//        node2.next=node3;
//        node3.next=node4;
//        node4.next=node1;
//        boolean b=detectionLoop(node1);
//        System.out.println(b);
//    }
//
//    private static boolean detectionLoop(Node node1) {
//        Node q=node1;
//        Node s=node1;
//        while(q!=null&&q.next!=null){
//            q=q.next.next;
//            s=s.next;
//            if(q!=null&&(q==s))
//                return true;
//        }
//        return false;
//    }
//
//
//}
