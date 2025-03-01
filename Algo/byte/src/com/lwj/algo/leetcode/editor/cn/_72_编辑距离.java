package com.lwj.algo.leetcode.editor.cn;

//<p>给你两个单词&nbsp;<code>word1</code> 和&nbsp;<code>word2</code>， <em>请返回将&nbsp;<code>word1</code>&nbsp;转换成&nbsp;<code>word2</code> 所使用的最少操作数</em> &nbsp;。</p>
//
//<p>你可以对一个单词进行如下三种操作：</p>
//
//<ul> 
// <li>插入一个字符</li> 
// <li>删除一个字符</li> 
// <li>替换一个字符</li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p><strong>示例&nbsp;1：</strong></p>
//
//<pre>
//<strong>输入：</strong>word1 = "horse", word2 = "ros"
//<strong>输出：</strong>3
//<strong>解释：</strong>
//horse -&gt; rorse (将 'h' 替换为 'r')
//rorse -&gt; rose (删除 'r')
//rose -&gt; ros (删除 'e')
//</pre>
//
//<p><strong>示例&nbsp;2：</strong></p>
//
//<pre>
//<strong>输入：</strong>word1 = "intention", word2 = "execution"
//<strong>输出：</strong>5
//<strong>解释：</strong>
//intention -&gt; inention (删除 't')
//inention -&gt; enention (将 'i' 替换为 'e')
//enention -&gt; exention (将 'n' 替换为 'x')
//exention -&gt; exection (将 'n' 替换为 'c')
//exection -&gt; execution (插入 'u')
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>0 &lt;= word1.length, word2.length &lt;= 500</code></li> 
// <li><code>word1</code> 和 <code>word2</code> 由小写英文字母组成</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>字符串</li><li>动态规划</li></div></div><br><div><li>👍 3601</li><li>👎 0</li></div>
class EditDistance {
    public static void main(String[] args) {
        Solution solution = new EditDistance().new Solution();
        System.out.println(solution.minDistance("horse", "ros"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        String word1;
        String word2;
        int[][] cache;

        public int minDistance(String word1, String word2) {
            this.word1 = word1;
            this.word2 = word2;
//            return dfs0(word1.length() - 1, word2.length() - 1);

//            cache = new int[word1.length() + 1][word2.length() + 1];
//            return dfs1(word1.length() - 1, word2.length() - 1);

//            return dp0(word1, word2);
            return dp1(word1, word2);
        }


        //递推版-只有两行的二维数组
        public int dp1(String word1, String word2) {
            char[] s = word1.toCharArray();
            char[] t = word2.toCharArray();
            int len1 = word1.length();
            int len2 = word2.length();
            int[][] dp = new int[2][len2 + 1];
            //行为word1的下标，列为word2的下标，当两个都为0的时候，
            dp[0][0] = 0;
            for (int j = 1; j <= len2; j++) {
                dp[0][j] = j;
            }
            for (int i = 0; i < len1; i++) {
                dp[(i + 1) % 2][0] = i + 1;
                for (int j = 0; j < len2; j++) {
                    if (s[i] == t[j]) {
                        dp[(i + 1) % 2][j + 1] = dp[i % 2][j];
                    } else {
                        dp[(i + 1) % 2][j + 1] = Math.min(Math.min(dp[i % 2][j + 1], dp[(i + 1) % 2][j]), dp[i % 2][j]) + 1;
                    }
                }
            }
            return dp[len1 % 2][len2];
        }

        //递推版
        public int dp0(String word1, String word2) {
            char[] s = word1.toCharArray();
            char[] t = word2.toCharArray();
            int len1 = word1.length();
            int len2 = word2.length();
            int[][] dp = new int[len1 + 1][len2 + 1];
            //行为word1的下标，列为word2的下标，当两个都为0的时候，
            dp[0][0] = 0;
            //当行不为0且列为0的时候，word1还没处理完，word2已经为空了，这时候需要把word1全部删掉才和word2一样
            for (int i = 1; i <= len1; i++) {
                dp[i][0] = i;
            }
            //同上
            for (int j = 1; j <= len2; j++) {
                dp[0][j] = j;
            }
            for (int i = 0; i < len1; i++) {
                for (int j = 0; j < len2; j++) {
                    if (s[i] == t[j]) {
                        dp[i + 1][j + 1] = dp[i][j];
                    } else {
                        dp[i + 1][j + 1] = Math.min(Math.min(dp[i][j + 1], dp[i + 1][j]), dp[i][j]) + 1;
                    }
                }
            }
            return dp[len1][len2];
        }

        //回溯版-记忆化搜索
        public int dfs1(int i, int j) {
            if (i < 0) {
                return j + 1;
            }
            if (j < 0) {
                return i + 1;
            }
            if (cache[i][j] != 0) {
                return cache[i][j];
            }
            char c1 = word1.charAt(i);
            char c2 = word2.charAt(j);
            //最后的两个字符相同，则比较前边的元素
            if (c1 == c2) {
                return dfs1(i - 1, j - 1);
            }
            //最后两个元素不同，则通过删除、插入、替换的方式使两个元素相同
            return cache[i][j] = Math.min(Math.min(dfs1(i - 1, j), dfs1(i, j - 1)), dfs1(i - 1, j - 1)) + 1;


        }

        //回溯版-暴力递归
        public int dfs0(int i, int j) {
            //注意这里的边界条件，当一个字符串遍历完还未与另外的字符串完全匹配，那只能把另外字符串剩余的部分全部删除，由于是从后向前遍历，
            //且i和j索引，那剩余长度就是i+1或者j+1
            if (i < 0) {
                return j + 1;
            }
            if (j < 0) {
                return i + 1;
            }
            char c1 = word1.charAt(i);
            char c2 = word2.charAt(j);
            //最后的两个字符相同，则比较前边的元素
            if (c1 == c2) {
                return dfs0(i - 1, j - 1);
            }
            //最后两个元素不同，则通过删除、插入、替换的方式使两个元素相同
            return Math.min(Math.min(dfs0(i - 1, j), dfs0(i, j - 1)), dfs0(i - 1, j - 1)) + 1;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}