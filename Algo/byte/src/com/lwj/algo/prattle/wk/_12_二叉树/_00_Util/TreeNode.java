package com.lwj.algo.prattle.wk._12_二叉树._00_Util;

/**
 * create by lwj on 2018/10/9
 */
public class TreeNode {
    public int val = 0;
    public TreeNode left = null;
    public TreeNode right = null;
    public TreeNode(){
    }
    public TreeNode(int val) {
        this.val = val;

    }
    public static void printTree(TreeNode root) {
        if (root != null) {
            printTree(root.left);
            System.out.print(root.val + " ");
            printTree(root.right);
        }
    }

}
