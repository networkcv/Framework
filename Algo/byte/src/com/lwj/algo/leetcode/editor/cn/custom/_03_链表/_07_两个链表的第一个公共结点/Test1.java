package com.lwj.algo.leetcode.editor.cn.custom._03_链表._07_两个链表的第一个公共结点;

import com.lwj.algo.leetcode.editor.cn.utils.Node;
import org.junit.Test;

/**
 * create by lwj on 2019/2/25.
 */
public class Test1 {
    //暴力破解
    public static Node firstCommonalityNode(Node n1, Node n2){
        if(n1==n2) return n1;
        Node tmpHead1=n1;
        while(tmpHead1!=null){
            Node tmpHead2=n2;
            while(tmpHead2!=null){
                if(tmpHead1==tmpHead2)
                    return tmpHead1;
                tmpHead2=tmpHead2.next;
            }
            tmpHead1=tmpHead1.next;
        }
        return null;
    }
    public static void main(String[] args) {
        //测试用例
        Node n1 =new Node(1);
        Node n2 =new Node(2);
        Node n3 =new Node(3);
        Node n4 =new Node(4);
        Node n5 =new Node(5);
        Node n6 =new Node(6);
        n1.next=n4;
        n2.next=n3;
        n3.next=n4;
        n4.next=n5;
//        Node node = firstCommonalityNode(n1, n2);
        Node node = firstCommonalityNode(n1, n6);
        System.out.println(node);
    }

    @Test
    public void test1(){
        //测试用例
        Node n1 =new Node(1);
        Node n2 =new Node(2);
        Node n3 =new Node(3);
        Node n4 =new Node(4);
        Node n5 =new Node(5);
        Node n6 =new Node(6);
        n1.next=n2;
        n2.next=n3;
        n4.next=n5;
        n5.next=n6;
        System.out.println(f(n1,n4));

    }
    //牛客  双指针
    public  Node f(Node n1,Node n2){
        Node p1 = n1;
        Node p2 = n2;
        while(p1!=p2){
            p1 = (p1==null ? n2 : p1.next);
            p2 = (p2==null ? n1 : p2.next);
        }
        return p1;
    }
}
