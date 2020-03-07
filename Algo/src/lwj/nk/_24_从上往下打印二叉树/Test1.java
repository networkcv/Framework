package lwj.nk._24_从上往下打印二叉树;

import lwj.nk._0_Utils.TreeNode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * create by lwj on 2018/10/30
 */
public class Test1 {
    public static ArrayList<Integer> PrintFromTopToBottom(TreeNode root) {
        ArrayList<Integer> reslist = new ArrayList<>();
        if(root==null){
            return reslist;
        }
        Queue <TreeNode> list = new LinkedList<>();
        list.add(root);
        TreeNode tmpNode ;//用于处理当前结点
        while(!list.isEmpty()){
            tmpNode=list.remove();
            reslist.add(tmpNode.val);
            if(tmpNode.left!=null){
                list.add(tmpNode.left);
            }
            if(tmpNode.right!=null){
                list.add(tmpNode.right);
            }
        }
        return reslist;
    }


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
        TreeNode.printTree(root);
        ArrayList<Integer> arrayList = PrintFromTopToBottom(root);
        System.out.println(arrayList);

    }
}
