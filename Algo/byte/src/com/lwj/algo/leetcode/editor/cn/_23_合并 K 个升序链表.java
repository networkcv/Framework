package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;

import java.util.Comparator;
import java.util.PriorityQueue;

class MergeKSortedLists {
    public static void main(String[] args) {
        Solution solution = new MergeKSortedLists().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for singly-linked list.
     * public class ListNode {
     * int val;
     * ListNode next;
     * ListNode() {}
     * ListNode(int val) { this.val = val; }
     * ListNode(int val, ListNode next) { this.val = val; this.next = next; }
     * }
     */
    class Solution {
        //使用最小堆合并排序头节点
        public ListNode mergeKLists(ListNode[] lists) {
            PriorityQueue<ListNode> heap = new PriorityQueue<>(Comparator.comparingInt(l -> l.val));
            for (ListNode head : lists) {
                if (head != null) heap.add(head);
            }
            ListNode dummyHead = new ListNode(0);
            ListNode cur = dummyHead;
            while (!heap.isEmpty()) {
                ListNode minNode = heap.poll();
                ListNode next = minNode.next;
                if (next != null) {
                    heap.add(next);
                }
                cur.next = minNode;
                cur = cur.next;
            }
            return dummyHead.next;
        }

        //使用最小堆直接合并排序所有节点
        public ListNode mergeKLists0(ListNode[] lists) {
            PriorityQueue<ListNode> heap = new PriorityQueue<>(Comparator.comparingInt(l -> l.val));
            for (ListNode listNode : lists) {
                ListNode cur = listNode;
                while (cur != null) {
                    heap.add(cur);
                    cur = cur.next;
                }
            }
            ListNode dummyHead = new ListNode(0);
            ListNode cur = dummyHead;
            while (!heap.isEmpty()) {
                cur.next = new ListNode(heap.poll().val);
                cur = cur.next;
            }
            return dummyHead.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}