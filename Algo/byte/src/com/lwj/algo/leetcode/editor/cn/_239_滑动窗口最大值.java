package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ArraysUtil;

import java.util.Deque;
import java.util.LinkedList;

//<p>给你一个整数数组 <code>nums</code>，有一个大小为&nbsp;<code>k</code><em>&nbsp;</em>的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 <code>k</code>&nbsp;个数字。滑动窗口每次只向右移动一位。</p>
//
//<p>返回 <em>滑动窗口中的最大值 </em>。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<b>输入：</b>nums = [1,3,-1,-3,5,3,6,7], k = 3
//<b>输出：</b>[3,3,5,5,6,7]
//<b>解释：</b>
//滑动窗口的位置                最大值
//---------------               -----
//[1  3  -1] -3  5  3  6  7       <strong>3</strong>
// 1 [3  -1  -3] 5  3  6  7       <strong>3</strong>
// 1  3 [-1  -3  5] 3  6  7      <strong> 5</strong>
// 1  3  -1 [-3  5  3] 6  7       <strong>5</strong>
// 1  3  -1  -3 [5  3  6] 7       <strong>6</strong>
// 1  3  -1  -3  5 [3  6  7]      <strong>7</strong>
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<b>输入：</b>nums = [1], k = 1
//<b>输出：</b>[1]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><b>提示：</b></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 10<sup>5</sup></code></li> 
// <li><code>-10<sup>4</sup>&nbsp;&lt;= nums[i] &lt;= 10<sup>4</sup></code></li> 
// <li><code>1 &lt;= k &lt;= nums.length</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>队列</li><li>数组</li><li>滑动窗口</li><li>单调队列</li><li>堆（优先队列）</li></div></div><br><div><li>👍 3043</li><li>👎 0</li></div>
class SlidingWindowMaximum {
    public static void main(String[] args) {
        Solution solution = new SlidingWindowMaximum().new Solution();
        ArraysUtil.print(solution.maxSlidingWindow(new int[]{1, 3, -1, -3, 5, 3, 6, 7}, 3));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //双端单调队列记录滑动窗口内的数据从大到小排列，
        public int[] maxSlidingWindow(int[] nums, int k) {
            int len = nums.length;
            int[] res = new int[len - k + 1];
            Deque<Integer> deque = new LinkedList<>();
            for (int i = 0; i < len; i++) {
                //入队，进入前需要保证队列单调递减，所以要先移除所有队列中比当前值小的元素
                while (!deque.isEmpty() && nums[deque.getLast()] < nums[i]) {
                    deque.removeLast();
                }
                deque.addLast(i);
                //出队，滑动串口右移一位后，右边前窗口内最左边的元素会离开窗口，所以此时该元素也需要从单调队列中移除
                if (i - deque.getFirst() + 1 > k) {
                    deque.removeFirst();
                }
                //记录
                if (i + 1 - k >= 0) {
                    res[i + 1 - k] = nums[deque.getFirst()];
                }
            }
            return res;

        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}