//给你一个链表数组，每个链表都已经按升序排列。 
//
// 请你将所有链表合并到一个升序链表中，返回合并后的链表。 
//
// 
//
// 示例 1： 
//
// 输入：lists = [[1,4,5],[1,3,4],[2,6]]
//输出：[1,1,2,3,4,4,5,6]
//解释：链表数组如下：
//[
//  1->4->5,
//  1->3->4,
//  2->6
//]
//将它们合并到一个有序链表中得到。
//1->1->2->3->4->4->5->6
// 
//
// 示例 2： 
//
// 输入：lists = []
//输出：[]
// 
//
// 示例 3： 
//
// 输入：lists = [[]]
//输出：[]
// 
//
// 
//
// 提示： 
//
// 
// k == lists.length 
// 0 <= k <= 10^4 
// 0 <= lists[i].length <= 500 
// -10^4 <= lists[i][j] <= 10^4 
// lists[i] 按 升序 排列 
// lists[i].length 的总和不超过 10^4 
// 
// Related Topics 链表 分治 堆（优先队列） 归并排序 👍 1872 👎 0


package com.lwj.algo.leetcode.editor.cn;

class MergeKSortedLists {
    public static void main(String[] args) {
        Solution solution = new MergeKSortedLists().new Solution();
        ListNode one1 = new ListNode(1);
        ListNode one2 = new ListNode(3);
        ListNode one3 = new ListNode(5);
        ListNode two1 = new ListNode(1);
        ListNode two2 = new ListNode(2);
        ListNode two3 = new ListNode(6);
        ListNode three1 = new ListNode(4);
        ListNode three2 = new ListNode(5);
        one1.next = one2;
        one2.next = one3;
        two1.next = two2;
        two2.next = two3;
        three1.next = three2;
        ListNode[] listNodes = {one1, two1, three1};
        System.out.println(solution.mergeKLists(listNodes));
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
        public ListNode mergeKLists(ListNode[] lists) {
            if (lists.length == 0) {
                return null;
            }
            if (lists.length == 1) {
                return lists[0];
            }
            //1.迭代合并，将多个链表和简化为多次两个链表的合并 时间复杂度（）
            ListNode res = mergeList(lists[0], lists[1]);
            for (int i = 2; i < lists.length; i++) {
                res = mergeList(res, lists[i]);
            }
            //2.
            return res;
        }

        public ListNode mergeList(ListNode node1, ListNode node2) {
            ListNode res = new ListNode();
            ListNode cur = res;
            while (node1 != null && node2 != null) {
                if (node1.val < node2.val) {
                    cur.next = node1;
                    node1 = node1.next;
                } else {
                    cur.next = node2;
                    node2 = node2.next;
                }
                cur = cur.next;
            }
            cur.next = node1 == null ? node2 : node1;
            return res.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}