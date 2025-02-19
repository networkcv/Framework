package com.lwj.algo.leetcode.editor.cn;

/**
 * 整数数组 nums 按升序排列，数组中的值 互不相同 。
 * <p>
 * 在传递给函数之前，nums 在预先未知的某个下标 k（0 <= k < nums.length）上进行了 旋转，使数组变为 [nums[k], nums[k+
 * 1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]（下标 从 0 开始 计数）。例如， [0,1,2,4
 * ,5,6,7] 在下标 3 处经旋转后可能变为 [4,5,6,7,0,1,2] 。
 * <p>
 * 给你 旋转后 的数组 nums 和一个整数 target ，如果 nums 中存在这个目标值 target ，则返回它的下标，否则返回 -1 。
 * <p>
 * 你必须设计一个时间复杂度为 O(log n) 的算法解决此问题。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：nums = [4,5,6,7,0,1,2], target = 0
 * 输出：4
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：nums = [4,5,6,7,0,1,2], target = 3
 * 输出：-1
 * <p>
 * 示例 3：
 * <p>
 * <p>
 * 输入：nums = [1], target = 0
 * 输出：-1
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 1 <= nums.length <= 5000
 * -10⁴ <= nums[i] <= 10⁴
 * nums 中的每个值都 独一无二
 * 题目数据保证 nums 在预先未知的某个下标上进行了旋转
 * -10⁴ <= target <= 10⁴
 * <p>
 * <p>
 * Related Topics数组 | 二分查找
 * <p>
 * 👍 3103, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class SearchInRotatedSortedArray {
    public static void main(String[] args) {
        Solution solution = new SearchInRotatedSortedArray().new Solution();
        System.out.println(solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 0));
//        System.out.println(solution.search(new int[]{4, 5, 6, 7, 0, 1, 2}, 3));
//        System.out.println(solution.search(new int[]{3, 1}, 3));
//        System.out.println(solution.search(new int[]{1, 3}, 3));
//        System.out.println(solution.search(new int[]{1}, 1));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //[4,5,6,7,0,1,2], target = 3
        public int search(int[] nums, int target) {
            //思路二：同时比较target和最后一个数
            //左区间表示target左侧元素，右区间表示target及target右侧元素
            int l = 0;
            int len = nums.length;
            int r = len - 1;
            int lastNum = nums[len - 1];
            while (l <= r) {
                int mid = (l + r) / 2;
                if (isRight(nums[mid], lastNum, target)) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            return (l == len || nums[l] != target) ? -1 : l;
        }

        public boolean isRight(int midNum, int lastNum, int target) {
            if (midNum <= lastNum) {
                if (midNum >= target) {
                    return true;
                }
                if (target > lastNum) {
                    return true;
                }
            }
            if (midNum > lastNum) {
                if (target <= midNum && target > lastNum) {
                    return true;
                }
            }
            return false;
        }

        public int search1(int[] nums, int target) {
            //思路一：结合寻找旋转排序数组中最小值该思路，将原数组拆分成两个有序数组，然后再二分查找target
            int minIndex = findMinIndex(nums);
            int l;
            int r;
            if (minIndex == 0) {
                //单调递增数组
                l = 0;
                r = nums.length - 1;
            } else {
                //最小值在中间的数组
                if (target >= nums[0]) {
                    //target在前半数组
                    l = 0;
                    r = Math.max(minIndex - 1, 0);
                } else {
                    l = minIndex;
                    r = nums.length - 1;
                }
            }
            //左区间表示<target的数字，右区间表示>=target的数字
            while (l <= r) {
                int mid = (l + r) / 2;
                if (nums[mid] < target) {
                    l = mid + 1;
                } else {
                    r = mid - 1;
                }
            }
            if (l == nums.length) {
                return -1;
            }
            return nums[l] == target ? l : -1;
        }


        public int findMinIndex(int[] nums) {
            int l = 0, r = nums.length - 2;
            while (l <= r) {
                int mid = l + (r - l) / 2;
                if (nums[mid] < nums[nums.length - 1]) {
                    r = mid - 1;
                } else {
                    l = mid + 1;
                }
            }
            return l;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}