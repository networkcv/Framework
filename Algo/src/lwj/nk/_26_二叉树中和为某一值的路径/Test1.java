package lwj.nk._26_二叉树中和为某一值的路径;

import lwj.nk._0_Utils.TreeNode;
import org.junit.Test;

import java.util.ArrayList;

/**
 * create by lwj on 2018/10/31
 */
public class Test1 {
    //输入一颗二叉树的跟结点和一个整数，打印出二叉树中结点值的和为输入整数的所有路径。
    //路径定义为从树的根结点开始往下一直到叶结点所经过的结点形成一条路径。(注意: 在返回值的list中，数组长度大的数组靠前)
    public ArrayList<ArrayList<Integer>> findPath(TreeNode root, int target) {
        return null;
    }
    @Test
    public void fun1(){
        //            10
        //         /      \
        //        5        12
        //       /\
        //      4  7
        TreeNode root = new TreeNode();
        root.val = 10;
        root.left = new TreeNode();
        root.left.val = 5;
        root.left.left = new TreeNode();
        root.left.left.val = 4;
        root.left.right = new TreeNode();
        root.left.right.val = 7;
        root.right = new TreeNode();
        root.right.val = 12;

        // 有两条路径上的结点和为22
        System.out.println("findPath(root, 22);");
        findPath(root, 22);

        // 没有路径上的结点和为15
        System.out.println("findPath(root, 15);");
        findPath(root, 15);

        // 有一条路径上的结点和为19
        System.out.println("findPath(root, 19);");
        findPath(root, 19);


        //               5
        //              /
        //             4
        //            /
        //           3
        //          /
        //         2
        //        /
        //       1
        TreeNode root2 = new TreeNode();
        root2.val = 5;
        root2.left = new TreeNode();
        root2.left.val = 4;
        root2.left.left = new TreeNode();
        root2.left.left.val = 3;
        root2.left.left.left = new TreeNode();
        root2.left.left.left.val = 2;
        root2.left.left.left.left = new TreeNode();
        root2.left.left.left.left.val = 1;

        // 有一条路径上面的结点和为15
        System.out.println("findPath(root2, 15);");
        findPath(root2, 15);

        // 没有路径上面的结点和为16
        System.out.println("findPath(root2, 16);");
        findPath(root2, 16);

        // 1
        //  \
        //   2
        //    \
        //     3
        //      \
        //       4
        //        \
        //         5
        TreeNode root3 = new TreeNode();
        root3.val = 1;
        root3.right = new TreeNode();
        root3.right.val = 2;
        root3.right.right = new TreeNode();
        root3.right.right.val = 3;
        root3.right.right.right = new TreeNode();
        root3.right.right.right.val = 4;
        root3.right.right.right.right = new TreeNode();
        root3.right.right.right.right.val = 5;

        // 有一条路径上面的结点和为15
        System.out.println("findPath(root3, 15);");
        findPath(root3, 15);

        // 没有路径上面的结点和为16
        System.out.println("findPath(root3, 16);");
        findPath(root3, 16);

        // 树中只有1个结点
        TreeNode root4 = new TreeNode();

        root4.val = 1;
        // 有一条路径上面的结点和为1
        System.out.println("findPath(root4, 1);");
        findPath(root4, 1);

        // 没有路径上面的结点和为2
        System.out.println("findPath(root4, 2);");
        findPath(root4, 2);

        // 树中没有结点
        System.out.println("findPath(null, 0);");
        findPath(null, 0);
    }
}
