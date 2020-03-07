package lwj.nk._0__数据结构与算法基础.链表.删除链表重复元素;

import lwj.nk._0__数据结构与算法基础.链表.MyLinkedList;
import lwj.nk._0__数据结构与算法基础.链表.Node;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

public class DeleteDuplicate {
    public static void main(String[] args) {
        MyLinkedList myLinkedList = new MyLinkedList();
        myLinkedList.addNode(1);
        myLinkedList.addNode(2);
        myLinkedList.addNode(2);
        myLinkedList.addNode(3);
//        myLinkedList.addNode(3);
//        myLinkedList.addNode(4);
//        myLinkedList.addNode(4);
//        myLinkedList.addNode(4);
//        myLinkedList.addNode(5);
        myLinkedList.print();
        System.out.println();//        deleteDuplicate1(myLinkedList.head);
//        deleteDuplicate2(myLinkedList.head);
//       Node node = deleteDuplicate3(myLinkedList.head);
       Node node = deleteDuplicate4(myLinkedList.head);
        myLinkedList.print();
    }

    //定义set来判别是否存在
    public static void deleteDuplicate1(Node head) {
        if (head == null) {
            return;
        }
        Set<Integer> set = new HashSet<Integer>();
        Node curNode = head;
        set.add(head.data);
        while (curNode.next != null) {
            if (set.add(curNode.next.data)) {
                curNode = curNode.next;
            } else {
                if (curNode.next.next != null) {
                    curNode.next = curNode.next.next;
                } else {
                    curNode.next = null;
                }
            }
        }
    }

    //定义table来判断是否存在，并使用前驱结点，以空间换时间
    public static void deleteDuplicate2(Node head) {
        Hashtable<Integer, Integer> table = new Hashtable<>();
        Node pre = null;
        Node tmp = head;
        if (tmp != null) {
            if (!table.containsKey(tmp.data)) {
                table.put(tmp.data,0);
                pre=tmp;
            } else {
                pre.next = tmp.next;
            }
            tmp = tmp.next;
        }
    }

    //不定义变量来存储值，通过循环嵌套来实现
    public static Node   deleteDuplicate3(Node pHead) {
        Node tmp =pHead;
        while(tmp!=null){
            Node tmpp=tmp.next;
           while(tmpp!=null){
                if(tmp.data==tmpp.data){
                    tmp.next=tmpp.next;
                }
                tmpp=tmpp.next;
           }
            tmp=tmp.next;
        }
        return pHead;
    }

    //将重复的元素全部删除
    public static Node   deleteDuplicate4(Node pHead) {
        Node first = new Node(-1);//设置一个trick

        first.next = pHead;

        Node p = pHead;
        Node last = first;
        while (p != null && p.next != null) {
            if (p.data == p.next.data) {
                int val = p.data;
                while (p!= null&&p.data == val)
                    p = p.next;
                last.next = p;
            } else {
                last = p;
                p = p.next;
            }
        }
        return first.next;
    }


}
