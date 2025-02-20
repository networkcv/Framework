package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

/**
 * 给定一个单链表 L 的头节点 head ，单链表 L 表示为：
 * <p>
 * <p>
 * L0 → L1 → … → Ln - 1 → Ln
 * <p>
 * <p>
 * 请将其重新排列后变为：
 * <p>
 * <p>
 * L0 → Ln → L1 → Ln - 1 → L2 → Ln - 2 → …
 * <p>
 * 不能只是单纯的改变节点内部的值，而是需要实际的进行节点交换。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * <p>
 * <p>
 * 输入：head = [1,2,3,4]
 * 输出：[1,4,2,3]
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * <p>
 * <p>
 * 输入：head = [1,2,3,4,5]
 * 输出：[1,5,2,4,3]
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 链表的长度范围为 [1, 5 * 10⁴]
 * 1 <= node.val <= 1000
 * <p>
 * <p>
 * Related Topics栈 | 递归 | 链表 | 双指针
 * <p>
 * 👍 1561, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class ReorderList {
    public static void main(String[] args) {
        ReorderList.Solution solution = new ReorderList().new Solution();
//        ListNode head = ListNodeUtils.build(12345);
        ListNode head = ListNodeUtils.build(123456);
        System.out.println(head);
        solution.reorderList(head);
        System.out.println(head);
    }
//    }

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
        public void reorderList(ListNode head) {
            ListNode q = head;
            ListNode s = head;
            while (q != null && q.next != null) {
                q = q.next.next;
                s = s.next;
            }
            ListNode pre = null;
            ListNode cur = s;
            while (cur != null) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            ListNode head2 = pre;
            if (head2 == null) return;
            while (head2 != null) {
                ListNode next = head.next;
                ListNode next2 = head2.next;
                head2.next = next;
                head.next = head2;
                head = next;
                head2 = next2;
            }

        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}
