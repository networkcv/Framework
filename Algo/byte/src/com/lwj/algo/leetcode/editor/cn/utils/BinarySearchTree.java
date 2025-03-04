package com.lwj.algo.leetcode.editor.cn.utils;

/**
 * Date: 2025/3/4
 * <p>
 * Description: 二叉搜索树
 *
 * @author 乌柏
 */
public class BinarySearchTree {

    private TreeNode root;

    public TreeNode getRoot() {
        return this.root;
    }

    public void insert(int val) {
        root = insert(root, val);
    }

    public TreeNode insert(TreeNode root, int val) {
        if (root == null) {
            root = new TreeNode(val);
            return root;
        }
        if (val < root.val) {
            root.left = insert(root.left, val);
        } else if (val > root.val) {
            root.right = insert(root.right, val);
        }
        return root;
    }

    public TreeNode search(int val) {
        return search(root, val);
    }

    public TreeNode search(TreeNode root, int val) {
        if (root == null) {
            return null;
        }
        if (val == root.val) {
            return root;
        } else if (val < root.val) {
            return search(root.left, val);
        } else {
            return search(root.right, val);
        }
    }

    public TreeNode delete(int val) {
        return delete(root, val);
    }

    public TreeNode delete(TreeNode root, int val) {
        if (root == null) {
            return null;
        }
        if (val < root.val) {
            root.left = delete(root.left, val);
        } else if (val > root.val) {
            root.right = delete(root.right, val);
        } else {
            if (root.left == null && root.right == null) {
                return null;
            }
            if (root.right == null) {
                return root.left;
            } else if (root.left == null) {
                return root.right;
            }
            //当节点有两个子节点时，找到右子树中最小的节点
            //用该节点的值替代当前节点的值，并删除右子树中最小的节点
            TreeNode minNode = findMin(root.right);
            root.val = minNode.val;
            delete(root.right, minNode.val);
        }
        return root;
    }

    public TreeNode findMin(TreeNode root) {
        while (root.left != null) {
            root = root.left;
        }
        return root;
    }

    public static void main(String[] args) {
        int[] ints = {8, 3, 1, 10, 6, 4, 7, 14, 13};
        BinarySearchTree root = new BinarySearchTree();
        for (int anInt : ints) {
            root.insert(anInt);
        }
        TreeNodeUtil.printTree(root.getRoot());

    }
}
