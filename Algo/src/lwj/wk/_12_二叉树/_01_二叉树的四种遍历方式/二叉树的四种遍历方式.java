package lwj.wk._12_二叉树._01_二叉树的四种遍历方式;


import lwj.wk._12_二叉树._00_Util.TreeNode;

import java.util.*;

/**
 * create by lwj on 2019/3/21
 */
public class 二叉树的四种遍历方式 {
    public static void main(String[] args) {
        //            1
        //      2           3
        //  4       5   6       7            1,2,3,4,5,6,7
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.left.left = new TreeNode(4);
        root.left.right = new TreeNode(5);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(6);
        root.right.right = new TreeNode(7);
//        preOrder(root);
//        preTraversal(root);
//        inTraversal(root);
//        posTraversal(root);
        System.out.println(levelTraversal(root));
        System.out.println(levelTraversal2(root));
    }

    //前序遍历的非递归实现
    public static ArrayList<Integer> preOrder(TreeNode root) {
        ArrayList<Integer> list = new ArrayList<>();
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        while (!stack.empty()) {
            TreeNode treeroot = stack.pop();
            list.add(treeroot.val);
            if (treeroot.right != null) {
                stack.push(treeroot.right);
            }
            if (treeroot.left != null) {
                stack.push(treeroot.left);
            }
        }
        return list;
    }

    //前序遍历
    private static void preTraversal(TreeNode root) {
        if (root != null) {
            System.out.print(root.val + " ");
            preTraversal(root.left);
            preTraversal(root.right);
        }

    }

    //中序遍历
    private static void inTraversal(TreeNode root) {
        if (root != null) {
            inTraversal(root.left);
            System.out.print(root.val + " ");
            inTraversal(root.right);
        }
    }

    //后序遍历
    private static void posTraversal(TreeNode root) {
        if (root != null) {
            posTraversal(root.left);
            posTraversal(root.right);
            System.out.print(root.val + " ");
        }
    }

    //按层遍历
    private static List<Integer> levelTraversal(TreeNode root) {
        if (root == null) return new ArrayList<>();
        List<Integer> res = new ArrayList<Integer>();
        Queue<TreeNode> queue = new LinkedList<TreeNode>();
        queue.offer(root);
        while (!queue.isEmpty()) {
            TreeNode treeNode = queue.peek();
            if (treeNode.left != null) {
                queue.offer(treeNode.left);
            }
            if (treeNode.right != null) {
                queue.offer(treeNode.right);
            }
            treeNode = queue.poll();
            res.add(treeNode.val);
        }
        return res;
    }

    //按层遍历，将每一层添加到一个list中返回
    private static List<List<Integer>> levelTraversal2(TreeNode root) {
        if (root == null) return new ArrayList<>();
        List res = new ArrayList();
        Queue<TreeNode> queue = new LinkedList();   //元素总队列
        Queue<TreeNode> curLevelNodes = new LinkedList();   //当前层级元素队列
        queue.offer(root);
        while (!queue.isEmpty()) {
            //将当前层级的元素从总队列中取出放入当前层级元素队列
            curLevelNodes.offer(queue.poll());
            if(queue.isEmpty()){    //上一步完成
                List list = new ArrayList(curLevelNodes.size());
                while(!curLevelNodes.isEmpty()){
                    TreeNode tmp = curLevelNodes.poll();
                    if (tmp.left != null) {
                        queue.offer(tmp.left);
                    }
                    if (tmp.right != null) {
                        queue.offer(tmp.right);
                    }
                    list.add(tmp.val);
                }
                res.add(list);
            }
        }
        return res;
    }
}
