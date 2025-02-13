package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

/**
 * 给你链表的头节点 head ，每 k 个节点一组进行翻转，请你返回修改后的链表。
 * <p>
 * k 是一个正整数，它的值小于或等于链表的长度。如果节点总数不是 k 的整数倍，那么请将最后剩余的节点保持原有顺序。
 * <p>
 * 你不能只是单纯的改变节点内部的值，而是需要实际进行节点交换。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：head = [1,2,3,4,5], k = 2
 * 输出：[2,1,4,3,5]
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * <p>
 * <p>
 * 输入：head = [1,2,3,4,5], k = 3
 * 输出：[3,2,1,4,5]
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 链表中的节点数目为 n
 * 1 <= k <= n <= 5000
 * 0 <= Node.val <= 1000
 * <p>
 * <p>
 * <p>
 * <p>
 * 进阶：你可以设计一个只用 O(1) 额外内存空间的算法解决此问题吗？
 * <p>
 * <p>
 * <p>
 * <p>
 * Related Topics递归 | 链表
 * <p>
 * 👍 2474, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class ReverseNodesInKGroup {
    public static void main(String[] args) {
        Solution solution = new ReverseNodesInKGroup().new Solution();
        ListNode listNode = ListNodeUtils.buildByArray(1, 2, 3, 4, 5);
        System.out.println(solution.reverseKGroup(listNode, 2));
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
        //2025/2/13
        //参考 https://www.bilibili.com/video/BV1sd4y1x7KN/?spm_id_from=333.788.player.switch&vd_source=1c3ddffa7c4adab111124a27aaa320a6
        public ListNode reverseKGroup(ListNode head, int k) {
            ListNode tmp = head;
            //链表长度
            int len = 0;
            while (tmp != null) {
                len++;
                tmp = tmp.next;
            }
            ListNode dummy = new ListNode(-1);
            dummy.next = head;
            //正在翻转的这组链表的前一个节点
            ListNode p0 = dummy;
            ListNode pre = dummy;
            ListNode cur = head;
            while (len - k >= 0) {
                len = len - k;
                int n = 0;
                //翻转一组链表节点  -1 [1,2,3,4,5], k = 2
                while (n++ < k) {
                    ListNode next = cur.next;
                    cur.next = pre;
                    pre = cur;
                    cur = next;
                }
                p0.next.next = cur;
                ListNode nextP0 = p0.next;
                p0.next = pre;
                p0 = nextP0;
            }
            return dummy.next;

        }

        //2022/7/27
        public ListNode reverseKGroup1(ListNode head, int k) {
            if (head == null) {
                return null;
            }
            ListNode a, b;
            a = b = head;
            for (int i = 0; i < k; i++) {
                // 不足 k 个，不需要反转，base case
                if (b == null)
                    return head;
                b = b.next;
            }
            ListNode newHead = reverse(a, b);
            a.next = reverseKGroup(b, k);
            return newHead;
        }

        /**
         * 反转区间 [a, b) 的元素，注意是左闭右开
         */
        ListNode reverse(ListNode a, ListNode b) {
            ListNode pre, cur, nxt;
            pre = null;
            cur = a;
            nxt = a;
            // while 终止的条件改一下就行了
            while (cur != b) {
                nxt = cur.next;
                cur.next = pre;
                pre = cur;
                cur = nxt;
            }
            // 返回反转后的头结点
            return pre;
        }

        //个人思路
        public ListNode reverseKGroup2(ListNode head, int k) {
            int i = k, j = k;
            ListNode cur = head;
            //找第一组k的节点后边的链表
            while (j-- > 0) {
                // 递归的base case
                if (cur == null)
                    return head;
                cur = cur.next;
            }
            //分解问题的核心
            ListNode pre = reverseKGroup2(cur, k);
            //下边这部分是反转链表的第一组的k个节点
            cur = head;
            ListNode next;
            while (i-- > 0) {
                next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}