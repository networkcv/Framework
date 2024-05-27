package com.lwj.algo.prattle.wk._12_二叉树._02_二叉查找树的实现;

import com.lwj.algo.prattle.nk._0_Utils.TreeNode;

/**
 * create by lwj on 2019/3/29
 */
public class BinarySearchTree1 {
    private TreeNode tree;

    //要插入的data不重复
    public void insert(int data) {
        if (tree == null) {
            tree = new TreeNode(data);
            return;
        }
        TreeNode p = tree;
        while (p != null) {
            if (data > p.val) {
                if (p.right == null) {
                    p.right = new TreeNode(data);
                    return;
                }
                p = p.right;
            } else {
                if (p.left == null) {
                    p.left = new TreeNode(data);
                    return;
                }
                p = p.left;
            }
        }

    }

    public void delete(int data) {
        TreeNode p = tree;    //指向当前节点
        TreeNode pp = null;   //指向当前节点父节点
        while (p != null && p.val != data) {
            pp = p;
            if (p.val > data) p = p.left;
            else p = p.right;
        }
        if (p==null)return; //没有找到

        //要删除的节点有两个子节点
        if(p.left!=null&&p.right!=null){    //找到右子树的最小节点或者左子树的最大节点
            TreeNode minP=p.right;
            TreeNode minPP=null;
            while(minP.left!=null){
                minPP=minP;
                minP=minPP.left;
            }
            p.val=minP.val;
            p=minP;
            pp=minPP;
        }
        //要删除的节点是叶子节点或者只有一个子节点
        TreeNode child;
        if(p.left!=null) child=p.left;
        else if(p.right!=null) child=p.right;
        else child=null;

        if(pp==null) tree=child;
        else if(pp.left==p)pp.left=child;
        else pp.right=child;
    }

    public TreeNode find(int data) {
        TreeNode p = tree;
        while (p != null && p.val != data) {
            if (p.val > data) p = p.left;
            else p = p.right;
        }
        return p;
    }
}
