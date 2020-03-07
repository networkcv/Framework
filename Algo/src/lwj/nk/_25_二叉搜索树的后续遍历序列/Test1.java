package lwj.nk._25_二叉搜索树的后续遍历序列;


import org.junit.Test;

/**
 * create by lwj on 2018/10/30
 */
public class Test1 {
    //思路：找第一个比root大的树，为index  判断index前是否都小于root，index后是否都大于root
    //      然后再用递归查找子树，但start>=end时，说明当前是叶子结点，就可以直接返回true了
    public boolean verifySequenceOfBST(int[] sequence) {
        if (sequence == null || sequence.length == 0) {
            return false;
        }
        return verifySequenceOfBST(sequence, 0, sequence.length - 1);
    }
    public boolean verifySequenceOfBST(int[] arr, int start, int end) {
        if (start >= end) {
            return true;
        }
        boolean flag=false;     //7,4,6,5   4,6,7,5
        int index = start;
        for (int i = start; i < end; i++) {
            if (arr[i] > arr[end]) {
                index = i;
                flag=true;
                break;
            }
        }
        if (flag) {
            for (int i = index; i < end; i++) {
                if (arr[i] < arr[end]) {
                    return false;
                }
            }
        }
        return verifySequenceOfBST(arr, start, index - 1) && verifySequenceOfBST(arr, index, end - 1);
    }

    @Test
    public void fun1() {
        //           10
        //         /   \
        //        6     14
        //       /\     /\
        //      4  8  12  16
        int[] data = {4, 8, 6, 12, 16, 14, 10};
        System.out.println("true: " + verifySequenceOfBST(data));

        //           5
        //          / \
        //         4   7
        //            /
        //           6
        int[] data2 = {4, 6, 7, 5};
        System.out.println("true: " + verifySequenceOfBST(data2));

        //               5
        //              /
        //             4
        //            /
        //           3
        //          /
        //         2
        //        /
        //       1
        int[] data3 = {1, 2, 3, 4, 5};
        System.out.println("true: " + verifySequenceOfBST(data3));

        // 1
        //  \
        //   2
        //    \
        //     3
        //      \
        //       4
        //        \
        //         5
        int[] data4 = {5, 4, 3, 2, 1};
        System.out.println("true: " + verifySequenceOfBST(data4));

        // 树中只有1个结点
        int[] data5 = {5};
        System.out.println("true: " + verifySequenceOfBST(data5));

        int[] data6 = {7, 4, 6, 5};
        System.out.println("false: " + verifySequenceOfBST(data6));

        int[] data7 = {4, 6, 12, 8, 16, 14, 10};
        System.out.println("false: " + verifySequenceOfBST(data7));
    }
}
