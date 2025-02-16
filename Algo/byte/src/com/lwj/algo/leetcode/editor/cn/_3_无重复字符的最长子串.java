package com.lwj.algo.leetcode.editor.cn;

import java.util.HashMap;

class LongestSubstringWithoutRepeatingCharacters {
    public static void main(String[] args) {
        Solution solution = new LongestSubstringWithoutRepeatingCharacters().new Solution();
        System.out.println(solution.lengthOfLongestSubstring("wwkew"));
//        System.out.println(solution.lengthOfLongestSubstring("pwwkew"));
//        System.out.println(solution.lengthOfLongestSubstring("a"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //// TODO: 乌柏 2025/2/16  error
        public int lengthOfLongestSubstring(String s) {
            HashMap<Character, Integer> map = new HashMap<>();
            int res = 0;
            int l = 0, r = 0;
            int len = s.length();
            while (r < len) {
                char c = s.charAt(r);
                r++;
                map.merge(c, 1, Integer::sum);
                while (map.get(c) > 1) {
                    l++;
                    map.computeIfPresent(c, (k, v) -> v - 1);
                }
                res = Math.max(res, r - l);

            }
            return res;
        }

        public int lengthOfLongestSubstring1(String s) {
            int res = 0;
            int l = 0, r = 0;
            HashMap<Character, Integer> map = new HashMap<>();
            while (r < s.length()) {
                char c = s.charAt(r);
                Integer cNum = map.getOrDefault(c, 0);
                if (cNum == 0) {
                    map.put(c, cNum + 1);
                    r++;
                    res = Math.max(res, r - l);
                    continue;
                }

                while (map.get(c) > 0) {
                    char d = s.charAt(l);
                    map.put(d, map.get(d) - 1);
                    l++;
                }
            }
            return res;
        }

        public int lengthOfLongestSubstring2(String s) {
            HashMap<Character, Integer> windows = new HashMap<>();
            int left = 0, right = 0;
            int res = 0;
            while (right < s.length()) {
                char c = s.charAt(right);
                right++;
                windows.merge(c, 1, Integer::sum);
                while (windows.get(c) > 1) {
                    char d = s.charAt(left);
                    left++;
                    windows.computeIfPresent(d, (k, v) -> v - 1);
                }
                res = Math.max(res, right - left);
            }
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}