package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;

//<p>给定二叉搜索树（BST）的根节点
// <meta charset="UTF-8" />&nbsp;<code>root</code>&nbsp;和一个整数值
// <meta charset="UTF-8" />&nbsp;<code>val</code>。</p>
//
//<p>你需要在 BST 中找到节点值等于&nbsp;<code>val</code>&nbsp;的节点。 返回以该节点为根的子树。 如果节点不存在，则返回
// <meta charset="UTF-8" />&nbsp;<code>null</code>&nbsp;。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<p><img alt="" src="https://assets.leetcode.com/uploads/2021/01/12/tree1.jpg" style="height: 179px; width: 250px;" />
// <meta charset="UTF-8" /></p>
//
//<pre>
//<b>输入：</b>root = [4,2,7,1,3], val = 2
//<b>输出：</b>[2,1,3]
//</pre>
//
//<p><strong>示例 2:</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2021/01/12/tree2.jpg" style="height: 179px; width: 250px;" /> 
//<pre>
//<b>输入：</b>root = [4,2,7,1,3], val = 5
//<b>输出：</b>[]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点数在&nbsp;<code>[1, 5000]</code>&nbsp;范围内</li> 
// <li><code>1 &lt;= Node.val &lt;= 10<sup>7</sup></code></li> 
// <li><code>root</code>&nbsp;是二叉搜索树</li> 
// <li><code>1 &lt;= val &lt;= 10<sup>7</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>二叉搜索树</li><li>二叉树</li></div></div><br><div><li>👍 494</li><li>👎 0</li></div>
class SearchInABinarySearchTree {
    public static void main(String[] args) {
        Solution solution = new SearchInABinarySearchTree().new Solution();
        System.out.println(solution);
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
        public TreeNode searchBST(TreeNode root, int val) {
            if (root == null) return null;
            if (root.val == val) return root;
            //合理利用二叉搜索树的性质
            return searchBST(val > root.val ? root.right : root.left, val);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}