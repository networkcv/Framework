//<p><strong>路径</strong> 被定义为一条从树中任意节点出发，沿父节点-子节点连接，达到任意节点的序列。同一个节点在一条路径序列中 <strong>至多出现一次</strong> 。该路径<strong> 至少包含一个 </strong>节点，且不一定经过根节点。</p>
//
//<p><strong>路径和</strong> 是路径中各节点值的总和。</p>
//
//<p>给你一个二叉树的根节点 <code>root</code> ，返回其 <strong>最大路径和</strong> 。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/10/13/exx1.jpg" style="width: 322px; height: 182px;" /> 
//<pre>
//<strong>输入：</strong>root = [1,2,3]
//<strong>输出：</strong>6
//<strong>解释：</strong>最优路径是 2 -&gt; 1 -&gt; 3 ，路径和为 2 + 1 + 3 = 6</pre>
//
//<p><strong>示例 2：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/10/13/exx2.jpg" /> 
//<pre>
//<strong>输入：</strong>root = [-10,9,20,null,null,15,7]
//<strong>输出：</strong>42
//<strong>解释：</strong>最优路径是 15 -&gt; 20 -&gt; 7 ，路径和为 15 + 20 + 7 = 42
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点数目范围是 <code>[1, 3 * 10<sup>4</sup>]</code></li> 
// <li><code>-1000 &lt;= Node.val &lt;= 1000</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>动态规划</li><li>二叉树</li></div></div><br><div><li>👍 1680</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

class BinaryTreeMaximumPathSum {
    public static void main(String[] args) {
        Solution solution = new BinaryTreeMaximumPathSum().new Solution();
//        TreeNodeUtil.printTree(TreeNodeUtil.constructTree(9, 6, -3, null, null, -6, 2, null, null, 2, null, -6, -6, -6));
//        System.out.println(solution.maxPathSum(TreeNodeUtil.constructTree(-10, 9, 20, null, null, 15, 7)));
//        System.out.println(solution.maxPathSum(TreeNodeUtil.constructTree(2, -1)));
        TreeNode root = TreeNodeUtil.constructTree(5, 4, 8, 11, null, 13, 4, 7, 2, null, null, null, 1);
        TreeNodeUtil.printTree(root);
        System.out.println(solution.maxPathSum(root));//48
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
        //maxPathSum会记录在遍历过程中所有节点的最大的那个路径和
        int maxPathSum = Integer.MIN_VALUE;

        public int maxPathSum(TreeNode root) {
            maxPathSumFun(root);
            return maxPathSum;
        }

        private int maxPathSumFun(TreeNode root) {
            if (root == null) {
                return 0;
            }
            //拿到两边子树的最大路径和 算出当前节点的最大路径和 如果是负数的话 对最大路径和没有帮助 需要置0
            int maxLeftPathSum = Math.max(maxPathSumFun(root.left), 0);
            int maxRightPathSum = Math.max(maxPathSumFun(root.right), 0);
            
            // 判断在该节点包含左右子树的路径和是否大于当前最大路径和
            maxPathSum = Math.max(maxRightPathSum + maxLeftPathSum + root.val, maxPathSum);
            //这里返回时，需要找到两个子树中路径和最大的那只返回
            return Math.max(maxLeftPathSum, maxRightPathSum) + root.val;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}