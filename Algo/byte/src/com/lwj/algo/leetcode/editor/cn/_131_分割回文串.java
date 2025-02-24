package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//<p>给你一个字符串 <code>s</code>，请你将<em> </em><code>s</code><em> </em>分割成一些子串，使每个子串都是 <strong><span data-keyword="palindrome-string">回文串</span></strong> 。返回 <code>s</code> 所有可能的分割方案。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "aab"
//<strong>输出：</strong>[["a","a","b"],["aa","b"]]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "a"
//<strong>输出：</strong>[["a"]]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s.length &lt;= 16</code></li> 
// <li><code>s</code> 仅由小写英文字母组成</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>字符串</li><li>动态规划</li><li>回溯</li></div></div><br><div><li>👍 1945</li><li>👎 0</li></div>
class PalindromePartitioning {
    public static void main(String[] args) {
        Solution solution = new PalindromePartitioning().new Solution();
        System.out.println(solution.partition("aab"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        List<List<String>> res = new ArrayList<>();
        LinkedList<String> path = new LinkedList<>();

        public List<List<String>> partition(String s) {
//            dfs(0, s);
            dfs1(0, 0, s);
            return res;
        }


        public boolean check(String str) {
            StringBuilder reverseSubStr = new StringBuilder(str).reverse();
            return str.contentEquals(reverseSubStr);
        }

        /**
         * 以答案视角
         */
        public void dfs(int i, String s) {
            if (i == s.length()) {
                res.add(new ArrayList<>(path));
            }
            for (int j = i; j < s.length(); j++) {
                String subStr = s.substring(i, j + 1);
                StringBuilder reverseSubStr = new StringBuilder(subStr).reverse();
                if (subStr.contentEquals(reverseSubStr)) {
                    path.add(subStr);
                    dfs(j + 1, s);
                    path.remove(path.size() - 1);
                }
            }
        }

        /**
         * 输入视角
         *
         * @param start 索引开始位置
         * @param i     当前分割位置
         * @param s     字符串
         */
        public void dfs1(int start, int i, String s) {
            if (i == s.length()) {
                res.add(new ArrayList<>(path));
                return;
            }
            if (i < s.length() - 1) {
                dfs1(start, i + 1, s);
            }
            String subStr = s.substring(start, i + 1);
            if (check(subStr)) {
                path.add(subStr);
                dfs1(i + 1, i + 1, s);
                path.removeLast();
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}