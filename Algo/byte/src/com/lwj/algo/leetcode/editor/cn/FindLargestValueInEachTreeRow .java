//<p>给定一棵二叉树的根节点&nbsp;<code>root</code> ，请找出该二叉树中每一层的最大值。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例1：</strong></p>
//
//<p><img alt="" src="https://assets.leetcode.com/uploads/2020/08/21/largest_e1.jpg" style="height: 172px; width: 300px;" /></p>
//
//<pre>
//<strong>输入: </strong>root = [1,3,2,5,3,null,9]
//<strong>输出: </strong>[1,3,9]
//</pre>
//
//<p><strong>示例2：</strong></p>
//
//<pre>
//<strong>输入: </strong>root = [1,2,3]
//<strong>输出: </strong>[1,3]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>二叉树的节点个数的范围是 <code>[0,10<sup>4</sup>]</code></li> 
// <li>
//  <meta charset="UTF-8" /><code>-2<sup>31</sup>&nbsp;&lt;= Node.val &lt;= 2<sup>31</sup>&nbsp;- 1</code></li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>广度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 271</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import jdk.jfr.Frequency;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class FindLargestValueInEachTreeRow {
    public static void main(String[] args) {
        Solution solution = new FindLargestValueInEachTreeRow().new Solution();
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

        List<Integer> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();

        //二叉树按层级遍历记录每层最大值
        public List<Integer> largestValues(TreeNode root) {
            if (root==null){
                return res;
            }
            queue.add(root);
            traverse();
            return res;
        }

        private void traverse() {
            if (queue.isEmpty()) {
                return;
            }
            int curLevelMax = Integer.MIN_VALUE;
            List<TreeNode> tmpNodeList = new ArrayList<>();
            while (!queue.isEmpty()) {
                TreeNode cur = queue.poll();
                curLevelMax = Math.max(curLevelMax, cur.val);
                tmpNodeList.add(cur);
            }
            res.add(curLevelMax);
            for (TreeNode treeNode : tmpNodeList) {
                if (treeNode.left != null) {
                    queue.add(treeNode.left);
                }
                if (treeNode.right != null) {
                    queue.add(treeNode.right);
                }
            }
            traverse();
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}