package com.lwj.algo.prattle.wk._07_排序._02_插入排序;

/**
 * create by lwj on 2019/3/1.
 */
public class InsertSort {
    //思路类似于整理扑克牌或向输入有序数组中插入数字
    //最好时间复杂度     平均时间复杂度  最坏时间复杂度      空间复杂度    是否稳定
    //      O(n)              O(n^2)         O(n^2)             O(1)       稳定
    // 是否稳定 比如我们有一组数据 2，9，3，4，8，3，按照大小排序之后2，3，3，4，8，9  其中两个3没有发生交换
    //原地排序算法，就是特指空间复杂度是 O(1) 的排序算法
    //有序数组的插入操作的平均时间复杂度为O(n)，对于插入排序来说每次插入操作都相当于在数组中插入一个数据，
    //执行n次插入操作，所以平均时间复杂度为O(n^2)

    //插入排序优于冒泡排序的原因是  在交换元素时，冒泡排序需要执行三次赋值语句，而插入排序只需要执行一次
    public static void main(String[] args) {
        int[] arr = {3, 5, 4, 1, 2, 6};
//        int [] arr = {4,5,6,3,2,1};
        insertSort(arr, arr.length);
        for (int a : arr) {
            System.out.print(a + " ");
        }
    }

    public static void insertSort(int[] arr, int n) {
        if (n <= 1) return;
        for (int i = 1; i < n; i++) {   //从第二个元素开始遍历数组
            int value = arr[i];   //有序区域元素后移会覆盖判别元素
            ///下边的for循环还有待优化
//            for (int j = 0; j < i; j++) {
//                if (value < arr[j]) {  //判别元素小于有序区域中的某一元素
//                    int tmp = arr[j + 1];
//                    arr[j + 1] = arr[j];
//                }
//            }
            ///优化后的for循环
            int j = i - 1;
            for (; j >= 0; j--) {
                if (arr[j] > value) {
                    arr[j + 1] = arr[j];
                } else {
                    break;
                }
            }
            arr[j + 1] = value;
        }
    }
}
