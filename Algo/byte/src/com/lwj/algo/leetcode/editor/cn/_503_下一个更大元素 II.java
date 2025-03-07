package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ArraysUtil;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;

//<p>给定一个循环数组&nbsp;<code>nums</code>&nbsp;（&nbsp;<code>nums[nums.length - 1]</code>&nbsp;的下一个元素是&nbsp;<code>nums[0]</code>&nbsp;），返回&nbsp;<em><code>nums</code>&nbsp;中每个元素的 <strong>下一个更大元素</strong></em> 。</p>
//
//<p>数字 <code>x</code>&nbsp;的 <strong>下一个更大的元素</strong> 是按数组遍历顺序，这个数字之后的第一个比它更大的数，这意味着你应该循环地搜索它的下一个更大的数。如果不存在，则输出 <code>-1</code>&nbsp;。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<pre>
//<strong>输入:</strong> nums = [1,2,1]
//<strong>输出:</strong> [2,-1,2]
//<strong>解释:</strong> 第一个 1 的下一个更大的数是 2；
//数字 2 找不到下一个更大的数； 
//第二个 1 的下一个最大的数需要循环搜索，结果也是 2。
//</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<pre>
//<strong>输入:</strong> nums = [1,2,3,4,3]
//<strong>输出:</strong> [2,3,4,-1,4]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示:</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 10<sup>4</sup></code></li> 
// <li><code>-10<sup>9</sup>&nbsp;&lt;= nums[i] &lt;= 10<sup>9</sup></code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>栈</li><li>数组</li><li>单调栈</li></div></div><br><div><li>👍 1036</li><li>👎 0</li></div>
class NextGreaterElementIi {
    public static void main(String[] args) {
        Solution solution = new NextGreaterElementIi().new Solution();
        ArraysUtil.print(solution.nextGreaterElements(new int[]{1, 2, 1}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {


        public int[] nextGreaterElements(int[] nums) {
            int n = nums.length;
            int[] ans = new int[n];
            Arrays.fill(ans, -1);
            Deque<Integer> st = new ArrayDeque<>();
            for (int i = 0; i < 2 * n; i++) {
                int x = nums[i % n];
                while (!st.isEmpty() && x > nums[st.peek()]) {
                    ans[st.pop()] = x;
                }
                //栈里存放的的都是需要求下一个最大值的元素
                //所以这里判断如果超过nums的长度的话就不往栈里添加元素了
                if (i < n) {
                    st.push(i);
                }
            }
            return ans;
        }

        //通过取模代替复制元素
        public int[] nextGreaterElements1(int[] nums) {
            int[] res = new int[nums.length];
            Arrays.fill(res, -1);
            Deque<Integer> stack = new LinkedList<>();
            int len = nums.length;
            for (int i = 0; i < len * 2 - 1; i++) {
                while (!stack.isEmpty() && nums[i % len] > nums[stack.peek() % len]) {
                    res[stack.pop() % len] = nums[i % len];
                }
                stack.push(i % len);
            }
            return res;
        }

        public int[] nextGreaterElements0(int[] nums) {
            int len = nums.length;
            int[] nums2 = new int[len * 2];
            System.arraycopy(nums, 0, nums2, 0, len);
            System.arraycopy(nums, 0, nums2, len, len);

            int[] tmpRes = new int[nums2.length];
            Arrays.fill(tmpRes, -1);
            Deque<Integer> stack = new LinkedList<>();
            for (int j = 0; j < nums2.length; j++) {
                while (!stack.isEmpty() && nums2[j] > nums2[stack.peek()]) {
                    tmpRes[stack.pop()] = nums2[j];
                }
                stack.push(j);
            }
            int[] res = new int[len];
            System.arraycopy(tmpRes, 0, res, 0, len);
            return res;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}