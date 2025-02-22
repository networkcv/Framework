package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

//<p>给定一个二叉树，判断它是否是 <span data-keyword="height-balanced">平衡二叉树</span> &nbsp;</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/10/06/balance_1.jpg" style="width: 342px; height: 221px;" /> 
//<pre>
//<strong>输入：</strong>root = [3,9,20,null,null,15,7]
//<strong>输出：</strong>true
//</pre>
//
//<p><strong>示例 2：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/10/06/balance_2.jpg" style="width: 452px; height: 301px;" /> 
//<pre>
//<strong>输入：</strong>root = [1,2,2,3,3,null,null,4,4]
//<strong>输出：</strong>false
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>root = []
//<strong>输出：</strong>true
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中的节点数在范围 <code>[0, 5000]</code> 内</li> 
// <li><code>-10<sup>4</sup> &lt;= Node.val &lt;= 10<sup>4</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 1579</li><li>👎 0</li></div>
class BalancedBinaryTree {
    public static void main(String[] args) {
        Solution solution = new BalancedBinaryTree().new Solution();
        TreeNode root = TreeNodeUtil.constructTree(1, 2, 2, 3, null, null, 3, 4, null, null, 4);
        TreeNodeUtil.printTree(root);
        System.out.println(solution.isBalanced(root));
    }

    //leetcode submit region begin(Prohibit modification and deletion)

    /**
     * Definition for a binary tree node.
     * public class TreeNode {
     * int val;
     * TreeNode left;
     * TreeNode right;
     * TreeNode() {}
     * TreeNode(int val) { this.val = val; }
     * TreeNode(int val, TreeNode left, TreeNode right) {
     * this.val = val;
     * this.left = left;
     * this.right = right;
     * }
     * }
     */
    class Solution {
        public boolean isBalanced(TreeNode root) {
            if (root == null) return true;
            return isBalanced(root.left) && isBalanced(root.right) &&
                    Math.abs(getTreeHeight(root.left) - getTreeHeight(root.right)) <= 1;
        }

        public int getTreeHeight(TreeNode root) {
            if (root == null) return 0;
            return 1 + Math.max(getTreeHeight(root.left), getTreeHeight(root.right));
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}