package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

//<p>给定二叉搜索树的根结点&nbsp;<code>root</code>，返回值位于范围 <em><code>[low, high]</code></em> 之间的所有结点的值的和。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/11/05/bst1.jpg" style="width: 400px; height: 222px;" /> 
//<pre>
//<strong>输入：</strong>root = [10,5,15,3,7,null,18], low = 7, high = 15
//<strong>输出：</strong>32
//</pre>
//
//<p><strong>示例 2：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/11/05/bst2.jpg" style="width: 400px; height: 335px;" /> 
//<pre>
//<strong>输入：</strong>root = [10,5,15,3,7,13,18,1,null,6], low = 6, high = 10
//<strong>输出：</strong>23
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点数目在范围 <code>[1, 2 * 10<sup>4</sup>]</code> 内</li> 
// <li><code>1 &lt;= Node.val &lt;= 10<sup>5</sup></code></li> 
// <li><code>1 &lt;= low &lt;= high &lt;= 10<sup>5</sup></code></li> 
// <li>所有 <code>Node.val</code> <strong>互不相同</strong></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>二叉搜索树</li><li>二叉树</li></div></div><br><div><li>👍 386</li><li>👎 0</li></div>
class RangeSumOfBst {
    public static void main(String[] args) {
        Solution solution = new RangeSumOfBst().new Solution();
        System.out.println(solution.rangeSumBST(TreeNodeUtil.constructTree(10, 5, 15, 3, 7, null, 18), 7, 15));
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
        public int rangeSumBST(TreeNode root, int low, int high) {
            if (root == null) return 0;
            int val = root.val;
            if (low <= val && val <= high) {
                return rangeSumBST(root.left, low, high) + rangeSumBST(root.right, low, high) + val;
            } else if (low > val) {
                return rangeSumBST(root.right, low, high);
            } else {
                return rangeSumBST(root.left, low, high);
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}