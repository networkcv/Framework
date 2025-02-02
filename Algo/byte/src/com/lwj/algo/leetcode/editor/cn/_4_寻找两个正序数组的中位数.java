/**
 * 给定两个大小分别为 m 和 n 的正序（从小到大）数组 nums1 和 nums2。请你找出并返回这两个正序数组的 中位数 。
 * <p>
 * 算法的时间复杂度应该为 O(log (m+n)) 。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：nums1 = [1,3], nums2 = [2]
 * 输出：2.00000
 * 解释：合并数组 = [1,2,3] ，中位数 2
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：nums1 = [1,2], nums2 = [3,4]
 * 输出：2.50000
 * 解释：合并数组 = [1,2,3,4] ，中位数 (2 + 3) / 2 = 2.5
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * nums1.length == m
 * nums2.length == n
 * 0 <= m <= 1000
 * 0 <= n <= 1000
 * 1 <= m + n <= 2000
 * -10⁶ <= nums1[i], nums2[i] <= 10⁶
 * <p>
 * <p>
 * Related Topics数组 | 二分查找 | 分治
 * <p>
 * 👍 7359, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */

package com.lwj.algo.leetcode.editor.cn;

class MedianOfTwoSortedArrays {
    public static void main(String[] args) {
        Solution solution = new MedianOfTwoSortedArrays().new Solution();
//        System.out.println(solution.findMedianSortedArrays(new int[]{4, 5}, new int[]{1, 2, 3}));
//        System.out.println(solution.findMedianSortedArrays(new int[]{1, 3}, new int[]{2}));
        System.out.println(solution.findMedianSortedArrays(new int[]{1}, new int[]{2}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        /**
         * 数组的中位数会因为元素个数奇偶存在差异，奇数数组则有一个中位数，偶数数组则需要将两个中位数取平均。
         * 可以通过公式来屏蔽奇偶的差异:((len+1)/2+(len+2)/2)*0.5
         * 找到数组中第((len+1)/2)个数和第((len+2)/2)个数进行平均
         * 例如：
         * 1，2，3，4，5     ((5+1)/2+(5+2)/2)*0.5=(3+3)*0.5=3
         * 1，2，3，4，5，6  ((6+1)/2+(6+2)/2)*0.5=(3+4)*0.5=3.5
         */
        public double findMedianSortedArrays(int[] nums1, int[] nums2) {
            int len1 = nums1.length;
            int len2 = nums2.length;
            return (getK(nums1, 0, len1 - 1, nums2, 0, len2 - 1, (len1 + len2 + 1) / 2) +
                    getK(nums1, 0, len1 - 1, nums2, 0, len2 - 1, (len1 + len2 + 2) / 2)) * 0.5;
        }

        /**
         * 获取两个有序数组中合并后的第K个数
         * <p>
         * 2,4,6
         * <p>
         * 1,3,5,7
         *
         * @param nums1 数组1
         * @param l1    数组1的起始索引
         * @param r1    数组1的结束索引
         * @param nums2 数组1
         * @param l2    数组2的起始索引
         * @param r2    数组2的结束索引
         * @param k     第k个数
         * @return 两个数组合并后的第K个数
         */
        public int getK(int[] nums1, int l1, int r1, int[] nums2, int l2, int r2, int k) {
            int len1 = r1 - l1 + 1;
            int len2 = r2 - l2 + 1;
            if (len1 > len2) {
                //保证num1长度始终小于nums2
                return getK(nums2, l2, r2, nums1, l1, r1, k);
            }
            if (len1 == 0) {
                return nums2[l2 + k - 1];
            }
            if (k == 1) {
                return Math.min(nums1[l1], nums2[l2]);
            }
            int idx1 = l1 + Math.min(len1, k / 2) - 1;
            int idx2 = l2 + Math.min(len2, k / 2) - 1;
            if (nums1[idx1] < nums2[idx2]) {
                //两个数组都截取k/2个数，比较两个新截取数组末尾元素的大小，比较结果小的那个数组则一定不包含第k个元素,
                //则该截取数组可以从原数组比较范围中舍去。舍去后，k的值也会发生变化，需要减去舍去的数组范围。
                //比如之前是7个数里找第四个，4/2=2，舍去两个之后就变成了5个里边找第三个
                return getK(nums1, idx1 + 1, r1, nums2, l2, r2, k - (idx1 - l1 + 1));
            }
            return getK(nums1, l1, r1, nums2, idx2 + 1, r2, k - (idx2 - l2 + 1));
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}