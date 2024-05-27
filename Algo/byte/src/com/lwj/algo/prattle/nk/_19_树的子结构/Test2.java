package com.lwj.algo.prattle.nk._19_树的子结构;

import com.lwj.algo.prattle.nk._0_Utils.TreeNode;

/**
 * create by lwj on 2018/10/16
 */
public class Test2 {
    //输入两棵二叉树A，B，判断B是不是A的子结构。（ps：我们约定空树不是任意一个树的子结构）
    //分为两步，第一步先在A树找到与B树根结点相同的结点，然后以该结点作为根结点与B树比较是否结构相同
    public static void main(String[] args) {
        TreeNode root1 = new TreeNode(8);
        root1.right = new TreeNode(7);
        root1.left = new TreeNode(8);
        root1.left.left = new TreeNode(9);
        root1.left.right = new TreeNode(2);
        root1.left.right.left = new TreeNode(4);
        root1.left.right.right = new TreeNode(7);

        TreeNode root2 = new TreeNode(8);
        root2.left = new TreeNode(9);
        root2.right = new TreeNode(2);

//        TreeNode.printTree(root1);  //9 8 4 2 7 8 7
//        TreeNode.printTree(root2);  //9 8 2

        System.out.println(hasSubtree(root1, root2));
        //        System.out.println(hasSubtree(root2, root1));
//        System.out.println(hasSubtree(root1, root1.left));
//        System.out.println(hasSubtree(root1, null));
//        System.out.println(hasSubtree(null, root2));
//        System.out.println(hasSubtree(null, null));
    }

    private static boolean hasSubtree(TreeNode root1, TreeNode root2) {
        if (root1 == null || root2 == null) {
            return false;
        }
        boolean matchResult = false;
        if (root1.val == root2.val) {
            //找到A树和B树的相同结点
            matchResult = match(root1, root2);
        }
        if (matchResult) {
            return true;
        }
        return hasSubtree(root1.left, root2) || hasSubtree(root1.right, root2);

    }

    public static boolean match(TreeNode root1, TreeNode root2) {
        //将传入的root1作为根结点与root比较
        if(root2==null){
            return true;
        }
        if(root1==null){
            return false;
        }
        if (root1.val == root2.val) {
            return match(root1.left, root2.left) && match(root1.right, root2.right);
        }
        return false;
    }
}
