package com.lwj.algo.leetcode.editor.cn.custom._12_二叉树._02_二叉查找树的实现;


import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;

/**
 * create by lwj on 2019/3/28
 */
public class BinarySearchTree {
    private TreeNode tree;

    public void insert(int val){
        if(tree==null){
            tree=new TreeNode(val);
            return;
        }
        TreeNode p=tree;
        while(p!=null){
            if(val>p.val){
                if(p.right==null){
                    p.right=new TreeNode(val);
                    return;
                }
                p=p.right;
            }else{
                if(p.left==null){
                    p.left=new TreeNode(val);
                    return;
                }
                p=p.left;
            }
        }
    }

    public void delete(int val){
        TreeNode p=tree;    //指向要删除的节点，初始化指向根节点
        TreeNode pp=null;   //记录p节点的父节点
        while(p!=null&&p.val!=val){ //查找要删除的元素
            pp=p;
            if(val>p.val) p=p.right;
            else p=p.left;
        }
        if(p==null) return; //没有找到

        //要删除的节点有两个子节点
        if(p.left!=null&&p.right!=null){    //查找右子树最小节点
            TreeNode minP=p.right;
            TreeNode minPP=p;   //minP的父节点
            while(minP!=null){
                minPP=minP;
                minP=minP.left;
            }
            p.val=minP.val;     //用右子树最小节点的值覆盖要删除节点的值，之后只需要删除原来右子树最小节点
            p=minP;             //指向右子树最小节点，转换为删除节点是叶子节点
            pp=minPP;
        }

        //要删除的节点是叶子节点或者只有一个子节点
        TreeNode child; //保留p的子节点   做统一处理
        if(p.left!=null) child=p.left;
        else if(p.right!=null) child=p.right;
        else child =null;

        if(pp==null) tree=child;//要删除的是根节点
        else if(pp.left==p) pp.left=child;
        else pp.right=child;
    }

    public TreeNode find(int val){
        TreeNode p = tree;
        while(p!=null){
            if(val>p.val) p=p.right;
            else if (val<p.val) p=p.left;
            else return p;
        }
        return null;
    }
}
