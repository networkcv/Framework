package com.lwj.algo.prattle.nk._21_顺时针打印矩阵;

import java.util.ArrayList;

/**
 * create by lwj on 2018/10/16
 */
public class Test1 {
    //输入一个矩阵，按照从外向里以顺时针的顺序依次打印出每一个数字，
    // 例如输入如下4 X 4矩阵： 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16
    // 则依次打印出数字        1,2,3,4,8,12,16,15,14,13,9,5,6,7,11,10.
    // 1  2  3  4
    // 5  6  7  8
    // 9 10 11 12
    //13 14 15 16
//         1, 2, 3, 4,
//         8,12,16,15,
//        14,13, 9, 5,
//         6, 7,11,10.
    public static void main(String[] args) {
        int[][] numbers = {
                {1, 2, 3, 4, 5},
                {16, 17, 18, 19, 6},
                {15, 24, 25, 20, 7},
                {14, 23, 22, 21, 8},
                {13, 12, 11, 10, 9},
        };
        printMatrix(numbers);
    }

    public static ArrayList<Integer> printMatrix(int[][] matrix) {
        return null;
    }
}
