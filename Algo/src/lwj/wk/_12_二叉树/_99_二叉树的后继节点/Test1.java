package lwj.wk._12_二叉树._99_二叉树的后继节点;

/**
 * 对一个二叉树进行中序遍历，即可获得后继节点，但是需要遍历整个数，时间复杂度为O(N)
 * O(LogN):
 *      实现1:包含父指针的节点  getNext
 *      实现2:不包含指针的节点(该法适用于二叉查找树) getNext2  通过记录parent 和 lastLeftNode(最后一次查找路径中作左拐的点)
 *            (直到找到一个节点P，父节点Q 是 P 节点的左边孩子，那么P节点就是该节点的后继节点)
 * create by lwj on 2019/3/28
 */
public class Test1 {
    public static  TreeNode getNext2(TreeNode root, TreeNode node){
        if(node==null)return null;
        //判断node节点是否包含右子树
        if(node.right!=null){
            node=node.right;
            while(node.left!=null){
                node=node.left;
            }
            return node;
        }
        //node节点不包含右子树的情况
        TreeNode p = null;  //记录parent节点
        TreeNode lp =null; //记录最后一次左拐的点 默认为root
        while(root!=null&&root!=node){ //遍历找到node节点，并记录 p和 lp
            p=root;
            if(node.val>root.val){
                root=root.right;
            }
            else{
                lp=root;    //记录需要左拐的节点
                root=root.left;
            }
        }
        //根据node节点与父节点p的关系返回不同的值
        //1.node为p的左子树，则返回p节点
        //2.node为p的右子树，则返回lp节点
        return  node.val<p.val?p:lp;
    }

    public static TreeNode getNext(TreeNode node){
        if(node==null)return null;
        if(node.right!=null){
            TreeNode tmp = node.right;
            while(tmp!=null&&tmp.left!=null){
                tmp=tmp.left;
            }
            return tmp;
        }
        while(node.next!=null){
            if(node.next.left==node) return node.next;
            node=node.next;
        }
        return null;
    }

    public static void main(String[] args){
        //            4
        //      2           6
        //  1       3   5       7            1,2,3,4,5,6,7
        TreeNode root = new TreeNode(4);
        TreeNode treeNode2 = new TreeNode(2);
        TreeNode treeNode3 = new TreeNode(6);
        TreeNode treeNode4 = new TreeNode(1);
        TreeNode treeNode5 = new TreeNode(3);
        TreeNode treeNode6 = new TreeNode(5);
        TreeNode treeNode7 = new TreeNode(7);
        root.left=treeNode2;
        treeNode2.left=treeNode4;
        treeNode2.right=treeNode5;
        treeNode2.next=root;
        treeNode4.next=treeNode2;
        treeNode5.next=treeNode2;
        root.right=treeNode3;
        treeNode3.left=treeNode6;
        treeNode3.right=treeNode7;
        treeNode3.next=root;
        treeNode6.next=treeNode3;
        treeNode7.next=treeNode3;
        System.out.println(getNext(root));
        System.out.println(getNext(treeNode2));
        System.out.println(getNext(treeNode3));
        System.out.println(getNext(treeNode4));
        System.out.println(getNext(treeNode5));
        System.out.println(getNext(treeNode6));
        System.out.println(getNext(treeNode7));
        System.out.println("________________");
        System.out.println(getNext2(root, root));
        System.out.println(getNext2(root,treeNode2));
        System.out.println(getNext2(root,treeNode3));
        System.out.println(getNext2(root,treeNode4));
        System.out.println(getNext2(root,treeNode5));
        System.out.println(getNext2(root,treeNode6));
        System.out.println(getNext2(root,treeNode7));
    }
}


class TreeNode {
    int val;
    TreeNode left = null;
    TreeNode right = null;
    TreeNode next = null;

    TreeNode(int val) {
        this.val = val;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
                "val=" + val +
                '}';
    }
}