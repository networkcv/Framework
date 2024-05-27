package com.lwj.algo.prattle.nk._3_二维数组中的查找;

/**
 * 时间复杂度为O(n^2)
 * 这个方法适合数组内数据没有规律，有规律的数组不建议使用
 * create by lwj on 2018/9/27
 */
//题目：在一个二维数组中，每一行都按照从左到右递增的顺序排序，每一列都按照从上到下递增的顺序排序。
//      请完成一个函数，输入这样的一个二维数组和一个整数，判断数组中是否含有该整数。
public class Test1_循环嵌套法 {
    public static boolean find(int[][] matrix, int number) {
        if(matrix==null||matrix.length<1){
            return false;
        }
        int rows = matrix.length;
        int clos = matrix[0].length;

        for(int i=0;i<rows;i++){
            for(int j=0;j<clos;j++){
                if(matrix[i][j]==number){
                    return  true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        int[][] matrix = {  {1, 2, 8, 9},
                            {2, 4, 9, 12},
                            {4, 7, 10, 13},
                         };
        System.out.println(find(matrix, 7));    // 要查找的数在数组中                 true
        System.out.println(find(matrix, 5));    // 要查找的数不在数组中               false
        System.out.println(find(matrix, 1));    // 要查找的数是数组中最小的数字       true
        System.out.println(find(matrix, 13));   // 要查找的数是数组中最大的数字       true
        System.out.println(find(matrix, 0));    // 要查找的数比数组中最小的数字还小   false
        System.out.println(find(matrix, 14));   // 要查找的数比数组中最大的数字还大   false
        System.out.println(find(null, 14));     // 健壮性测试，输入空指针     false
    }
}
