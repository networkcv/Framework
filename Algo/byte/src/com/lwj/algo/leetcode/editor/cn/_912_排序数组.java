package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;
import java.util.Random;

/**
 * 给你一个整数数组 nums，请你将该数组升序排列。
 * <p>
 * 你必须在 不使用任何内置函数 的情况下解决问题，时间复杂度为 O(nlog(n))，并且空间复杂度尽可能小。
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：nums = [5,2,3,1]
 * 输出：[1,2,3,5]
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：nums = [5,1,1,2,0,0]
 * 输出：[0,0,1,1,2,5]
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= nums.length <= 5 * 10⁴
 * -5 * 10⁴ <= nums[i] <= 5 * 10⁴
 * <p>
 * <p>
 * Related Topics数组 | 分治 | 桶排序 | 计数排序 | 基数排序 | 排序 | 堆（优先队列） | 归并排序
 * <p>
 * 👍 1074, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class SortAnArray {
    public static void main(String[] args) {
        Solution solution = new SortAnArray().new Solution();
//        int[] ints = {4, 5, 4, 3};
        int[] ints = {5, 2, 3, 1};
        System.out.println(Arrays.toString(solution.sortArray(ints)));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] sortArray(int[] nums) {
//            quickSort(nums, 0, nums.length - 1);
            quickSort2(nums, 0, nums.length - 1);
            return nums;
        }

        private void quickSort(int[] nums, int l, int r) {
            if (l >= r) return;
            int[] pivotIndex = partition(nums, l, r);
            quickSort(nums, l, pivotIndex[0]);
            quickSort(nums, pivotIndex[1], r);
        }

        //三路快排
        private int[] partition(int[] nums, int l, int r) {
//            取随机元素作为切分元素, 防止因为有序数组导致时间复杂度退化
            int pivotIndex = l + new Random().nextInt(r - l + 1);
            swap(nums, l, pivotIndex);
            int pivot = nums[l];
            //lt为小于pivot区间的最后一个元素索引
            //gt为大pivot区间的第一个元素索引
            int lt = l;
            int gt = r + 1;
            //nums[l+1..lt] < pivot
            //nums(lt..i) = pivot
            //nums[gt..r] > pivot
            int i = l + 1;
            while (i < gt) {
                if (nums[i] > pivot) {
                    gt--;
                    swap(nums, i, gt);
                } else if (nums[i] < pivot) {
                    lt++;
                    swap(nums, i, lt);
                    i++;
                } else {
                    i++;
                }
            }
            swap(nums, l, lt);
            return new int[]{lt, gt};
        }

        private void quickSort2(int[] nums, int l, int r) {
            if (l >= r) return;
            int pivotIndex = partition0(nums, l, r);
            quickSort(nums, l, pivotIndex - 1);
            quickSort(nums, pivotIndex + 1, r);
        }

        //随机切分点快排
        private int partition1(int[] nums, int l, int r) {
            //取随机元素作为切分元素,防止因为有序数组导致时间复杂度退化
            int pivotIndex = l + new Random().nextInt(r - l + 1);
            swap(nums, l, pivotIndex);
            int pivot = nums[l];
            //j为小于pivot区间的最后一个元素
            int j = l;
            //nums[left+1..j] < pivot
            //nums(j..i) > pivot
            for (int i = l + 1; i <= r; i++) {
                if (nums[i] < pivot) {
                    j++;
                    swap(nums, i, j);
                }
            }
            swap(nums, l, j);
            return j;
        }

        //固定切分点快排
        private int partition0(int[] nums, int l, int r) {
            //取最左边元素作为切分元素
            int pivot = nums[l];
            //j为小于pivot区间的最后一个元素
            int j = l;
            //nums[left+1..j] < pivot
            //nums(j..i) > pivot
            for (int i = l + 1; i <= r; i++) {
                if (nums[i] < pivot) {
                    j++;
                    swap(nums, i, j);
                }
            }
            swap(nums, l, j);
            return j;
        }

        private void swap(int[] nums, int a, int b) {
            int temp = nums[a];
            nums[a] = nums[b];
            nums[b] = temp;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}