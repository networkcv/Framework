package com.lwj.algo.prattle.nk._18_合并两个有序链表;

import com.lwj.algo.prattle.nk._0_Utils.ListNode;

/**
 * create by lwj on 2018/10/5
 */
public class Test1 {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        ListNode l44 = new ListNode(4);
        ListNode l5 = new ListNode(5);
        ListNode l6 = new ListNode(6);
        l1.next = l3;
        l3.next = l44;
        l44.next = l5;      //1 3 4 5

        l2.next = l4;
        l4.next = l6;       //2 4 6
        long l11 = System.nanoTime();
//        ListNode.printListNode(Merge(l1, l2));
        long l22 = System.nanoTime();
//        ListNode.printListNode(merge1(l1, l2));
        long l33 = System.nanoTime();
        System.out.println(l22 - l11 + "使用哨兵");     //534974使用哨兵
        System.out.println(l33 - l22 + "使用递归");     //261835使用递归

    }

    public static ListNode Merge(ListNode head1, ListNode head2) {  //使用哨兵
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        ListNode root = new ListNode();  // 务必创建一个临时结点，用于添加元素时方便
        ListNode pointer = root;     // 用于指向合并后的新链的尾结点

        while (head1 != null && head2 != null) {
            if (head1.val < head2.val) {
                pointer.next = head1;
                head1 = head1.next;
            } else {
                pointer.next = head2;
                head2 = head2.next;
            }
            pointer = pointer.next;             // 将指针移动到合并后的链表的末尾
        }

        if (head1 != null) {
            pointer.next = head1;
        }
        if (head2 != null) {
            pointer.next = head2;
        }
        return root.next;
    }

    public static ListNode merge1(ListNode head1, ListNode head2) {     //递归
        if (head1 == null) {
            return head2;
        }
        if (head2 == null) {
            return head1;
        }
        // 记录两个链表中头部较小的结点
        ListNode tmp = head1;
        if (tmp.val < head2.val) {
            // 如果第一个链表的头结点小，就递归处理第一个链表的下一个结点和第二个链表的头结点
            tmp.next = merge1(head1.next, head2);
        } else {
            // 如果第二个链表的头结点小，就递归处理第一个链表的头结点和第二个链表的头结点的下一个结点
            tmp = head2;
            tmp.next = merge1(head1, head2.next);
        }

        // 返回处理结果
        return tmp;
    }

}
