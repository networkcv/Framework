//给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过也可能不穿过根结点。 
//
// 
//
// 示例 : 
//给定二叉树 
//
//           1
//         / \
//        2   3
//       / \     
//      4   5    
// 
//
// 返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。 
//
// 
//
// 注意：两结点之间的路径长度是以它们之间边的数目表示。 
// Related Topics 树 深度优先搜索 二叉树 👍 1017 👎 0


package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

class DiameterOfBinaryTree {
    public static void main(String[] args) {
        Solution solution = new DiameterOfBinaryTree().new Solution();
        TreeNode root = TreeNodeUtil.constructTree(new Integer[]{4, -7, -3, null, null, -9, -3, 9, -7, -4, null, 6, null, -6, -6, null, null, 0, 6, 5, null, 9, null, null, -1, -4, null, null, null, -2});
        TreeNodeUtil.printTree(root);
        System.out.println(solution.maxDepth(root));
        System.out.println(solution.diameterOfBinaryTree(root));
    }
    //leetcode submit region begin(Prohibit modification and deletion)

    class Solution {
        //二叉树的最长直径是一个节点下，两个子树的最大深度之和，注意最大路径不一定会穿过根节点，所以下边的方法是错误的
        public int error(TreeNode root) {
            if (root == null) {
                return 0;
            }
            int leftMax = maxDepth(root.left);
            int rightMax = maxDepth(root.right);

            return leftMax + rightMax;
        }

        public int maxDepth(TreeNode node) {
            if (node == null) {
                return 0;
            }
            int l = maxDepth(node.left);
            int r = maxDepth(node.right);
            int max = Math.max(l, r);
            return max + 1;
        }

        int maxDiameter = 0;

        //正确
        public int diameterOfBinaryTree(TreeNode root) {
            maxDiameterFun(root);
            return maxDiameter;
        }

        private int maxDiameterFun(TreeNode node) {
            if (node == null) {
                return 0;
            }
            int l = maxDiameterFun(node.left);
            int r = maxDiameterFun(node.right);
            int curMax = l + r;
            maxDiameter = Math.max(curMax, maxDiameter);
            return Math.max(l, r) + 1;
        }


    }
//leetcode submit region end(Prohibit modification and deletion)

}