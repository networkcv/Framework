package com.lwj.algo.leetcode.editor.cn;

import java.util.Arrays;

class ZigzagConversion {
    public static void main(String[] args) {
        Solution solution = new ZigzagConversion().new Solution();
//        System.out.println(solution.convert("ABCDEFGHI", 3));
        System.out.println(solution.convert("ABCDEFGHI", 3).equals("AEIBDFHCG"));
        System.out.println(solution.convert("ABC", 1).equals("ABC"));
        System.out.println(solution.convert("AB", 1).equals("AB"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public String convert(String s, int numRows) {
            String[][] resArray = new String[numRows][s.length()];
            for (int i = 0; i < numRows; i++) {
                Arrays.fill(resArray[i], "");
            }
            int x = 0;
            int y = 0;
            boolean down = true;
            int xLimit = numRows - 1;
            char[] charArray = s.toCharArray();
            for (char c : charArray) {
                String str = String.valueOf(c);
                resArray[x][y] = str;
                if (down) {
                    if (x == xLimit) {
                        down = false;
                        x = Math.max(x - 1, 0);
                        y++;
                    } else {
                        x++;
                    }
                } else {
                    if (x == 0) {
                        down = true;
                        x = Math.min(x + 1, xLimit);
                        y++;
                    } else {
                        x--;
                        y++;
                    }
                }
            }
            StringBuilder res = new StringBuilder();
            for (String[] subRes : resArray) {
                res.append(String.join("", subRes));
            }
            return res.toString();
        }

    }
//leetcode submit region end(Prohibit modification and deletion)

}