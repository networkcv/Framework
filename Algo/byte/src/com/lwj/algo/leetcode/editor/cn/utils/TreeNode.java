package com.lwj.algo.leetcode.editor.cn.utils;

/**
 * Date: 2022/5/6
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class TreeNode {
    public int val;
    public TreeNode left;
    public TreeNode right;

    public TreeNode() {
    }

    public TreeNode(Integer val) {
        this.val = val;
    }

    public TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }

    public static void main(String[] args) {
        TreeNode treeNode = TreeNodeUtil.constructTree(new Integer[]{1, 2, 3, 4, 5, 6, 7});
        TreeNodeUtil.printTreeHorizontal(treeNode);
        TreeNodeUtil.printTree(treeNode);
        System.out.println(TreeNodeUtil.getTreeDepth(treeNode));
    }
}
