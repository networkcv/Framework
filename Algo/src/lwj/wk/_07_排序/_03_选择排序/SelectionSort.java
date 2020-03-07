package lwj.wk._07_排序._03_选择排序;

import java.util.Arrays;

/**
 * create by lwj on 2019/3/4.
 */
public class SelectionSort {
    //选择排序的实现思路类似于插入排序，也分已排序区间和未排序区间，
    //但选择排序每次会从未排序区间中找到最小元素，放到已排序区间末尾
    //最好时间复杂度   平均时间复杂度  最坏时间复杂度      空间复杂度    是否稳定
    //      O(n)          O(n^2)          O(n^2)             O(1)         不稳定
    //比如5,8,5,2,9这样一组数据,使用选择排序算法来排序的话,第一次找到最小元素 2,与第一个5交换位置,
    // 那第一个5和中间的5顺序就变了,所以就不稳定了。正是因此,相对于冒泡排序和插入排序,选择排序就稍微逊色了。
    public static void main(String[] args) {
        int[] arr = {5, 8, 5, 2, 9};
        selectionSort(arr, arr.length);
        System.out.println(Arrays.toString(arr));
    }

    //每次选出无序区最小的值，放到有序区的末尾
    public static void selectionSort(int[] arr, int n) {
        if (n <= 1) return;
        for (int i = 0; i < n - 1; i++) {
            int min = i;
            boolean flag = false;   //如果无序区的第一个元素是最小值，则不必发生交换
            for (int j = i + 1; j < n; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                    flag = true;
                }
            }
            if (flag) {
                int tmp = arr[i];
                arr[i] = arr[min];
                arr[min] = tmp;
            }
        }
    }
}
