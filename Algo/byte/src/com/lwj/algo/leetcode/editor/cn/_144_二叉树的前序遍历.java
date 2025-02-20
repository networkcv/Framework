package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你二叉树的根节点 root ，返回它节点值的 前序 遍历。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：root = [1,null,2,3]
 * <p>
 * <p>
 * 输出：[1,2,3]
 * <p>
 * 解释：
 * <p>
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：root = [1,2,3,4,5,null,8,null,null,6,7,9]
 * <p>
 * <p>
 * 输出：[1,2,4,5,6,7,3,8,9]
 * <p>
 * 解释：
 * <p>
 * <p>
 * <p>
 * 示例 3：
 * <p>
 * <p>
 * 输入：root = []
 * <p>
 * <p>
 * 输出：[]
 * <p>
 * 示例 4：
 * <p>
 * <p>
 * 输入：root = [1]
 * <p>
 * <p>
 * 输出：[1]
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 树中节点数目在范围 [0, 100] 内
 * -100 <= Node.val <= 100
 * <p>
 * <p>
 * <p>
 * <p>
 * 进阶：递归算法很简单，你可以通过迭代算法完成吗？
 * <p>
 * Related Topics栈 | 树 | 深度优先搜索 | 二叉树
 * <p>
 * 👍 1315, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class BinaryTreePreorderTraversal {
    public static void main(String[] args) {
        Solution solution = new BinaryTreePreorderTraversal().new Solution();
        TreeNode treeNode = TreeNodeUtil.constructTree(1, null, 2, 3);
        TreeNodeUtil.printTree(treeNode);
        System.out.println(solution.preorderTraversal(treeNode));
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

        public List<Integer> preorderTraversal(TreeNode root) {
            if (root == null) return res;
            res.add(root.val);
            preorderTraversal(root.left);
            preorderTraversal(root.right);
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}