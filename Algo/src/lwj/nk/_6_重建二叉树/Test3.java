package lwj.nk._6_重建二叉树;

import lwj.nk._0_Utils.TreeNode;

/**
 * create by lwj on 2018/10/2
 */
public class Test3 {
    //输入某二叉树的前序遍历和中序遍历的结果，请重建出该二叉树。假设输入的前序遍历和中序遍历的结果中都不含重复的数字。
    // 例如输入前序遍历序列{1,2,4,7,3,5,6,8}和中序遍历序列{4,7,2,1,5,3,8,6}，则重建二叉树并返回。
    public static void main(String[] args){
        int[] pre = new int[]{1, 2, 4, 7, 3, 5, 6, 8};
        int[] in = new int[] {4, 7, 2, 1, 5, 3, 8, 6};
        TreeNode root = reConstructTree(pre, 0, pre.length - 1, in, 0, in.length - 1);
        printTree(root);
    }



    private static TreeNode reConstructTree(int[]pre,int preStart,int preEnd,int[] in,int inStart ,int inEnd){
        if(preStart>preEnd||inStart>inEnd){
            return null;
        }
        TreeNode root=new TreeNode(pre[preStart]);
        for(int i=inStart;i<=inEnd;i++){
            if(pre[preStart]==in[i]){
                root.left=reConstructTree(pre,preStart+1,preStart+i-inStart,in,inStart,i-1);
                root.right=reConstructTree(pre,preStart+i-inStart+1,preEnd,in,i+1,inEnd);
                return root;
            }
        }
        return null;
    }

    private static void printTree(TreeNode root) {
        if (root!=null){
            printTree(root.left);
            System.out.print(root.val+" ");
            printTree(root.right);
        }
    }
}
