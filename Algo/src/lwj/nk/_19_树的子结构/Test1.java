package lwj.nk._19_树的子结构;

/**
 * create by lwj on 2018/10/9
 */

import lwj.nk._0_Utils.TreeNode;

/**
 * public class TreeNode {
 * int val = 0;
 * TreeNode left = null;
 * TreeNode right = null;
 * <p>
 * public TreeNode(int val) {
 * this.val = val; } }
 */
public class Test1 {

    //输入两棵二叉树A，B，判断B是不是A的子结构。（ps：我们约定空树不是任意一个树的子结构）
    public static void main(String[] args) {
        TreeNode root1 = new TreeNode(8);
        root1.right = new TreeNode(7);
        root1.left = new TreeNode(8);
        root1.left.left = new TreeNode(9);
        root1.left.right = new TreeNode(2);
        root1.left.right.left = new TreeNode(4);
        root1.left.right.right = new TreeNode(7);

        TreeNode root2 = new TreeNode(8);
        root2.left = new TreeNode(8);
        root2.right = new TreeNode(7);

        TreeNode root3 = new TreeNode(8);
        root3.left = new TreeNode(9);
        root3.right = new TreeNode(3);



//        TreeNode.printTree(root1);
//           8                      8
//      8         7             8       7
//  9      2
//      4    7
//        TreeNode.printTree(root2);  //9 8 2

        System.out.println(hasSubtree(root1, root2));
        System.out.println(hasSubtree(root1, root3));
//        System.out.println(hasSubtree(root2, root1));
//        System.out.println(hasSubtree(root1, root1.left));
//        System.out.println(hasSubtree(root1, null));
//        System.out.println(hasSubtree(null, root2));
//        System.out.println(hasSubtree(null, null));


    }

    public static boolean hasSubtree(TreeNode root1, TreeNode root2) {
//        if (root1 == root2) {
//            return true;
//        }
        if (root1 == null || root2 == null) {
            return false;
        }
        boolean result = false;
        if (root1.val == root2.val) {
            result = match(root1, root2);
        }
        if (result) {
            return true;
        }
        return hasSubtree(root1.left, root2) || hasSubtree(root1.right, root2);

    }

    public static boolean match(TreeNode root1, TreeNode root2) {
        // 只要两个对象是同一个就返回true
//
//        if (root1 == root2) {
//            return true;
//        }
        // 只要树B的根结点点为空就返回true
        if (root2 == null) {
            return true;
        }
        // 树B的根结点不为空，如果树A的根结点为空就返回false
        if (root1 == null) {
            return false;
        }

        // 如果两个结点的值相等，则分别判断其左子结点和右子结点
        if (root1.val == root2.val) {
            return match(root1.left, root2.left) && match(root1.right, root2.right);
        }

        // 结点值不相等返回false
        return false;
    }
}
