package com.lwj.algo.prattle.nk._6_重建二叉树;

import com.lwj.algo.prattle.nk._0_Utils.TreeNode;

/**
 * create by lwj on 2018/9/30
 */

public class Test2 {
    public static void main(String[] args) {
        int[] pre = new int[]{1, 2, 4, 7, 3, 5, 6, 8};
        int[] in = new int[]{4, 7, 2, 1, 5, 3, 8, 6};
        TreeNode root = reConstructTree(pre, 0, pre.length - 1, in, 0, in.length - 1);
        printTree(root);
    }

    public static TreeNode reConstructTree(int[] pre, int preStart, int preEnd, int[] in, int inStart, int inEnd) {
        //pre 1 2 4 7 3 5 6 8
        //in  4 7 2 1 5 3 8 6
        //首先根据前序遍历的根的值在中序遍历找到根的位置
        if(preStart>preEnd||inStart>inEnd){
            return null;
        }
        TreeNode root = new TreeNode(pre[preStart]);
        for (int i = inStart; i <=inEnd; i++) {
            if (pre[preStart] == in[i]) {  //此时的i为根在中序中的索引位置
                root.left = reConstructTree(pre,preStart+1,preStart+i-inStart,in,inStart,i-1);
                root.right = reConstructTree(pre,preStart+i-inStart+1,preEnd,in,i+1,inEnd);
                return root;
            }
        }
        return null;
    }

    public static void printTree(TreeNode root) {
        if (root != null) {
            printTree(root.left);
            System.out.print(root.val + " ");
            printTree(root.right);
        }
    }


}
