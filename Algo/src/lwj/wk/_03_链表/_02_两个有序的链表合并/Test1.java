package lwj.wk._03_链表._02_两个有序的链表合并;

import lwj.nk._0__数据结构与算法基础.链表.MyLinkedList;
import lwj.nk._0__数据结构与算法基础.链表.Node;

public class Test1 {
    public static void main(String[] args) {
        MyLinkedList linkedList1 =new MyLinkedList();
        linkedList1.addNode(0);
        linkedList1.addNode(3);
        linkedList1.addNode(5);
        linkedList1.addNode(6);
        MyLinkedList linkedList2 =new MyLinkedList();
        linkedList2.addNode(2);
        linkedList2.addNode(4);
        linkedList2.addNode(6);
        linkedList2.addNode(8);
        Node merge = merge(linkedList1.getHead(), linkedList2.getHead());
        MyLinkedList.printNode(merge);

    }
    public static Node merge(Node list1, Node list2){
        if(list1==null)
            return list2;
        if(list2==null)
            return list1;
        if(list1.data<=list2.data){
            list1.next=merge(list1.next,list2);
            return list1;
        }else {
            list2.next=merge(list1,list2.next);
            return list2;
        }


    }
}
