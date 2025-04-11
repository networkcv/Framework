package com.lwj.algo.leetcode.editor.cn;

import java.util.ArrayList;
import java.util.List;

//<p><strong>有效 IP 地址</strong> 正好由四个整数（每个整数位于 <code>0</code> 到 <code>255</code> 之间组成，且不能含有前导 <code>0</code>），整数之间用 <code>'.'</code> 分隔。</p>
//
//<ul> 
// <li>例如：<code>"0.1.2.201"</code> 和<code> "192.168.1.1"</code> 是 <strong>有效</strong> IP 地址，但是 <code>"0.011.255.245"</code>、<code>"192.168.1.312"</code> 和 <code>"192.168@1.1"</code> 是 <strong>无效</strong> IP 地址。</li> 
//</ul>
//
//<p>给定一个只包含数字的字符串 <code>s</code> ，用以表示一个 IP 地址，返回所有可能的<strong>有效 IP 地址</strong>，这些地址可以通过在 <code>s</code> 中插入&nbsp;<code>'.'</code> 来形成。你 <strong>不能</strong>&nbsp;重新排序或删除 <code>s</code> 中的任何数字。你可以按 <strong>任何</strong> 顺序返回答案。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "25525511135"
//<strong>输出：</strong>["255.255.11.135","255.255.111.35"]
//</pre>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "0000"
//<strong>输出：</strong>["0.0.0.0"]
//</pre>
//
//<p><strong>示例 3：</strong></p>
//
//<pre>
//<strong>输入：</strong>s = "101023"
//<strong>输出：</strong>["1.0.10.23","1.0.102.3","10.1.0.23","10.10.2.3","101.0.2.3"]
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= s.length &lt;= 20</code></li> 
// <li><code>s</code> 仅由数字组成</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>字符串</li><li>回溯</li></div></div><br><div><li>👍 1494</li><li>👎 0</li></div>
class RestoreIpAddresses {
    public static void main(String[] args) {
        Solution solution = new RestoreIpAddresses().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //25525511135
        List<String> res = new ArrayList<>();
        List<String> path = new ArrayList<>();
        String s;

        public List<String> restoreIpAddresses(String s) {
            this.s = s;
            dfs(0, 1, s);
            return res;
        }

        public void dfs(int i, int j, String s) {
            if (path.size() == 4) {
                String subRes = String.join(",", path);
                res.add(subRes);
                return;
            }
            String sub = s.substring(i, j);
            if (!sub.isEmpty()) {
                int num = Integer.parseInt(sub);
                if (num > 0 && num < 256) {
                    path.add(sub);
                }
            }
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}