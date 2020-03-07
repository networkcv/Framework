package lwj.wk._07_排序._07_计数排序;

/**
 * create by lwj on 2018/11/3
 */
public class 计数排序 {
    //平均时间复杂度  最坏时间复杂度  空间复杂度    是否稳定
    //    O(n+k)        O(n+k)          O(n+k)        稳定
    //只能给非负整数排序，而且数据的范围k不能比要排序的数据n大得多，如果是小数或负数的话得转化为非负整数再排序
    //利用B数组下标当作值，下标所对应的位置存放 小于等于下标值的元素个数，从末尾遍历存放初值的数组A，找A[A.length-1]对应B数组的索引，
    //对应索引所存放的值就是A[A.length-1]在新数组C中的索引，存放完后，B数组索引对应的值-1.
    // 计数排序，快速排序 是数组，n 是数组大小。假设数组中存储的都是非负整数。
    public void countingSort(int[] a, int n) {
        if (n <= 1) return;

        // 查找数组中数据的范围
        int max = a[0];
        for (int i = 1; i < n; ++i) {
            if (max < a[i]) {
                max = a[i];
            }
        }

        int[] c = new int[max + 1]; // 申请一个计数数组 c，下标大小 [0,max]
        for (int i = 0; i <= max; ++i) {
            c[i] = 0;
        }

        // 计算每个元素的个数，放入 c 中
        for (int i = 0; i < n; ++i) {
            c[a[i]]++;
        }

        // 依次累加
        for (int i = 1; i <= max; ++i) {
            c[i] = c[i-1] + c[i];
        }

        // 临时数组 r，存储排序之后的结果
        int[] r = new int[n];
        // 计算排序的关键步骤，有点难理解
        for (int i = n - 1; i >= 0; --i) {
            int index = c[a[i]]-1;
            r[index] = a[i];
            c[a[i]]--;
        }

        // 将结果拷贝给 快速排序 数组
        for (int i = 0; i < n; ++i) {
            a[i] = r[i];
        }
    }

}
