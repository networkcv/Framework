package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ListNode;

import java.util.Comparator;
import java.util.PriorityQueue;

//<p>给你一个链表数组，每个链表都已经按升序排列。</p>
//
//<p>请你将所有链表合并到一个升序链表中，返回合并后的链表。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre><strong>输入：</strong>lists = [[1,4,5],[1,3,4],[2,6]]
//<strong>输出：</strong>[1,1,2,3,4,4,5,6]
//<strong>解释：</strong>链表数组如下：
//[
//  1-&gt;4-&gt;5,
//  1-&gt;3-&gt;4,
//  2-&gt;6
//]
//将它们合并到一个有序链表中得到。
//1-&gt;1-&gt;2-&gt;3-&gt;4-&gt;4-&gt;5-&gt;6
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre><strong>输入：</strong>lists = []
//<strong>输出：</strong>[]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre><strong>输入：</strong>lists = [[]]
//<strong>输出：</strong>[]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>k == lists.length</code></li> 
// <li><code>0 &lt;= k &lt;= 10^4</code></li> 
// <li><code>0 &lt;= lists[i].length &lt;= 500</code></li> 
// <li><code>-10^4 &lt;= lists[i][j] &lt;= 10^4</code></li> 
// <li><code>lists[i]</code> 按 <strong>升序</strong> 排列</li> 
// <li><code>lists[i].length</code> 的总和不超过 <code>10^4</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>链表</li><li>分治</li><li>堆（优先队列）</li><li>归并排序</li></div></div><br><div><li>👍 2971</li><li>👎 0</li></div>
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