package com.lwj.algo.prattle.bk._00_Utils;

public class MyLinkedListTest {
    private final Node sentry=new Node();
    private Node head=null;
    private int count;

    public MyLinkedListTest(){
        this.head=sentry;
    }
    //插入
    public void insert(Node node){
        Node tmpHead=head;
        while(tmpHead.next!=null){
            tmpHead=tmpHead.next;
        }
        tmpHead.next=node;
    }
    //打印
    public void print(){
        Node tmp=head.next;
        while(tmp!=null){
            System.out.print(tmp.data+" ");
            tmp=tmp.next;
        }
    }

    public static void main(String[] args) {
        MyLinkedListTest myLinkedList=new MyLinkedListTest();
        myLinkedList.print();
        myLinkedList.insert(new Node(1));
        myLinkedList.insert(new Node(2));
        myLinkedList.insert(new Node(3));
        myLinkedList.print();
        System.out.println(myLinkedList.head.data);
        System.out.println(myLinkedList.sentry.data);
    }


}
