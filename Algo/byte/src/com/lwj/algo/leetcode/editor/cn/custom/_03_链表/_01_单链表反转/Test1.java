package com.lwj.algo.leetcode.editor.cn.custom._03_链表._01_单链表反转;


import com.lwj.algo.leetcode.editor.cn.utils.Node;

/**
 * create by lwj on 2019/2/18
 */
public class Test1 {
    public static void main(String[] args) {
        Node root = new Node();
        Node node1 = new Node(2);
        Node node2 = new Node(3);
        Node node3 = new Node(4);
        root.next = node1;
        node1.next = node2;
        node2.next = node3;
        Node node = fun1(null);   //传入null
//        Node node = fun1(node3);    //一个结点的情况
//        Node node = fun1(node1);  //两个结点的情况
//        Node node = fun1(root);   //多个结点的情况
        while (node != null) {
            System.out.println(node.val);
            node = node.next;
        }
    }

    public static Node fun1(Node node) {
        Node pre = null;
        Node current = null;
        while (node != null) {
            current = node;   //保存当前结点，作为下一个结点的前驱结点
            node = node.next; //保存当前结点的后继结点，不然无法进行while循环
            //已经保存好了后继结点的位置，现在就可以进行操作了
            current.next = pre; //将当前结点的next指向上一个结点，也就是前驱结点pre，默认pre为空，刚好第一个结点的next也指向空
            pre = current;    //为下次循环做准备，pre指向当前结点 下次循环时，pre就代表了前驱结点
        }
        return current;

    }


}
