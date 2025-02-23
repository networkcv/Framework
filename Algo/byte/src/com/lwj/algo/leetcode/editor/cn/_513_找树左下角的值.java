package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

//<p>给定一个二叉树的 <strong>根节点</strong> <code>root</code>，请找出该二叉树的&nbsp;<strong>最底层&nbsp;最左边&nbsp;</strong>节点的值。</p>
//
//<p>假设二叉树中至少有一个节点。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<p><img src="https://assets.leetcode.com/uploads/2020/12/14/tree1.jpg" style="width: 182px; " /></p>
//
//<pre>
//<strong>输入: </strong>root = [2,1,3]
//<strong>输出: </strong>1
//</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<p><img src="https://assets.leetcode.com/uploads/2020/12/14/tree2.jpg" style="width: 242px; " /><strong> </strong></p>
//
//<pre>
//<strong>输入: </strong>[1,2,3,4,null,5,6,null,null,7]
//<strong>输出: </strong>7
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示:</strong></p>
//
//<ul> 
// <li>二叉树的节点个数的范围是 <code>[1,10<sup>4</sup>]</code></li> 
// <li>
//  <meta charset="UTF-8" /><code>-2<sup>31</sup>&nbsp;&lt;= Node.val &lt;= 2<sup>31</sup>&nbsp;- 1</code>&nbsp;</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>广度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 618</li><li>👎 0</li></div>
class FindBottomLeftTreeValue {
    public static void main(String[] args) {
        Solution solution = new FindBottomLeftTreeValue().new Solution();
        System.out.println(solution.findBottomLeftValue(TreeNodeUtil.constructTree(2, 1, 3)));
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

        //从右向左遍历法
        public int findBottomLeftValue(TreeNode root) {
            queue.add(root);
            TreeNode tmp = root;
            while (!queue.isEmpty()) {
                tmp = queue.poll();
                if (tmp.right != null) queue.add(tmp.right);
                if (tmp.left != null) queue.add(tmp.left);
            }
            return tmp.val;

        }

        Queue<TreeNode> queue = new LinkedList<>();
        Integer res = null;

        public int findBottomLeftValue0(TreeNode root) {
            queue.add(root);
            findBottomLeftValue0();
            return res;
        }

        public void findBottomLeftValue0() {
            int size = queue.size();
            if (size == 0) return;
            List<TreeNode> tmp = new ArrayList<>();
            while (size-- > 0) {
                TreeNode node = queue.poll();
                tmp.add(node);
                if (node == null) continue;
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            if (queue.isEmpty()) {
                res = tmp.get(0).val;
            }
            findBottomLeftValue0();
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}