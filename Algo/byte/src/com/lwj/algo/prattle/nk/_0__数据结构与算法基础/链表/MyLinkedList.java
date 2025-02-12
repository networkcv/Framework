package com.lwj.algo.prattle.nk._0__数据结构与算法基础.链表;


public class MyLinkedList {
    public Node head = null;
    public int count = 0;

    //返回头结点
    public Node getHead(){
        return head;
    }

    //添加结点
    public void addNode(int data) {
        Node node = new Node(data);
        if (head == null) {
            head = node;
            count++;
            return;
        }
        Node tmp = head;
        while (tmp.next != null) {
            tmp = tmp.next;
        }
        tmp.next = node;
        count++;
    }

    //根据索引删除指定结点
    public Boolean deleteNode(int index) {
        if (index < 1 || index > length()) {
            return false;
        }
        if (index == 1) {
            head = head.next;
            count--;
            return true;
        }
        Node tmp = head;
        for (int i = 2; i < index; i++) {   //找到要删除结点的前一个结点
            tmp = tmp.next;
        }
        tmp.next = tmp.next.next;     //实现删除
        count--;
        return true;
    }

    //对链表进行排序，返回排序后的头结点 不对结点的next修改，仅修改结点中的值，来实现有序
    public Node sortList() {
        Node nextNode = null;
        int temp = 0;
        Node curNode = head;
        while (curNode.next != null) {
            nextNode = curNode.next;
            while (nextNode != null) {
                if (nextNode.data < curNode.data) {
                    temp=curNode.data;
                    curNode.data=nextNode.data;
                    nextNode.data=temp;
                }
                nextNode=nextNode.next;
            }
            curNode = curNode.next;
        }
        return  head;

    }

    //打印
    public void print() {
        System.out.print("[ ");
        if (head != null) {
            Node tmp = head;
            while (tmp.next != null) {
                System.out.print(tmp.data + " -> ");
                tmp = tmp.next;
            }
            System.out.print(tmp.data); //手动打印最后一个元素
        }
        System.out.print(" ]");
    }

    //打印结点
    public static void printNode(Node node){
        while(node!=null){
            System.out.print(node.data+" ");
            node=node.next;
        }
    }

    public int length() {
        return count;
    }
}
