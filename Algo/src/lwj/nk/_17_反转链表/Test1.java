package lwj.nk._17_反转链表;

import lwj.nk._0_Utils.ListNode;

import java.util.Stack;

/**
 * create by lwj on 2018/10/4
 */
public class Test1 {
    //输入一个链表，反转链表后，输出新链表的表头。
    public static void main(String[] args) {
        ListNode l1 = new ListNode(1);
        ListNode l2 = new ListNode(2);
        ListNode l3 = new ListNode(3);
        ListNode l4 = new ListNode(4);
        l1.next = l2;
        l2.next = l3;
        l3.next = l4;
        long l11 = System.nanoTime();
        ListNode node = ReverseList1(l1);
        ListNode.printListNode(node);
        long l22 = System.nanoTime();
        ListNode.printListNode(ReverseList(node));
        long l33 = System.nanoTime();
        System.out.println(l22 - l11 + "使用栈");
        System.out.println(l33 - l22 + "不使用栈");
    }

    public static ListNode ReverseList(ListNode head) {
        //对每一个结点进行如下操作：保存当前结点的后一个结点next，将当前结点的后一个结点置为前一个结点pre，再重置pre为当前结点
        ListNode pre = null, next = null;
        while (head != null) {
            next = head.next; //保存当前结点的后一个结点next
            head.next = pre;  //将当前结点的下一个结点置为前一个结点pre
            pre = head;   //再重置pre为当前结点
            head = next;  //使用刚才保存点next使整个链表循环
        }
        return pre;//当head等于尾结点的时候，next为空 pre保存了当前结点，然后执行head=Null 退出循环 返回反转后的头结点

    }

    public static ListNode ReverseList1(ListNode head) {   //使用栈
        if (head == null) {
            return null;
        }
        Stack stack = new Stack();
        ListNode pre;
        ListNode next;
        while (head != null) {
            stack.push(head);
            head = head.next;
        }
        head = pre = (ListNode) stack.pop();
        while (!stack.isEmpty()) {
            next = (ListNode) stack.pop();
            if (stack.isEmpty()) {
                next.next = null;
            }
            pre.next = next;
            pre = next;
        }
        return head;
    }

    public static ListNode ReverseList2(ListNode head) {
        ListNode tmp=null;
        ListNode pre = null;
        while (head != null) {
            tmp = head.next;
            head.next = pre;
            pre = head;
            head =tmp;
        }
        return pre;

    }


}
