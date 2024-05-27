package com.lwj.algo.prattle.nk._16_链表倒数第k个结点;


import com.lwj.algo.prattle.nk._0_Utils.ListNode;

import java.util.Stack;

/**
 * create by lwj on 2018/10/4
 */
public class Test1 {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;

        long ll1 = System.nanoTime();
        System.out.println(FindKthToTail1(l1, 4));
        long ll2 = System.nanoTime();
        System.out.println(FindKthToTail(l1, 4));
        long ll3 = System.nanoTime();
        System.out.println(ll2 - ll1 + "  使用前后指针");
        System.out.println(ll3 - ll2 + "  借用栈");
    }

    public static ListNode FindKthToTail1(ListNode head, int k) {    //使用前后指针
        if (k < 1 || head == null) {
            return null;
        }
        ListNode befor = head;
        ListNode after = head;
        while (k != 0) {
            if (befor == null) {
                return null;
            }
            befor = befor.next;
            k--;
        }
        while (befor != null) {
            befor = befor.next;
            after = after.next;
        }
        return after;
    }

    public static ListNode FindKthToTail(ListNode head, int k) {    //借用栈
        if (k < 1) {
            return null;
        }
        Stack stack = new Stack();
        ListNode node = head;
        while (node != null) {
            stack.push(node);
            node = node.next;
        }
        if (k > stack.size()) {
            return null;
        }
        int i = 1;
        ListNode pop = (ListNode) stack.pop();
        while (i != k) {
            pop = (ListNode) stack.pop();
            i++;
        }
        return pop;


    }

}

