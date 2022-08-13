//<p>给定一个&nbsp;<strong>完美二叉树&nbsp;</strong>，其所有叶子节点都在同一层，每个父节点都有两个子节点。二叉树定义如下：</p>
//
//<pre>
//struct Node {
//  int val;
//  Node *left;
//  Node *right;
//  Node *next;
//}</pre>
//
//<p>填充它的每个 next 指针，让这个指针指向其下一个右侧节点。如果找不到下一个右侧节点，则将 next 指针设置为 <code>NULL</code>。</p>
//
//<p>初始状态下，所有&nbsp;next 指针都被设置为 <code>NULL</code>。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<p><img alt="" src="https://assets.leetcode.com/uploads/2019/02/14/116_sample.png" style="height: 171px; width: 500px;" /></p>
//
//<pre>
//<b>输入：</b>root = [1,2,3,4,5,6,7]
//<b>输出：</b>[1,#,2,3,#,4,5,6,7,#]
//<b>解释：</b>给定二叉树如图 A 所示，你的函数应该填充它的每个 next 指针，以指向其下一个右侧节点，如图 B 所示。序列化的输出按层序遍历排列，同一层节点由 next 指针连接，'#' 标志着每一层的结束。
//</pre>
//
//<p>
// <meta charset="UTF-8" /></p>
//
//<p><strong>示例 2:</strong></p>
//
//<pre>
//<b>输入：</b>root = []
//<b>输出：</b>[]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点的数量在
//  <meta charset="UTF-8" />&nbsp;<code>[0, 2<sup>12</sup>&nbsp;- 1]</code>&nbsp;范围内</li> 
// <li><code>-1000 &lt;= node.val &lt;= 1000</code></li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p><strong>进阶：</strong></p>
//
//<ul> 
// <li>你只能使用常量级额外空间。</li> 
// <li>使用递归解题也符合要求，本题中递归程序占用的栈空间不算做额外的空间复杂度。</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>广度优先搜索</li><li>链表</li><li>二叉树</li></div></div><br><div><li>👍 848</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

class PopulatingNextRightPointersInEachNode {
    public static void main(String[] args) {
        Solution solution = new PopulatingNextRightPointersInEachNode().new Solution();
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public Node connect(Node root) {
            if (root == null) {
                return null;
            }
            pointNextNode(root.left, root.right);
            return root;
        }

        private void pointNextNode(Node left, Node right) {
            if (left == null || right == null) {
                return;
            }
            left.next = right;
            pointNextNode(left.left, left.right);
            pointNextNode(left.right, right.left);
            pointNextNode(right.left, right.right);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}

class Node {
    public int val;
    public Node left;
    public Node right;
    public Node next;

    public Node() {
    }

    public Node(int _val) {
        val = _val;
    }

    public Node(int _val, Node _left, Node _right, Node _next) {
        val = _val;
        left = _left;
        right = _right;
        next = _next;
    }
}

