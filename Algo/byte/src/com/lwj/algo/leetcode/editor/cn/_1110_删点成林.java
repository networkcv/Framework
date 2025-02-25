package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//<p>给出二叉树的根节点&nbsp;<code>root</code>，树上每个节点都有一个不同的值。</p>
//
//<p>如果节点值在&nbsp;<code>to_delete</code>&nbsp;中出现，我们就把该节点从树上删去，最后得到一个森林（一些不相交的树构成的集合）。</p>
//
//<p>返回森林中的每棵树。你可以按任意顺序组织答案。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<p><strong><img alt="" src="https://assets.leetcode-cn.com/aliyun-lc-upload/uploads/2019/07/05/screen-shot-2019-07-01-at-53836-pm.png" style="height: 150px; width: 237px;" /></strong></p>
//
//<pre>
//<strong>输入：</strong>root = [1,2,3,4,5,6,7], to_delete = [3,5]
//<strong>输出：</strong>[[1,2,null,4],[6],[7]]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>root = [1,2,4,null,3], to_delete = [3]
//<strong>输出：</strong>[[1,2,4]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li>树中的节点数最大为&nbsp;<code>1000</code>。</li> 
// <li>每个节点都有一个介于&nbsp;<code>1</code> 到&nbsp;<code>1000</code>&nbsp;之间的值，且各不相同。</li> 
// <li><code>to_delete.length &lt;= 1000</code></li> 
// <li><code>to_delete</code> 包含一些从&nbsp;<code>1</code> 到&nbsp;<code>1000</code>、各不相同的值。</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>树</li><li>深度优先搜索</li><li>数组</li><li>哈希表</li><li>二叉树</li></div></div><br><div><li>👍 355</li><li>👎 0</li></div>
class DeleteNodesAndReturnForest {
    public static void main(String[] args) {
        //<strong>输入：</strong>root = [1,2,3,4,5,6,7], to_delete = [3,5]
//<strong>输出：</strong>[[1,2,null,4],[6],[7]]
        Solution solution = new DeleteNodesAndReturnForest().new Solution();
        TreeNode root = TreeNodeUtil.constructTree(1, 2, 3, 4, 5, 6, 7);
        TreeNodeUtil.printTree(root);
        List<TreeNode> treeNodes = solution.delNodes(root, new int[]{3, 5});
        treeNodes.forEach(TreeNodeUtil::printTree);
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
        ArrayList<TreeNode> res = new ArrayList<>();
        Set<Integer> delNums;

        public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
            delNums = Arrays.stream(to_delete).boxed().collect(Collectors.toSet());
            if (root == null) return res;
            if (dfs(root) != null) {
                res.add(root);
            }
            return res;
        }

        /**
         * 遍历节点
         * <p>
         * 更新左儿子（右儿子）为递归左儿子（右儿子）的返回值。<p>
         * 如果当前节点被删除，那么就检查左儿子（右儿子）是否被删除，如果没被删除，就加入答案。<p>
         * 如果当前节点被删除，返回空节点，否则返回当前节点。<p>
         * 最后，如果根节点没被删除，把根节点加入答案。<p>
         */
        public TreeNode dfs(TreeNode root) {
            if (root == null) return null;
            root.left = dfs(root.left);
            root.right = dfs(root.right);
            boolean curDel = delNums.contains(root.val);
            if (root.left != null && curDel) {
                res.add(root.left);
            }
            if (root.right != null && curDel) {
                res.add(root.right);
            }
            return curDel ? null : root;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}