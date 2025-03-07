package com.lwj.algo.leetcode.editor.cn;


import com.lwj.algo.leetcode.editor.cn.utils.ArraysUtil;

import java.util.Deque;
import java.util.LinkedList;

//<p>给定一个整数数组&nbsp;<code>temperatures</code>&nbsp;，表示每天的温度，返回一个数组&nbsp;<code>answer</code>&nbsp;，其中&nbsp;<code>answer[i]</code>&nbsp;是指对于第 <code>i</code> 天，下一个更高温度出现在几天后。如果气温在这之后都不会升高，请在该位置用&nbsp;<code>0</code> 来代替。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<pre>
//<strong>输入:</strong> <span><code>temperatures</code></span> = [73,74,75,71,69,72,76,73]
//<strong>输出:</strong>&nbsp;[1,1,4,2,1,1,0,0]
//</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<pre>
//<strong>输入:</strong> temperatures = [30,40,50,60]
//<strong>输出:</strong>&nbsp;[1,1,1,0]
//</pre>
//
//<p><strong>示例 3:</strong></p>
//
//<pre>
//<strong>输入:</strong> temperatures = [30,60,90]
//<strong>输出: </strong>[1,1,0]</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;=&nbsp;temperatures.length &lt;= 10<sup>5</sup></code></li> 
// <li><code>30 &lt;=&nbsp;temperatures[i]&nbsp;&lt;= 100</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>栈</li><li>数组</li><li>单调栈</li></div></div><br><div><li>👍 1913</li><li>👎 0</li></div>
class DailyTemperatures {
    public static void main(String[] args) {
        Solution solution = new DailyTemperatures().new Solution();
        ArraysUtil.print(solution.dailyTemperatures(new int[]{73, 74, 75, 71, 69, 72, 76, 73}));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {


        //单调栈 从右向左遍历
        public int[] dailyTemperatures0(int[] temperatures) {
            int[] res = new int[temperatures.length];
            Deque<Integer> stack = new LinkedList<>();
            for (int i = temperatures.length - 1; i >= 0; i--) {
                while (!stack.isEmpty() && temperatures[i] >= temperatures[stack.peek()]) {
                    stack.poll();
                }
                res[i] = stack.isEmpty() ? 0 : stack.peek() - i;
                stack.push(i);
            }
            return res;
        }


    }
//leetcode submit region end(Prohibit modification and deletion)

}