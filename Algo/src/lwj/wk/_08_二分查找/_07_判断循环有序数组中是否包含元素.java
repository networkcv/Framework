package lwj.wk._08_二分查找;

/**
 * create by lwj on 2019/3/12.
 */
public class _07_判断循环有序数组中是否包含元素 {
    public static void main(String[] args) {
//        int arr[] = {4, 5, 6, 1, 2, 3};
        int arr[] = {4, 5, 6, 4, 4};
//        System.out.println(pointFind(arr, arr.length, 1));
        System.out.println(binaryFind(arr, 0, arr.length - 1, 6));
    }

    //方法二：
    //    循环数组存在一个性质：以数组中间点为分区，会将数组分成一个有序数组和一个循环有序数组。
    //
    //    如果首元素小于 mid，说明前半部分是有序的，后半部分是循环有序数组；
    //    如果首元素大于 mid，说明后半部分是有序的，前半部分是循环有序的数组；
    //    如果目标元素在有序数组范围中，使用二分查找；
    //    如果目标元素在循环有序数组中，设定数组边界后，使用以上方法继续查找。
    //    时间复杂度为O(logN)
    private static int binaryFind(int[] arr, int low, int high, int target) {
        int mid = (low + high) / 2;
        if (arr[low] >= arr[mid]) {  //则mid右边有序(包含mid)
            //判断target是否在有序区间里
            if (target <= arr[high]) {
                //在有序区间里则进行二分查找
                return binary(arr, mid + 1, high, target);
            } else {
                //不在有序区间里则,重新设定数组边界 继续使用该方法查找
                return binaryFind(arr, low, mid - 1, target);
            }
        } else {  //则mid左边有序(包含mid)
            if (target >= arr[low]) {
                //在左边的有序区间内
                return binary(arr, low, mid, target);
            } else {
                //在右边的无序区间内
                return binaryFind(arr, mid, high, target);
            }
        }
    }

    private static int binary(int[] arr, int low, int high, int target) {
        int mid = 0;
        while (low <= high) {
            mid = (low + high) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] > target) {
                high = mid - 1;
            } else {
                low = mid + 1;
            }
        }
        return -1;

    }


    //方法一：
    //找到分界下标，可以分成两个有序数组，根据下标判断目标元素在哪个数组中，然后进行二分查找，
    //基于查找分界点，最好时间复杂度为O(logN) 最坏时间复杂度为O(n+logN) 平均时间复杂度为O(n+logN)
    private static int pointFind(int[] arr, int n, int target) {
        int point = 0;
        for (int i = 0; i < n - 2; i++) {
            if (arr[i] > arr[i + 1])
                point = i;
        }
        int low;
        int high;
        int mid;
        if (target >= arr[0]) {
            low = 0;
            high = point;
        } else {
            low = point + 1;
            high = n - 1;
        }
        while (low <= high) {
            mid = (low + high) / 2;
            if (arr[mid] > target) {
                high = mid - 1;
            } else if (arr[mid] < target) {
                low = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }


}
