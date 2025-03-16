package com.lwj.algo.leetcode.editor.cn.custom._03_链表._06_检测链表中的环并返回环入口;


import com.lwj.algo.leetcode.editor.cn.utils.Node;

/**
 * 参考链接： https://www.nowcoder.com/profile/163334/codeBookDetail?submissionId=1512740
 * create by lwj on 2019/2/21
 */
public class Test1 {
    public static void main(String[] args) {
        Node node1 = new Node(1);
        Node node2 = new Node(2);
        Node node3 = new Node(3);
        Node node4 = new Node(4);
        node1.next = node2;
        node2.next = node3;
        node3.next = node4;
        node4.next = node2;
        Node node = EntryNodeOfLoop1(node1);
    }

    public static Node EntryNodeOfLoop(Node pHead) {
        if (pHead == null || pHead.next == null || pHead.next.next == null) return null;
        Node fast = pHead.next.next;
        Node slow = pHead.next;
        /////先判断有没有环
        while (fast != slow) {
            if (fast.next != null && fast.next.next != null) {
                fast = fast.next.next;
                slow = slow.next;
            } else {
                //没有环,返回
                return null;
            }
        }
        //循环出来的话就是有环，且此时fast==slow.
        fast = pHead;
        while (fast != slow) {
            fast = fast.next;
            slow = slow.next;
        }
        return slow;
    }


    //断链法
    public static Node EntryNodeOfLoop1(Node pHead) {
        if (pHead == null || pHead.next == null) return null;
        Node fast = pHead.next;
        Node slow = pHead;
        while (fast != null) {
            slow.next = null;
            slow = fast;
            fast = fast.next;
        }
        return slow;
    }

    //最优解
    public static Node EntryNodeOfLoop3(Node pHead) {
        if (pHead == null || pHead.next == null)
            return null;
        Node p1 = pHead;
        Node p2 = pHead;
        while (p2 != null && p2.next != null) {
            p1 = p1.next;
            p2 = p2.next.next;
            if (p1 == p2) {
                p2 = pHead;
                while (p1 != p2) {
                    p1 = p1.next;
                    p2 = p2.next;
                }
                if (p1 == p2)
                    return p1;
            }
        }
        return null;
    }
}
