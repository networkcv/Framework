package com.lwj.algo.leetcode.editor.cn;

import java.util.Deque;
import java.util.LinkedList;

/**
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * <p>
 * <p>
 * <p>
 * 输入：height = [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出：6
 * 解释：上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。
 * <p>
 * <p>
 * 示例 2：
 * <p>
 * <p>
 * 输入：height = [4,2,0,3,2,5]
 * 输出：9
 * <p>
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * <p>
 * n == height.length
 * 1 <= n <= 2 * 10⁴
 * 0 <= height[i] <= 10⁵
 * <p>
 * <p>
 * Related Topics栈 | 数组 | 双指针 | 动态规划 | 单调栈
 * <p>
 * 👍 5515, 👎 0bug 反馈 | 使用指南 | 更多配套插件
 */
class TrappingRainWater {
    public static void main(String[] args) {
        Solution solution = new TrappingRainWater().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //单调栈，横着接雨水
        //保持单调栈中元素自底向上是逐渐递增的
        public int trap(int[] heights) {
            int res = 0;
            Deque<Integer> stack = new LinkedList<>();
            for (int i = 0; i < heights.length; i++) {
                //当前元素cur大于栈顶元素top的是就可以横着计算雨水面积了，此时还需要栈顶元素下面的元素pre，也就是数组中栈顶元素左边的元素pre，
                //左边元素pre一定是大于栈顶元素top的，此时p和cur之间的距离就是雨水面积的长，Math.min(pre,cur)-top就是雨水面积的宽，乘积就是top位置的接水面积
                while (!stack.isEmpty() && heights[i] > heights[stack.peek()]) {
                    Integer top = stack.pop();
                    if (stack.isEmpty()) break;
                    Integer pre = stack.peek();
                    int height = Math.min(heights[pre], heights[i]) - heights[top];
                    int weight = i - pre - 1;
                    res += height * weight;
                }
                stack.push(i);
            }
            return res;
        }

        //时间复杂度O(n) 空间负责度O(1)
        public int trap1(int[] height) {
            //左边最大高度
            int preMax = height[0];
            //右边最大高度
            int sufMax = height[height.length - 1];
            int l = 0, r = height.length - 1;
            int res = 0;
            while (l < r) {
                preMax = Math.max(preMax, height[l]);
                sufMax = Math.max(sufMax, height[r]);
                if (preMax < sufMax) {
                    res += preMax - height[l];
                    l++;
                } else {
                    res += sufMax - height[r];
                    r--;
                }
            }
            return res;
        }

        //时间复杂度O(n) 空间负责度O(n)
        public int trap0(int[] height) {
            int[] preMax = new int[height.length];
            preMax[0] = height[0];
            for (int i = 1; i < height.length; i++) {
                preMax[i] = Math.max(height[i], preMax[i - 1]);
            }
            int[] sufMax = new int[height.length];
            sufMax[height.length - 1] = height[height.length - 1];
            for (int i = height.length - 2; i >= 0; i--) {
                sufMax[i] = Math.max(height[i], sufMax[i + 1]);
            }
            int res = 0;
            for (int i = 0; i < height.length; i++) {
                res += Math.min(preMax[i], sufMax[i]) - height[i];
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}