package com.lwj.algo.leetcode.editor.cn.custom._07_排序._01_冒泡排序;

/**
 * create by lwj on 2019/2/28.
 */
public class bubbleSort{
    //最好时间复杂度  平均时间复杂度  最坏时间复杂度     空间复杂度    是否稳定
    //      O(n)         O(n^2)          O(n^2)             O(1)        稳定
    // 是否稳定 比如我们有一组数据 2，9，3，4，8，3，按照大小排序之后2，3，3，4，8，9  其中两个3没有发生交换
    //原地排序算法，就是特指空间复杂度是 O(1) 的排序算法
    //逆序度=满有序度-有序度  满有序度=n(n-1)/2
    public static void main(String[] args) {
        int [] arr = {3,5,4,1,2,6};
//        int [] arr = {4,5,6,3,2,1};
        bubbleSort(arr,arr.length);
        for(int a:arr){
            System.out.print(a+" ");
        }
    }
    
    public static void bubbleSort(int [] arr,int n){
        if(n<=0) return;
        for(int i=0;i<n;i++){
            boolean flag=false;     //判断是否发生交换
            for(int j=0;j<n-i-1;j++){
                if(arr[j]>arr[j+1]){
                    int tmp=arr[j];     //交换
                    arr[j]=arr[j+1];
                    arr[j+1]=tmp;
                    flag=true;
                }
            }
            if(!flag){      //没有交换则说明数组有序
                break;
            }
        }
    }

}
