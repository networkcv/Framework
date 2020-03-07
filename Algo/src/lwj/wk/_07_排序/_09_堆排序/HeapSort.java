package lwj.wk._07_排序._09_堆排序;

import java.util.Arrays;

/**
 * create by lwj on 2019/3/29
 */
public class HeapSort {
    /**
     * 堆排序 时间复杂度O(NlogN) 原地排序 不稳定
     * 1.建堆 2.排序
     */
    public static void main(String[] args) {
        int[] arr = {0, 1, 3, 2, 6, 5, 4};
        heapSort(arr, arr.length-1);
        System.out.println(Arrays.toString(arr));
    }

    private static void heapSort(int[] a, int n) {
        buildHeap(a,n);
        int k=n;
        while(k>1){
            swap(a,1,k);
            heapify(a,--k,1);
        }

    }

    private static void buildHeap(int[] a, int n) {
        for (int i = n / 2; i >= 1; i--) {
            heapify(a, n, i);
        }
    }

    private static void heapify(int[] a, int n, int i) {
        while (true) {
            int maxPos = i;
            if (i * 2 <= n && a[i] < a[i * 2]) maxPos = i * 2;
            if (i * 2 + 1 <= n && a[maxPos] < a[i * 2 + 1]) maxPos = i * 2 + 1;
            if(maxPos==i)break;
            swap(a,i,maxPos);
            i=maxPos;
        }
    }

    private static void swap(int[] a, int i, int maxPos) {
        int tmp;
        tmp=a[i];
        a[i]=a[maxPos];
        a[maxPos]=tmp;
    }
}
