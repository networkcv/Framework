package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

//<p>给你一棵二叉树的根节点，返回该树的 <strong>直径</strong> 。</p>
//
//<p>二叉树的 <strong>直径</strong> 是指树中任意两个节点之间最长路径的 <strong>长度</strong> 。这条路径可能经过也可能不经过根节点 <code>root</code> 。</p>
//
//<p>两节点之间路径的 <strong>长度</strong> 由它们之间边数表示。</p>
//
//<p>&nbsp;</p>
//
//<p><strong class="example">示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2021/03/06/diamtree.jpg" style="width: 292px; height: 302px;" /> 
//<pre>
//<strong>输入：</strong>root = [1,2,3,4,5]
//<strong>输出：</strong>3
//<strong>解释：</strong>3 ，取路径 [4,2,1,3] 或 [5,2,1,3] 的长度。
//</pre>
//
//<p><strong class="example">示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>root = [1,2]
//<strong>输出：</strong>1
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点数目在范围 <code>[1, 10<sup>4</sup>]</code> 内</li> 
// <li><code>-100 &lt;= Node.val &lt;= 100</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 1669</li><li>👎 0</li></div>
class DiameterOfBinaryTree {
    public static void main(String[] args) {
        Solution solution = new DiameterOfBinaryTree().new Solution();
//        TreeNode root = TreeNodeUtil.constructTree(1, 2, 3, 4, 5);
        TreeNode root = TreeNodeUtil.constructTree(1, 2, 3);
        TreeNodeUtil.printTree(root);
        System.out.println(solution.diameterOfBinaryTree(root));
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
        int res = 0;

        public int diameterOfBinaryTree(TreeNode root) {
            dfs(root);
            return res;
        }

        //返回当前节点左右子树中到当前节点的最大直径
        public int dfs(TreeNode root) {
            if (root == null) return -1;
            int l = dfs(root.left);
            int r = dfs(root.right);
            res = Math.max(res, l + r + 2);
            return Math.max(l, r) + 1;
        }

        public int dfs0(TreeNode root) {
            if (root == null) return -1;
            int l = dfs0(root.left) + 1;
            int r = dfs0(root.right) + 1;
            res = Math.max(res, l + r);
            return Math.max(l, r);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}