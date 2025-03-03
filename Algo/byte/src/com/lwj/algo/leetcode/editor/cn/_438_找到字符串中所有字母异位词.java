package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//<p>给定两个字符串&nbsp;<code>s</code>&nbsp;和 <code>p</code>，找到&nbsp;<code>s</code><strong>&nbsp;</strong>中所有&nbsp;<code>p</code><strong>&nbsp;</strong>的&nbsp;<strong><span data-keyword="anagram">异位词</span>&nbsp;</strong>的子串，返回这些子串的起始索引。不考虑答案输出的顺序。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例&nbsp;1:</strong></p>
//
//<pre>
//<strong>输入: </strong>s = "cbaebabacd", p = "abc"
//<strong>输出: </strong>[0,6]
//<strong>解释:</strong>
//起始索引等于 0 的子串是 "cba", 它是 "abc" 的异位词。
//起始索引等于 6 的子串是 "bac", 它是 "abc" 的异位词。
//</pre>
//
//<p><strong>&nbsp;示例 2:</strong></p>
//
//<pre>
//<strong>输入: </strong>s = "abab", p = "ab"
//<strong>输出: </strong>[0,1,2]
//<strong>解释:</strong>
//起始索引等于 0 的子串是 "ab", 它是 "ab" 的异位词。
//起始索引等于 1 的子串是 "ba", 它是 "ab" 的异位词。
//起始索引等于 2 的子串是 "ab", 它是 "ab" 的异位词。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示:</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s.length, p.length &lt;= 3 * 10<sup>4</sup></code></li> 
// <li><code>s</code>&nbsp;和&nbsp;<code>p</code>&nbsp;仅包含小写字母</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>哈希表</li><li>字符串</li><li>滑动窗口</li></div></div><br><div><li>👍 1612</li><li>👎 0</li></div>
class FindAllAnagramsInAString {
    public static void main(String[] args) {
        Solution solution = new FindAllAnagramsInAString().new Solution();
        System.out.println(solution.findAnagrams("cbaebabacd", "abc"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {

        public List<Integer> findAnagrams(String s, String p) {
            List<Integer> res = new ArrayList<>();
            int[] sArr = new int[26];
            int[] pArr = new int[26];
            for (char c : p.toCharArray()) {
                pArr[c - 'a']++;
            }
            int len = p.length();
            int r = 0;
            int l = 0;
            while (r < s.length()) {
                char c = s.charAt(r);
                sArr[c - 'a']++;
                while ((r - l + 1) == len) {
                    if (Arrays.equals(sArr, pArr)) {
                        res.add(l);
                    }
                    sArr[s.charAt(l) - 'a']--;
                    l++;
                }
                r++;
            }
            return res;
        }

        //每次比较字符串时都需要先截取再排序
        public List<Integer> findAnagrams0(String str, String p) {
            List<Integer> res = new ArrayList<>();
            String sortP = sortString(p);
            int len = p.length();
            int f = 0;
            int s = 0;
            while (f < str.length()) {
                f++;
                while (s < f && (f - s + 1) > len) {
                    if (sortString(str.substring(s, f)).equals(sortP)) {
                        res.add(s);
                    }
                    s++;
                }
            }
            return res;
        }

        public String sortString(String s) {
            char[] charArray = s.toCharArray();
            Arrays.sort(charArray);
            return new String(charArray);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}