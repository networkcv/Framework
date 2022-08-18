//<p>你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： <code>'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'</code> 。每个拨轮可以自由旋转：例如把 <code>'9'</code> 变为&nbsp;<code>'0'</code>，<code>'0'</code> 变为 <code>'9'</code> 。每次旋转都只能旋转一个拨轮的一位数字。</p>
//
//<p>锁的初始数字为 <code>'0000'</code> ，一个代表四个拨轮的数字的字符串。</p>
//
//<p>列表 <code>deadends</code> 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。</p>
//
//<p>字符串 <code>target</code> 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 <code>-1</code> 。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<pre>
//<strong>输入：</strong>deadends = ["0201","0101","0102","1212","2002"], target = "0202"
//<strong>输出：</strong>6
//<strong>解释：</strong>
//可能的移动序列为 "0000" -&gt; "1000" -&gt; "1100" -&gt; "1200" -&gt; "1201" -&gt; "1202" -&gt; "0202"。
//注意 "0000" -&gt; "0001" -&gt; "0002" -&gt; "0102" -&gt; "0202" 这样的序列是不能解锁的，
//因为当拨动到 "0102" 时这个锁就会被锁定。
//</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<pre>
//<strong>输入:</strong> deadends = ["8888"], target = "0009"
//<strong>输出：</strong>1
//<strong>解释：</strong>把最后一位反向旋转一次即可 "0000" -&gt; "0009"。
//</pre>
//
//<p><strong>示例 3:</strong></p>
//
//<pre>
//<strong>输入:</strong> deadends = ["8887","8889","8878","8898","8788","8988","7888","9888"], target = "8888"
//<strong>输出：</strong>-1
//<strong>解释：</strong>无法旋转到目标数字且不被锁定。
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;=&nbsp;deadends.length &lt;= 500</code></li> 
// <li><code><font face="monospace">deadends[i].length == 4</font></code></li> 
// <li><code><font face="monospace">target.length == 4</font></code></li> 
// <li><code>target</code> <strong>不在</strong> <code>deadends</code> 之中</li> 
// <li><code>target</code> 和 <code>deadends[i]</code> 仅由若干位数字组成</li> 
//</ul>
//
//<div><div>Related Topics</div><div><li>广度优先搜索</li><li>数组</li><li>哈希表</li><li>字符串</li></div></div><br><div><li>👍 528</li><li>👎 0</li></div>

package com.lwj.algo.leetcode.editor.cn;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

class OpenTheLock {
    public static void main(String[] args) {
        Solution solution = new OpenTheLock().new Solution();
        System.out.println(solution.openLock(new String[]{"0201", "0101", "0102", "1212", "2002"}, "0202"));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        //先来个简单的不考虑 deadends条件  bfs遍历所有可能密码到达target的最小步数
        public int openLock(String[] deadends, String target) {
            Queue<String> q = new LinkedList<>();
            Set<String> visited = new HashSet<>();
//            List<String> deads = Arrays.asList(deadends); //	执行耗时:461 ms,击败了5.02% 的Java用户 离谱
            Set<String> deads = new HashSet<>(); //执行耗时:73 ms,击败了57.91% 的Java用户
            for (String s : deadends)
                deads.add(s);
            q.add("0000");
            visited.add("0000");
            int nums = 0;
            while (!q.isEmpty()) {
                int size = q.size();
                for (int i = 0; i < size; i++) {
                    String cur = q.poll();
                    if (deads.contains(cur)) {
                        continue;
                    }
                    if (cur.equals(target)) {
                        return nums;
                    }
                    for (int j = 0; j < 4; j++) {
                        String up = plusOne(cur, j);
                        if (!visited.contains(up)) {
                            q.offer(up);
                            visited.add(up);
                        }
                        String down = minusOne(cur, j);
                        if (!visited.contains(down)) {
                            q.offer(down);
                            visited.add(down);
                        }
                    }
                }
                nums++;
            }
            return -1;
        }

        private String plusOne(String s, int i) {
            char[] chars = s.toCharArray();
            if (chars[i] == '9') {
                chars[i] = '0';
            } else {
                chars[i] += 1;
            }
            return new String(chars);
        }

        private String minusOne(String s, int i) {
            char[] chars = s.toCharArray();
            if (chars[i] == '0') {
                chars[i] = '9';
            } else {
                chars[i] -= 1;
            }
            return new String(chars);
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}