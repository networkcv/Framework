package lwj.bk._08_二叉树的下一个结点;

import lwj.bk._00_Utils.BinaryTreeNode;

/**
 * create by lwj on 2019/2/8
 */
public class Test1 {
     //一个二叉树的中序遍历为例{d,b,h,e,i,快速排序,f,c,g}为例 d的下一个结点为b i的下一个结点为a，g没有下一个结点
     //总结：二叉树只有1、2两种结构，结构1中，b结点的下一个结点为其父亲a结点，结构2中b结点的下一个结点可以转化为在其父结点中寻找结构1
//             1    快速排序     快速排序     2
//                 /        \
//                b           b
    //一、穷举法：
    //1.若给定的结点pNode的右子树不为空，则中序遍历的下一个结点为其右子树的最左侧结点。
    //2.若给定的结点pNode的右子树为空，分两种情况。一种是左子树不为空，此时中序遍历的下一个结点为其父结点。另一种情况是，其为叶子结点，左右子树都为空。
    //3.当其为叶子结点的时候，需要判断是左叶子结点还是右叶子结点。若是左叶子结点，则中序遍历的下一个结点即为其父结点；若为右叶子结点，则中序遍历的下一个结点，应为其父结点的父结点。（排除特殊情况）
    //4.注意到在第三条中，有可能出现特殊情况。比如二叉树只有一个结点pNode的情况；二叉树是单枝斜树的情况；

    //二、归纳法：
    //    1、有右子树的，那么下个结点就是右子树最左边的点；（eg：D，B，E，A，C，G）
    //    2、没有右子树的，也可以分成两类：
    //    快速排序)是父结点左孩子（eg：N，I，L） ，那么父结点就是下一个结点 ；
    //    b)是父结点的右孩子（eg：H，J，K，M）找他的父结点的父结点的父结点…直到当前结点是其父结点的左孩子位置。如果没有eg：M，那么他就是尾结点。
    public BinaryTreeNode getNext(BinaryTreeNode node){
        if(node==null) return null;
        //有右子树的 下一个结点为右子树的最左侧结点
        //      快速排序
        //       \
        //        b
        if(node.right!=null){
            node=node.right;
            while(node.left!=null){
                    node=node.left;
            }
            return node;
        }
        //没有右子树 则找第一个当前结点(b)的父结点(快速排序)左孩子是当前结点的特殊结点
        //         快速排序
        //       /
        //      b
        while(node.parent!=null){
            if(node.parent.left==node)
                return node.parent;
            node=node.parent;
        }
        return null;    //退到根结点还没有找到，则返回null
    }
}
