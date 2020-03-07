package lwj.wk._03_链表._04_求链表的中间结点;

import lwj.nk._0__数据结构与算法基础.链表.MyLinkedList;
import lwj.nk._0__数据结构与算法基础.链表.Node;

/**
 * 还是用快慢指针的方式
 * create by lwj on 2019/2/21
 */
public class Test1 {
    public static void main(String[] args){
        MyLinkedList list = new MyLinkedList();
        list.addNode(1);
        list.addNode(2);
        list.addNode(3);
        list.addNode(4);
        Node middleNode = findMiddleNode(null);
//        Node middleNode = findMiddleNode(list.getHead());
        System.out.println(middleNode);
    }
    public static Node findMiddleNode(Node node){
        if(node==null)
            return null;
        Node quick =node;
        Node slow =node;
        while(quick.next!=null&&quick.next.next!=null){
            quick=quick.next.next;
            slow=slow.next;
        }
        return slow;
    }
}
