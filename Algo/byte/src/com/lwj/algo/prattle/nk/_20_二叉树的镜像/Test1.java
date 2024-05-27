package com.lwj.algo.prattle.nk._20_二叉树的镜像;

import com.lwj.algo.prattle.nk._0_Utils.TreeNode;

/**
 * create by lwj on 2018/10/16
 */
public class Test1 {
//    操作给定的二叉树，将其变换为源二叉树的镜像。
//    二叉树的镜像定义：源二叉树
//    	                8
//                    /  \
//                    6   10
//                    / \  / \
//                    5  7 9 11
//    镜像二叉树
//    	                8
//                    /  \
//                    10   6
//                    / \  / \
//                    11 9 7  5

    public static void main(String[] args) {
        TreeNode root1 = new TreeNode(8);
        root1.right = new TreeNode(7);
        root1.left = new TreeNode(8);
        root1.left.left = new TreeNode(9);
        root1.left.right = new TreeNode(2);
        root1.left.right.left = new TreeNode(4);
        root1.left.right.right = new TreeNode(7);
//          8                      8
//      8         7             8      7
//  9      2
//      4    7
        TreeNode root2 = new TreeNode(1);
        TreeNode.printTree(root2);
        Mirror(root2);
        System.out.println();
        TreeNode.printTree(root2);
    }

    public static void Mirror(TreeNode root) {
        if(root!=null){
            TreeNode tmp;
            tmp=root.left;
            root.left=root.right;
            root.right=tmp;
            Mirror(root.left);
            Mirror(root.right);
        }

    }
}
