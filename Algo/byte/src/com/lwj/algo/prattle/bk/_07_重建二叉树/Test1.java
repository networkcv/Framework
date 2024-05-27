package com.lwj.algo.prattle.bk._07_重建二叉树;

import com.lwj.algo.prattle.bk._00_Utils.TreeNode;

/**
 * create by lwj on 2019/2/7
 */
public class Test1 {
    public static void main(String[] args) {
        //普通二叉树
        int[] pre = new int[]{1, 2, 4, 7, 3, 5, 6, 8};
        int[] in = new int[]{4, 7, 2, 1, 5, 3, 8, 6};
        //没有右结点的二叉树
//        int[] pre = new int[]{1,2,3,4,5};
//        int[] in = new int[]{5,4,3,2,1};
        //只有一个结点的二叉树
//        int[] pre = new int[]{1};
//        int[] in = new int[]{1};
        //null，前序遍历和中序遍历不匹配的二叉树
//        int[] pre = new int[]{1,2,3,4,5};
//        int[] in = new int[]{5,4,3,1,2};
        TreeNode root = reConstructBinaryTree(pre, 0, pre.length - 1, in, 0, in.length - 1);
        TreeNode.printTree(root);
    }

    private static TreeNode reConstructBinaryTree(int[] pre, int preStart, int preEnd, int[] in, int inStart, int inEnd) {
        if (preStart > preEnd || inStart > inEnd) {
            return null;
        }
        TreeNode root = new TreeNode(pre[preStart]);
        for (int i = inStart; i <= inEnd; i++) {
            if (pre[preStart] == in[i]) {
                //i-inStart是中序遍历里根左边数字的个数，preStart+i-inStart 代表前序遍历子序列的索引最大值
                root.left = reConstructBinaryTree(pre, preStart + 1, preStart + i - inStart, in, inStart, i - 1);
                root.right = reConstructBinaryTree(pre, preStart + i - inStart + 1, preEnd, in, i + 1, inEnd);
            }
        }
        return root;
    }

}
