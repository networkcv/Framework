//<p>给定一个二叉树，找出其最小深度。</p>
//
//<p>最小深度是从根节点到最近叶子节点的最短路径上的节点数量。</p>
//
//<p><strong>说明：</strong>叶子节点是指没有子节点的节点。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2020/10/12/ex_depth.jpg" style="width: 432px; height: 302px;" /> 
//<pre>
//<strong>输入：</strong>root = [3,9,20,null,null,15,7]
//<strong>输出：</strong>2
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>root = [2,null,3,null,4,null,5,null,6]
//<strong>输出：</strong>5
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点数的范围在 <code>[0, 10<sup>5</sup>]</code> 内</li> 
// <li><code>-1000 &lt;= Node.val &lt;= 1000</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>广度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 819</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

import java.util.LinkedList;
import java.util.Queue;

class MinimumDepthOfBinaryTree {
    public static void main(String[] args) {
        Solution solution = new MinimumDepthOfBinaryTree().new Solution();
        System.out.println(solution.minDepth(TreeNodeUtil.constructTree(3, 9, 20, null, null, 15, 7)));
        TreeNode root = TreeNodeUtil.constructTree(2, null, 3, null, 4, null, 5, null, 6);
        System.out.println(solution.minDepth(root));
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
        //深度优先搜索 depth first search
        public int minDepth1(TreeNode root) {
            if (root == null) {
                return 0;
            }
            if (root.left == null && root.right == null) {
                return 1;
            }
            int minDepth = Integer.MAX_VALUE;
            if (root.left != null) {
                minDepth = Math.min(minDepth, minDepth1(root.left));
            }
            if (root.right != null) {
                minDepth = Math.min(minDepth, minDepth1(root.right));
            }
            return minDepth + 1;
        }

        //广度优先搜索 breadth first search
        public int minDepth(TreeNode root) {
            if (root == null) {
                return 0;
            }
            int depth = 1;
            Queue<TreeNode> queue = new LinkedList<>();
            queue.add(root);
            while (!queue.isEmpty()) {
                int size = queue.size();
                for (int i = 0; i < size; i++) {
                    TreeNode cur = queue.poll();
                    if (cur.left == null && cur.right == null) {
                        return depth;
                    }
                    if (cur.left != null) {
                        queue.offer(cur.left);
                    }
                    if (cur.right != null) {
                        queue.offer(cur.right);
                    }
                }
                depth++;
            }
            return depth;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}