//<p>给你一个字符串 <code>s</code> 、一个字符串 <code>t</code> 。返回 <code>s</code> 中涵盖 <code>t</code> 所有字符的最小子串。如果 <code>s</code> 中不存在涵盖 <code>t</code> 所有字符的子串，则返回空字符串 <code>""</code> 。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>注意：</strong></p>
//
//<ul> 
// <li>对于 <code>t</code> 中重复字符，我们寻找的子字符串中该字符数量必须不少于 <code>t</code> 中该字符数量。</li> 
// <li>如果 <code>s</code> 中存在这样的子串，我们保证它是唯一的答案。</li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "ADOBECODEBANC", t = "ABC"
//<strong>输出：</strong>"BANC"
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "a", t = "a"
//<strong>输出：</strong>"a"
//</pre>
//
//<p><strong>示例 3:</strong></p>
//
//<pre>
//<strong>输入:</strong> s = "a", t = "aa"
//<strong>输出:</strong> ""
//<strong>解释:</strong> t 中两个字符 'a' 均应包含在 s 的子串中，
//因此没有符合条件的子字符串，返回空字符串。</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s.length, t.length &lt;= 10<sup>5</sup></code></li> 
// <li><code>s</code> 和 <code>t</code> 由英文字母组成</li> 
//</ul>
//
//<p>&nbsp;</p> 
//<strong>进阶：</strong>你能设计一个在 
//<code>o(n)</code> 时间内解决此问题的算法吗？
//
//<div><div>Related Topics</div><div><li>哈希表</li><li>字符串</li><li>滑动窗口</li></div></div><br><div><li>👍 2033</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.HashMap;

class MinimumWindowSubstring {
    public static void main(String[] args) {
        Solution solution = new MinimumWindowSubstring().new Solution();
//        System.out.println(solution.minWindow("cabwefgewcwaefgcf", "cae")); //cwae
        System.out.println(solution.minWindow("a", "aa")); //""
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //最小覆盖串
        public String minWindow(String s, String t) {
            HashMap<Character, Integer> need = new HashMap<>();
            HashMap<Character, Integer> windows = new HashMap<>();

            int left = 0, right = 0, valid = 0;
            int start = 0, end = s.length(), len = Integer.MAX_VALUE;

            for (char c : t.toCharArray()) {
                need.merge(c, 1, Integer::sum);
                //等同下边这段代码
//                Integer count = need.get(c);
//                if (count == null) {
//                    need.put(c, 1);
//                } else {
//                    need.put(c, c + 1);
//                }
            }
            //窗口扩张  目的是找个满足t的子串
            while (right < s.length()) {
                //滑动中当前要处理的字符
                char c = s.charAt(right);
                right++;
                //进入窗口的字符
                if (need.containsKey(c)) {
                    windows.merge(c, 1, Integer::sum);
                    if (need.get(c).equals(windows.get(c))) {
                        valid++;
                    }
                }
                //窗口收缩 将刚才的子串取出最小覆盖的部分
                while (valid == need.size()) {
                    //和以往的最小覆盖子串相比较
                    if (right - left < len) {
                        start = left;
                        end = right;
                        len = right - left;
                    }
                    char d = s.charAt(left);
                    left++;
                    if (need.containsKey(d)) {
                        if (windows.get(d).equals(need.get(d))) {
                            valid--;
                        }
                        windows.computeIfPresent(d, (k, v) -> v - 1);
                    }
                }
            }
            return len == Integer.MAX_VALUE ? "" : s.substring(start, end);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}