package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 给你二叉树的根节点 root ，返回其节点值的 层序遍历 。 （即逐层地，从左到右访问所有节点）。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：root = [3,9,20,null,null,15,7]
 * 输出：[[3],[9,20],[15,7]]
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：root = [1]
 * 输出：[[1]]
 * <p>
 * <p>
 * 示例 3：
 * <p>
 * <p>
 * 输入：root = []
 * 输出：[]
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 树中节点数目在范围 [0, 2000] 内
 * -1000 <= Node.val <= 1000
 * <p>
 * <p>
 * Related Topics树 | 广度优先搜索 | 二叉树
 * <p>
 * 👍 2069, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class BinaryTreeLevelOrderTraversal {
    public static void main(String[] args) {
        Solution solution = new BinaryTreeLevelOrderTraversal().new Solution();
        TreeNode root = TreeNodeUtil.constructTree(3, 9, 20, null, null, 15, 7);
        System.out.println(solution.levelOrder(root));
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

        public List<List<Integer>> levelOrder(TreeNode root) {
            queue.add(root);
            bfs();
            return res;
        }

        public void bfs() {
            int size = queue.size();
            if (size == 0) return;
            List<Integer> subList = new ArrayList<>();
            while (size-- > 0) {
                TreeNode cur = queue.poll();
                if (cur == null) continue;
                queue.add(cur.left);
                queue.add(cur.right);
                subList.add(cur.val);
            }
            if (!subList.isEmpty()) {
                res.add(subList);
            }
            bfs();
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}