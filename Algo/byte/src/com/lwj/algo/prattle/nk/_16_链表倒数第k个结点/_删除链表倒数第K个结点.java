package com.lwj.algo.prattle.nk._16_链表倒数第k个结点;

import com.lwj.algo.prattle.nk._0_Utils.ListNode;

/**
 * create by lwj on 2018/10/5
 */
public class _删除链表倒数第K个结点 {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
//        ListNode.printListNode(fun1(l1, 4));
        ListNode.printListNode(fun2(l1, 4));
    }

    public static ListNode fun1(ListNode head, int k) {
        if (k < 1) {
            return head;
        }
        ListNode before = head;
        ListNode after = head;
        while (k > 0) {
            before = before.next;
            if (before == null) {
                return head.next;
            }
            k--;
        }
        while (before.next != null) {
            before = before.next;
            after = after.next;
        }
        after.next = after.next.next;
        return head;
    }
    public static ListNode fun2(ListNode head, int k) {
        if(k<1){
            return head;
        }
        ListNode before=head;
        ListNode after=head;
        while(k>0){
            before=before.next;
            if(before==null){
                return head.next;
            }
            k--;
        }
        while(before.next!=null){
            before=before.next;
            after=after.next;
        }
        after.next=after.next.next;
        return head;
    }

}
