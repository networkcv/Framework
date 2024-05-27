package com.lwj.algo.prattle.nk._15_调整数组顺序使奇数位于偶数前面;

/**
 * create by lwj on 2018/10/3
 */
public class Test1 {
    //输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有奇数位于数组的前半部分，所有偶数位予数组的后半部分。
    public static void main(String[] args) {
        int[] array = {0, 2, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] array1 = {0, 2, 2, 3, 4, 5, 6, 7, 8, 9};
        long l1 = System.nanoTime();
        int[] arr = reOrderArray(array);
        long l2 = System.nanoTime();
        reOrderArray1(array1);
        long l3 = System.nanoTime();
        printArray(arr);
        System.out.println(l2 - l1 + "创建两数组");
        printArray(array1);
        System.out.println(l3 - l2 + "两层for循环");

    }

    public static int[] reOrderArray(int[] array) {
        int[] arr1 = new int[array.length]; //  存放奇数的数组
        int[] arr2 = new int[array.length];   //  存放偶数的数组
        int a1 = 0, a2 = 0;
        for (int i = 0; i < array.length; i++) {
            if (array[i] % 2 == 1) {
                arr1[a1] = array[i];
                a1++;
            } else {
                arr2[a2] = array[i];
                a2++;
            }
        }
        int start = 0, count = 0;
        for (int arr : arr1) {
            if (arr != 0) {
                start++;
            }
        }
        for (int i = start; i < arr1.length; i++) {
            arr1[i] = arr2[count];
            count++;
        }
        return arr1;

    }


    public static void reOrderArray1(int[] array) {
        for(int i=0;i<array.length-1;i++){
            for(int j=0;j<array.length-i;j++){
                if(array[j]%2==0 &&array[j+1]%2==1){
                    int t = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = t;
                }
            }
        }
    }


    public static void printArray(int[] arr) {
        if (arr != null && arr.length > 0) {
            for (int i : arr) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
    }
}
