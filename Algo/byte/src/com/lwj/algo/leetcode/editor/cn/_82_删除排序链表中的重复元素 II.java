package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

/**
 * 给定一个已排序的链表的头 head ， 删除原始链表中所有重复数字的节点，只留下不同的数字 。返回 已排序的链表 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：head = [1,2,3,3,4,4,5]
 * 输出：[1,2,5]
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：head = [1,1,1,2,3]
 * 输出：[2,3]
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 链表中节点数目在范围 [0, 300] 内
 * -100 <= Node.val <= 100
 * 题目数据保证链表已经按升序 排列
 * <p>
 * <p>
 * Related Topics链表 | 双指针
 * <p>
 * 👍 1338, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class RemoveDuplicatesFromSortedListIi {
    public static void main(String[] args) {
        Solution solution = new RemoveDuplicatesFromSortedListIi().new Solution();
//        System.out.println(solution.deleteDuplicates(ListNodeUtils.build(11)));
//        System.out.println(solution.deleteDuplicates(ListNodeUtils.buildByArray(1, 2, 3, 3, 4, 4, 5)));
        System.out.println(solution.deleteDuplicates(ListNodeUtils.build(11)));
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

        //出现重复的元素全部删除
        public ListNode deleteDuplicates(ListNode head) {
            if (head == null || head.next == null) return head;
            ListNode dummy = new ListNode();
            dummy.next = head;
            ListNode pre = dummy;
            ListNode cur = head;
            while (cur != null && cur.next != null) {
                int val = cur.val;
                boolean delFlag = cur.val == cur.next.val;
                if (delFlag) {
                    //遍历一个删除一个
                    while (cur != null && cur.val == val) {
                        pre.next = cur.next;
                        cur = cur.next;
                    }
                } else {
                    pre = cur;
                    cur = cur.next;
                }
            }
            return dummy.next;
        }

        public ListNode deleteDuplicates0(ListNode head) {
            ListNode dummy = new ListNode();
            dummy.next = head;
            ListNode pre = dummy;
            ListNode cur = head;
            while (cur != null) {
                ListNode next = cur.next;
                boolean delFlag = next != null && cur.val == cur.next.val;
                //遍历到不是重复元素值的位置，一次性删除
                while (next != null && next.val == cur.val) {
                    next = next.next;
                }
                if (delFlag) {
                    pre.next = next;
                } else {
                    pre = cur;
                }
                cur = next;
            }
            return dummy.next;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}