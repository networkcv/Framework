package com.lwj.algo.leetcode.editor.cn;

//<p>给你一个 32 位的有符号整数 <code>x</code> ，返回将 <code>x</code> 中的数字部分反转后的结果。</p>
//
//<p>如果反转后整数超过 32 位的有符号整数的范围&nbsp;<code>[−2<sup>31</sup>,&nbsp; 2<sup>31&nbsp;</sup>− 1]</code> ，就返回 0。</p> 
//<strong>假设环境不允许存储 64 位整数（有符号或无符号）。</strong>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>x = 123
//<strong>输出：</strong>321
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>x = -123
//<strong>输出：</strong>-321
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>x = 120
//<strong>输出：</strong>21
//</pre>
//
//<p><strong>示例 4：</strong></p>
//
//<pre>
//<strong>输入：</strong>x = 0
//<strong>输出：</strong>0
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>-2<sup>31</sup> &lt;= x &lt;= 2<sup>31</sup> - 1</code></li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>数学</li></div></div><br><div><li>👍 4095</li><li>👎 0</li></div>
class ReverseInteger {
    public static void main(String[] args) {
        Solution solution = new ReverseInteger().new Solution();
        System.out.println(11 / 10);
        System.out.println(11 % 10);
        System.out.println(-11 / 10);
        System.out.println(-11 % 10);
//        System.out.println(solution.reverse(-123));
//        System.out.println(solution.reverse(123));
//        System.out.println(solution.reverse(120));
        System.out.println(solution.reverse(-2147483648));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //题目要求不能使用long，那么需要通过使用求余的方式逐步获取每一位的数字，并且根据十分位和个位数字判断是否溢出
        public int reverse(int x) {
            int res = 0;
            while (x != 0) {
                int tmp = x % 10;
                if (res > 214748364 || (x == 214748364 && tmp > 7)) {
                    return 0;
                }
                if (res < -214748364 || (x == -214748364 && tmp < 8)) {
                    return 0;
                }
                res = res * 10 + tmp;
                x = x / 10;
            }
            return res;
        }

        //直接字符串反转，注意 int 表示范围是 -2147483648～2147483647，
        //所以如果x为-2147483648，那么 -x=2147483648，刚好溢出变成了-2147483648
        //所以需要先把 int 转成 long 才能进行 -x操作
        public int reverse1(int x) {
            long xLong = x;
            boolean positiveNumber = true;
            if (xLong < 0) {
                positiveNumber = false;
            }
            if (!positiveNumber) {
                xLong = -xLong;
            }
            StringBuilder sb = new StringBuilder(String.valueOf(xLong));
            StringBuilder reverseStr = sb.reverse();
            long i = Long.parseLong(reverseStr.toString());
            if (i > Integer.MAX_VALUE) {
                return 0;
            }
            if (positiveNumber) {
                return (int) i;
            } else {
                return (int) -i;

            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}