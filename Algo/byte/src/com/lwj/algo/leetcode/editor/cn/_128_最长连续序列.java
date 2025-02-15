package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 给定一个未排序的整数数组 nums ，找出数字连续的最长序列（不要求序列元素在原数组中连续）的长度。
 * <p>
 * 请你设计并实现时间复杂度为 O(n) 的算法解决此问题。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * 输入：nums = [100,4,200,1,3,2]
 * 输出：4
 * 解释：最长数字连续序列是 [1, 2, 3, 4]。它的长度为 4。
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：nums = [0,3,7,2,5,8,4,6,0,1]
 * 输出：9
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * 0 <= nums.length <= 10⁵
 * -10⁹ <= nums[i] <= 10⁹
 * <p>
 * <p>
 * Related Topics并查集 | 数组 | 哈希表
 * <p>
 * 👍 2362, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class LongestConsecutiveSequence {
    public static void main(String[] args) {
        Solution solution = new LongestConsecutiveSequence().new Solution();
//        System.out.println(solution.longestConsecutive(new int[]{9, 1, 4, 7, 3, -1, 0, 5, 8, -1, 6}));
//        System.out.println(solution.longestConsecutive(new int[]{100, 4, 200, 1, 3, 2}));
        System.out.println(solution.longestConsecutive(new int[]{1, 2, 0, 1}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //空间换时间 遍历的时候从当前元素相两遍遍历，通过hash表来维护是否被访问
        public int longestConsecutive1(int[] nums) {
            HashMap<Integer, AtomicInteger> map = new HashMap<>();
            for (int num : nums) {
                map.put(num, new AtomicInteger(1));
            }
            AtomicInteger res = new AtomicInteger();
            map.keySet().forEach(num -> {
                if (map.get(num) == null || map.get(num).get() == 0) {
                    return;
                }
                int count = 1;
                AtomicInteger data;
                int pre = num - 1;
                while ((data = map.get(pre--)) != null && data.get() != 0) {
                    count++;
                    data.set(0);
                }
                int next = num + 1;
                while ((data = map.get(next++)) != null && data.get() != 0) {
                    count++;
                    data.set(0);
                }
                res.set(Math.max(res.get(), count));
            });
            return res.get();
        }

        //暴力法 先排序 再遍历计数
        public int longestConsecutive(int[] nums) {
            int len = nums.length;
            if (len < 2) {
                return len;
            }
            Arrays.sort(nums);
            int res = 0;
            int count = 1;
            for (int i = 1; i < nums.length; i++) {
                if (nums[i] == nums[i - 1]) {
                    continue;
                }
                if (nums[i] == nums[i - 1] + 1) {
                    count++;
                } else {
                    res = Math.max(res, count);
                    count = 1;
                }
            }
            res = Math.max(res, count);
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}