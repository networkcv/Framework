package com.lwj.algo.prattle.nk._37_两个链表的第一个公共结点;


/**
 *  用A链表的每一个结点和B链表的每一个结点对比 相同返回，时间复杂度为O(mn)
 * create by lwj on 2018/9/26
 */
public class Test37_1_暴力破解 {
    private static class ListNode {
        int val;
        ListNode next;

        public ListNode() {}

        public ListNode(int val) {
            this.val = val;
        }
        public String toString() {
            return val + "";
        }
    }
    public static ListNode findFirstCommonNode(ListNode head1, ListNode head22) {
        ListNode head2;
        while (head1!=null){
            head2=head22;
            while (head2!=null){
                if(head1==head2){
                    return head1;
                }
                    head2=head2.next;
            }
            head1=head1.next;
        }
        return head1;
    }
    public static void main(String[] args){
        test1();
        test2();
        test3();
        test4();
    }
    private static void test1() {
        // 第一个公共结点在链表中间
        // 1 - 2 \
        //        4
        //     3 /

        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);

        n1.next = n2;
        n2.next = n4;

        n3.next = n4;

        System.out.println(findFirstCommonNode(n1, n3)); // 6
    }
    private static void test2() {
        // 没有公共结点
        // 1 - 2 - 3 - 4
        //
        // 5 - 6 - 7
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        ListNode n7 = new ListNode(7);

        n1.next = n2;
        n2.next = n3;
        n3.next = n4;

        n5.next = n6;
        n6.next = n7;
        System.out.println(findFirstCommonNode(n1, n5)); // null
    }

    private static void test3() {
        // 公共结点是最后一个结点
        // 1 - 2 - 3 - 4 \
        //                7
        //         5 - 6 /
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        ListNode n7 = new ListNode(7);

        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n7;

        n5.next = n6;
        n6.next = n7;
        System.out.println(findFirstCommonNode(n1, n5)); // 7
    }

    private static void test4() {
        // 公共结点是第一个结点
        // 1 - 2 - 3 - 4 - 5
        // 两个链表完全重合
        ListNode n1 = new ListNode(1);
        ListNode n2 = new ListNode(2);
        ListNode n3 = new ListNode(3);
        ListNode n4 = new ListNode(4);
        ListNode n5 = new ListNode(5);
        ListNode n6 = new ListNode(6);
        ListNode n7 = new ListNode(7);

        n1.next = n2;
        n2.next = n3;
        n3.next = n4;
        n4.next = n5;

        System.out.println(findFirstCommonNode(n1, n1)); // 1
    }
}
