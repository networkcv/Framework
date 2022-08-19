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
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>动态规划</li><li>二叉树</li></div></div><br><div><li>👍 1400</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

import java.util.HashMap;

class HouseRobberIii {
    public static void main(String[] args) {
        Solution solution = new HouseRobberIii().new Solution();
//        System.out.println(solution.rob(TreeNodeUtil.constructTree(3, 2, 3, null, 3, null, 1))); //7
//        System.out.println(solution.rob(TreeNodeUtil.constructTree(3, 4, 5, 1, 3, null, 1))); //9
        System.out.println(solution.rob(TreeNodeUtil.constructTree(4, 2, null, 1, 3))); //8
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
        //dp 备忘录
        HashMap<TreeNode, Integer> memo = new HashMap<>();

        //dp 备忘录
        public int rob1(TreeNode root) {
            return dp1(root);
        }

        private int dp1(TreeNode root) {
            if (root == null) {
                return 0;
            }
            if (memo.get(root) != null) {
                return memo.get(root);
            }
            int num = Math.max(
                    //抢 当前+隔代最大的金额
                    root.val
                            + (root.left == null ? 0 : dp1(root.left.left) + dp1(root.left.right))
                            + (root.right == null ? 0 : dp1(root.right.left) + dp1(root.right.right)),
                    //不抢
                    dp1(root.left) + dp1(root.right)
            );
            memo.putIfAbsent(root, num);
            return num;
        }

        //不用额外空间的dp
        public int rob(TreeNode root) {
            int[] res = dp(root);
            return Math.max(res[0], res[1]);
        }

        /* 返回一个大小为 2 的数组 arr
        //arr[0] 表示不抢 root 的话，得到的最大钱数
        //arr[1] 表示抢 root 的话，得到的最大钱数 */
        private int[] dp(TreeNode root) {
            if (root == null) {
                return new int[]{0, 0};
            }
            int[] left = dp(root.left);
            int[] right = dp(root.right);
            // 抢，下家就不能抢了
            int rob = root.val + left[0] + right[0];
            // 不抢，下家可抢可不抢，取决于收益大小
            int not_rob = Math.max(left[0], left[1])
                    + Math.max(right[0], right[1]);
            return new int[]{not_rob, rob};
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}