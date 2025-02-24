package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.List;

//<p>数字 <code>n</code>&nbsp;代表生成括号的对数，请你设计一个函数，用于能够生成所有可能的并且 <strong>有效的 </strong>括号组合。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>n = 3
//<strong>输出：</strong>["((()))","(()())","(())()","()(())","()()()"]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>n = 1
//<strong>输出：</strong>["()"]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= n &lt;= 8</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>字符串</li><li>动态规划</li><li>回溯</li></div></div><br><div><li>👍 3777</li><li>👎 0</li></div>
class GenerateParentheses {
    public static void main(String[] args) {
        Solution solution = new GenerateParentheses().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<String> res = new ArrayList<>();

        int m;
        int n;

        //在2n个位置中选择n个位置放左括号
        public List<String> generateParenthesis(int n) {
            this.n = n;
            this.m = n * 2;
            dfs(0, 0, "");
            return res;
        }

        public void dfs(int i, int leftNum, String path) {
            //总数到达最后一个时返回
            if (i == m) {
                res.add(path);
                return;
            }
            //左括号没到一半时，可以选左括号
            if (leftNum < n) {
                dfs(i + 1, leftNum + 1, path + "(");
            }
            //右括号少于左括号时，可以选右括号
            if (i - leftNum < leftNum) {
                dfs(i + 1, leftNum, path + ")");
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}