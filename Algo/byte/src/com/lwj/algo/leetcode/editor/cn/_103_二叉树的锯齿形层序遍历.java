package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

import java.util.*;

//<p>给你二叉树的根节点 <code>root</code> ，返回其节点值的 <strong>锯齿形层序遍历</strong> 。（即先从左往右，再从右往左进行下一层遍历，以此类推，层与层之间交替进行）。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p> 
//<img alt="" src="https://assets.leetcode.com/uploads/2021/02/19/tree1.jpg" style="width: 277px; height: 302px;" /> 
//<pre>
//<strong>输入：</strong>root = [3,9,20,null,null,15,7]
//<strong>输出：</strong>[[3],[20,9],[15,7]]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>root = [1]
//<strong>输出：</strong>[[1]]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>root = []
//<strong>输出：</strong>[]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中节点数目在范围 <code>[0, 2000]</code> 内</li> 
// <li><code>-100 &lt;= Node.val &lt;= 100</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>广度优先搜索</li><li>二叉树</li></div></div><br><div><li>👍 946</li><li>👎 0</li></div>
class BinaryTreeZigzagLevelOrderTraversal {
    public static void main(String[] args) {
        Solution solution = new BinaryTreeZigzagLevelOrderTraversal().new Solution();
        System.out.println(solution.zigzagLevelOrder(TreeNodeUtil.constructTree(1)));
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
        List<List<Integer>> res = new ArrayList<>();
        Queue<TreeNode> queue = new LinkedList<>();
        boolean isOdd = true;

        public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
            if (root == null) return res;
            queue.add(root);
//            zigzagLevelOrder0();
            zigzagLexvelOrder1();
            return res;
        }

        private void zigzagLexvelOrder1() {
            if (queue.isEmpty()) return;
            ArrayList<Integer> subResList = new ArrayList<>();
            int size = queue.size();
            while (size-- > 0) {
                TreeNode cur = queue.poll();
                if (cur == null) {
                    continue;
                }
                queue.add(cur.left);
                queue.add(cur.right);
                subResList.add(cur.val);
            }

            if (!subResList.isEmpty()) {
                if (isOdd) {
                    res.add(subResList);
                } else {
                    Collections.reverse(subResList);
                    res.add(subResList);
                }
            }
            isOdd = !isOdd;
            zigzagLexvelOrder1();
        }

        public void zigzagLevelOrder0() {
            List<Integer> subRes = new ArrayList<>();
            int size = queue.size();
            if (size == 0) return;
            while (size-- > 0) {
                TreeNode node = queue.poll();
                if (node == null) continue;
                subRes.add(node.val);
                if (node.left != null) queue.add(node.left);
                if (node.right != null) queue.add(node.right);
            }
            if (isOdd) {
                res.add(subRes);
                isOdd = false;
            } else {
                Collections.reverse(subRes);
                res.add(subRes);
                isOdd = true;
            }
            zigzagLevelOrder0();
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}