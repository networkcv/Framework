package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;
import com.lwj.algo.leetcode.editor.cn.utils.ListNodeUtils;

/**
 * <p>给你一个链表的头节点 <code>head</code> 。</p>
 *
 * <p>移除每个右侧有一个更大数值的节点。</p>
 *
 * <p>返回修改后链表的头节点<em> </em><code>head</code><em> </em>。</p>
 *
 * <p>&nbsp;</p>
 *
 * <p><strong>示例 1：</strong></p>
 *
 * <p><img alt="" src="https://assets.leetcode.com/uploads/2022/10/02/drawio.png" style="width: 631px; height: 51px;" /></p>
 *
 * <pre>
 * <strong>输入：</strong>head = [5,2,13,3,8]
 * <strong>输出：</strong>[13,8]
 * <strong>解释：</strong>需要移除的节点是 5 ，2 和 3 。
 * - 节点 13 在节点 5 右侧。
 * - 节点 13 在节点 2 右侧。
 * - 节点 8 在节点 3 右侧。
 * </pre>
 *
 * <p><strong>示例 2：</strong></p>
 *
 * <pre>
 * <strong>输入：</strong>head = [1,1,1,1]
 * <strong>输出：</strong>[1,1,1,1]
 * <strong>解释：</strong>每个节点的值都是 1 ，所以没有需要移除的节点。
 * </pre>
 *
 * <p>&nbsp;</p>
 *
 * <p><strong>提示：</strong></p>
 *
 * <ul>
 * <li>给定列表中的节点数目在范围 <code>[1, 10<sup>5</sup>]</code> 内</li>
 * <li><code>1 &lt;= Node.val &lt;= 10<sup>5</sup></code></li>
 * </ul>
 *
 * <div><div>Related Topics</div><div><li>栈</li><li>递归</li><li>链表</li><li>单调栈</li></div></div><br><div><li>👍 124</li><li>👎 0</li></div>
 */
class RemoveNodesFromLinkedList {
    public static void main(String[] args) {
        Solution solution = new RemoveNodesFromLinkedList().new Solution();
        System.out.println(solution.removeNodes(ListNodeUtils.buildByArray(493, 998, 112, 660, 961, 943, 721, 480, 522, 133, 129, 276, 362, 616, 52, 117, 300, 274, 862, 487, 715, 272, 232, 543, 275, 68, 144, 656, 623, 317, 63, 908, 565, 880, 12, 920, 467, 559, 91, 698)));
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
        public ListNode removeNodes(ListNode head) {
            if (head == null) return null;
            if (head.next == null) return head;

            ListNode rightMaxNode = removeNodes(head.next);
            if (head.val < rightMaxNode.val) {
                return rightMaxNode;
            } else {
                head.next = rightMaxNode;
                return head;
            }
        }

        public ListNode removeNodes2(ListNode head) {
            if (head == null) return null;
            ListNode pre = null;
            ListNode cur = head;
            while (cur != null) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            ListNode newHead = pre;
            cur = newHead.next;
            int rightMaxNum = newHead.val;
            while (cur != null) {
                ListNode next = cur.next;
                if (cur.val < rightMaxNum) {
                    pre.next = next;
                } else {
                    pre = cur;
                }
                rightMaxNum = Math.max(cur.val, rightMaxNum);
                cur = next;
            }
            pre = null;
            cur = newHead;
            while (cur != null) {
                ListNode next = cur.next;
                cur.next = pre;
                pre = cur;
                cur = next;
            }
            return pre;
        }

        //暴力法
        public ListNode removeNodes3(ListNode head) {
            ListNode dummy = new ListNode();
            dummy.next = head;
            ListNode pre = dummy;
            ListNode cur = head;
            while (cur.next != null) {
                ListNode next = cur.next;
                if (travelListReturnCompare(next, cur.val)) {
                    pre.next = next;
                } else {
                    pre = cur;
                }
                cur = next;
            }
            return dummy.next;
        }

        private boolean travelListReturnCompare(ListNode head, Integer target) {
            ListNode cur = head;
            while (cur != null) {
                if (cur.val > target) {
                    return true;
                }
                cur = cur.next;
            }
            return false;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}