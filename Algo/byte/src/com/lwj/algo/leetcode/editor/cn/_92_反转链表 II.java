package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;

/**
 * 给你单链表的头指针 head 和两个整数 left 和 right ，其中 left <= right 。请你反转从位置 left 到位置 right 的链表节
 * 点，返回 反转后的链表 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：head = [1,2,3,4,5], left = 2, right = 4
 * 输出：[1,4,3,2,5]
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：head = [5], left = 1, right = 1
 * 输出：[5]
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 链表中节点数目为 n
 * 1 <= n <= 500
 * -500 <= Node.val <= 500
 * 1 <= left <= right <= n
 * <p>
 * <p>
 * <p>
 * <p>
 * 进阶： 你可以使用一趟扫描完成反转吗？
 * <p>
 * Related Topics链表
 * <p>
 * 👍 1913, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class ReverseLinkedListIi {
    public static void main(String[] args) {
        Solution solution = new ReverseLinkedListIi().new Solution();
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

        //反转链表指定区间节点
        public ListNode reverseBetween(ListNode head, int left, int right) {
            if (left == 1) {
                //反转前k个，其实范围就是1～k
                return reverse(head, right);
            }
            //假设left =2 right=k，如果我们把head节点去掉了，那left=2的那个节点 就是变成了新的头节点，
            //那么left也就是变成了1，就变成了反转1～k节点的问题了。
            head.next = reverseBetween(head.next, left - 1, right - 1);
            return head;
        }

        ListNode reverseTailNextNode;

        //反转链表前k个节点
        private ListNode reverse(ListNode head, int k) {
            if (k == 1) {
                reverseTailNextNode = head.next;
                return head;
            }
            ListNode last = reverse(head.next, k - 1);
            head.next.next = head;
            head.next = reverseTailNextNode;
            return last;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}