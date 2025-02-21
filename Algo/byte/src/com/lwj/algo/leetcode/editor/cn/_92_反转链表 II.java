package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

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
        ListNode listNode = ListNodeUtils.buildByArray(1, 2, 3, 4, 5);
        System.out.println(solution.reverseBetween(listNode, 2, 4));
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

        // 2025/2/21
        public ListNode reverseBetween(ListNode head, int left, int right) {
            ListNode dummy = new ListNode();
            dummy.next = head;
            int i = 0;
            //开始反转的前一个节点
            ListNode po = dummy;
            while (i++ < left - 1) {
                po = po.next;
            }
            int n = right - left + 1;
            ListNode pre = null;
            ListNode cur = po.next;
            while (n-- > 0) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            po.next.next = cur;
            po.next = pre;
            return dummy.next;
        }

        // 2025/2/13
        public ListNode reverseBetween2(ListNode head, int left, int right) {
            int i = 0;
            ListNode dummy = new ListNode();
            dummy.next = head;
            ListNode po = dummy;
            while (i++ < left - 1) {
                po = po.next;
            }
            i = 0;
            ListNode pre = po;
            ListNode cur = po.next;
            while (i++ < (right - left + 1)) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            po.next.next = cur;
            po.next = pre;
            return dummy.next;
        }

        // 2025/2/13
        //head = [1,2,3,4,5], left = 2, right = 4 输出：[1,4,3,2,5]
        public ListNode reverseBetween1(ListNode head, int left, int right) {
            ListNode dummy = new ListNode();
            ListNode pre = dummy;
            dummy.next = head;
            ListNode cur = head;
            int count = 1;
            //此处可以优化
            while (count < left) {
                count++;
                ListNode next = cur.next;
                pre = cur;
                cur = next;
            }
            //这里边保留反转前pre的指向 这里pre指向的是1，1这个节点指向2
            ListNode po = pre;
            while (count < right + 1) {
                count++;
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            //反转结束之后 cur指向5 pre指向4，
            po.next.next = cur;
            po.next = pre;
            return dummy.next;
        }


        //历史
        //反转链表指定区间节点
        public ListNode reverseBetween0(ListNode head, int left, int right) {
            if (left == 1) {
                //反转前k个，其实范围就是1～k
                return reverse(head, right);
            }
            //假设left =2 right=k，如果我们把head节点去掉了，那left=2的那个节点 就是变成了新的头节点，
            //那么left也就是变成了1，就变成了反转1～k节点的问题了。
            head.next = reverseBetween0(head.next, left - 1, right - 1);
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