package lwj.nk.__二叉树的深度;

import lwj.nk._0_Utils.TreeNode;

/**
 * create by lwj on 2019/3/28
 */
public class Test1 {
    public  static int TreeDepth(TreeNode root) {
        if(root==null)return 0;
        int left = TreeDepth(root.left);
        int right = TreeDepth(root.right);
        return  left>right?++left:++right;
    }
    public static void main(String[] args){
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
        root.right.right.right = new TreeNode(8);
//        System.out.println(TreeDepth(new TreeNode(1)));
        System.out.println(TreeDepth(root));

    }
}
