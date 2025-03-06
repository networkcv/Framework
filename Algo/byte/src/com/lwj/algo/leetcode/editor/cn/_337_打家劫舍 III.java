package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;

//<p>小偷又发现了一个新的可行窃的地区。这个地区只有一个入口，我们称之为
// <meta charset="UTF-8" />&nbsp;<code>root</code>&nbsp;。</p>
//
//<p>除了
// <meta charset="UTF-8" />&nbsp;<code>root</code>&nbsp;之外，每栋房子有且只有一个“父“房子与之相连。一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。 如果 <strong>两个直接相连的房子在同一天晚上被打劫</strong> ，房屋将自动报警。</p>
//
//<p>给定二叉树的&nbsp;<code>root</code>&nbsp;。返回&nbsp;<em><strong>在不触动警报的情况下</strong>&nbsp;，小偷能够盗取的最高金额</em>&nbsp;。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<p><img alt="" src="https://assets.leetcode.com/uploads/2021/03/10/rob1-tree.jpg" /></p>
//
//<pre>
//<strong>输入: </strong>root = [3,2,3,null,3,null,1]
//<strong>输出:</strong> 7 
//<strong>解释:</strong>&nbsp;小偷一晚能够盗取的最高金额 3 + 3 + 1 = 7</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<p><img alt="" src="https://assets.leetcode.com/uploads/2021/03/10/rob2-tree.jpg" /></p>
//
//<pre>
//<strong>输入: </strong>root = [3,4,5,1,3,null,1]
//<strong>输出:</strong> 9
//<strong>解释:</strong>&nbsp;小偷一晚能够盗取的最高金额 4 + 5 = 9
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<p>
// <meta charset="UTF-8" /></p>
//
//<ul> 
// <li>树的节点数在&nbsp;<code>[1, 10<sup>4</sup>]</code> 范围内</li> 
// <li><code>0 &lt;= Node.val &lt;= 10<sup>4</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>动态规划</li><li>二叉树</li></div></div><br><div><li>👍 2057</li><li>👎 0</li></div>
class HouseRobberIii {
    public static void main(String[] args) {
        Solution solution = new HouseRobberIii().new Solution();
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
        public int rob(TreeNode root) {
            int[] res = dfs(root);
            return Math.max(res[0], res[1]);
        }

        //返回包含两个值的数组 arr[0] 表示偷当前节点返回的最大金额  arr[1] 表示不偷当前节点返回的最大金额
        public int[] dfs(TreeNode root) {
            if (root == null) return new int[]{0, 0};
            int[] lm = dfs(root.left);
            int[] rm = dfs(root.right);

            int[] res = new int[2];
            //偷当前节点 子节点则不能偷
            res[0] = root.val + lm[1] + rm[1];
            //不偷当前节点 则子节点可偷可不偷
            res[1] = Math.max(lm[0], lm[1]) + Math.max(rm[0], rm[1]);
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}