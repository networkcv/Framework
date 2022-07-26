//给你一个链表的头节点 head 和一个特定值 x ，请你对链表进行分隔，使得所有 小于 x 的节点都出现在 大于或等于 x 的节点之前。 
//
// 你应当 保留 两个分区中每个节点的初始相对位置。 
//
// 
//
// 示例 1： 
//
// 
//输入：head = [1,4,3,2,5,2], x = 3
//输出：[1,2,2,4,3,5]
// 
//
// 示例 2： 
//
// 
//输入：head = [2,1], x = 2
//输出：[1,2]
// 
//
// 
//
// 提示： 
//
// 
// 链表中节点的数目在范围 [0, 200] 内 
// -100 <= Node.val <= 100 
// -200 <= x <= 200 
// 
// Related Topics 链表 双指针 👍 595 👎 0


package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

class PartitionList {
    public static void main(String[] args) {
        Solution solution = new PartitionList().new Solution();
        System.out.println(solution.partition(ListNodeUtils.build(143252), 3));
//        System.out.println(solution.partition2(ListNodeUtils.get(143252), 3));
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

        /**
         * 每次会创建新节点，占用额外空间
         */
        public ListNode partition2(ListNode head, int x) {
            if (head == null) {
                return null;
            }
            ListNode dummyMin = new ListNode(0);
            ListNode dummyMax = new ListNode(0);
            ListNode min = dummyMin;
            ListNode max = dummyMax;
            while (head != null) {
                if (head.val < x) {
                    min.next = new ListNode(head.val);
                    min = min.next;
                } else {
                    max.next = new ListNode(head.val);
                    max = max.next;
                }
                head = head.next;
            }
            min.next = dummyMax.next;
            return dummyMin.next;
        }

        /**
         * 不占用额外空间
         */
        public ListNode partition(ListNode head, int x) {
            if (head == null) {
                return null;
            }
            ListNode dummyMin = new ListNode(0);
            ListNode dummyMax = new ListNode(0);
            ListNode min = dummyMin;
            ListNode max = dummyMax;
            while (head != null) {
                if (head.val < x) {
                    min.next = head;
                    min = min.next;
                } else {
                    max.next = head;
                    max = max.next;
                }
                head = head.next;
            }
            min.next = null;
            max.next = null;
            min.next = dummyMax.next;
            return dummyMin.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}