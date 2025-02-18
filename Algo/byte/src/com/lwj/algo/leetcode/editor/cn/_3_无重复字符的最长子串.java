package com.lwj.algo.leetcode.editor.cn;

import java.util.HashMap;

class LongestSubstringWithoutRepeatingCharacters {
    public static void main(String[] args) {
        Solution solution = new LongestSubstringWithoutRepeatingCharacters().new Solution();
//        System.out.println(solution.lengthOfLongestSubstring("wwkew"));
        System.out.println(solution.lengthOfLongestSubstring("pwwkew"));
//        System.out.println(solution.lengthOfLongestSubstring("a"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int lengthOfLongestSubstring(String str) {
            HashMap<Character, Integer> map = new HashMap<>();
            int res = 0;
            int q = 0, s = 0;
            while (q < str.length()) {
                char c = str.charAt(q++);
                map.merge(c, 1, Integer::sum);
                while (map.get(c) > 1) {
                    char d = str.charAt(s++);
                    map.computeIfPresent(d, (k, v) -> v - 1);
                }
                res = Math.max(res, q - s);
            }
            return res;
        }

        public int lengthOfLongestSubstring0(String s) {
            HashMap<Character, Integer> map = new HashMap<>();
            int res = 0;
            int l = 0, r = 0;
            int len = s.length();
            while (r < len) {
                char c = s.charAt(r);
                r++;
                map.merge(c, 1, Integer::sum);
                while (map.get(c) > 1) {
                    char d = s.charAt(l);
                    map.computeIfPresent(d, (k, v) -> v - 1);
                    l++;
                }
                res = Math.max(res, r - l);

            }
            return res;
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}