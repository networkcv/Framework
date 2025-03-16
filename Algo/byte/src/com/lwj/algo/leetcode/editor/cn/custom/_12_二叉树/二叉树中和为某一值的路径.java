package com.lwj.algo.leetcode.editor.cn.custom._12_二叉树;


import com.lwj.algo.leetcode.editor.cn.utils.TreeNode;
import com.lwj.algo.leetcode.editor.cn.utils.TreeNodeUtil;

import java.util.ArrayList;

/**
 * Date: 2025/3/14
 * <p>
 * Description:
 *
 * @author 乌柏
 */
class FindPath {

    ArrayList<ArrayList<Integer>> res = new ArrayList<>();
    ArrayList<Integer> path = new ArrayList<>();


    public static void main(String[] args) {
        TreeNode root = TreeNodeUtil.constructTree(10, 5, 12, 7, 3);
        TreeNodeUtil.printTree(root);
        ArrayList<ArrayList<Integer>> path = new FindPath().findPath(root, 22);
        System.out.println(path);
    }

    public ArrayList<ArrayList<Integer>> findPath(TreeNode root, int target) {
        dfs(root, target);
        return res;
    }

    //在dfs在访问节点的时候需要添加到path里，离开节点的时候需要从path中移除
    public void dfs(TreeNode root, int target) {
        if (root == null || target < 0) return;
        path.add(root.val);
        if (root.left == null && root.right == null && target == root.val) {
            res.add(new ArrayList<>(path));
            path.remove(path.size() - 1);
            return;
        }
        dfs(root.left, target - root.val);
        dfs(root.right, target - root.val);
        path.remove(path.size() - 1);
    }

}
