package com.lwj.algo.leetcode.editor.cn;

import com.lwj.algo.leetcode.editor.cn.utils.ArraysUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

//<p>给你一个整数数组 <code>nums</code> 和一个整数 <code>k</code> ，请你返回其中出现频率前 <code>k</code> 高的元素。你可以按 <strong>任意顺序</strong> 返回答案。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1:</strong></p>
//
//<pre>
//<strong>输入: </strong>nums = [1,1,1,2,2,3], k = 2
//<strong>输出: </strong>[1,2]
//</pre>
//
//<p><strong>示例 2:</strong></p>
//
//<pre>
//<strong>输入: </strong>nums = [1], k = 1
//<strong>输出: </strong>[1]</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>1 &lt;= nums.length &lt;= 10<sup>5</sup></code></li> 
// <li><code>k</code> 的取值范围是 <code>[1, 数组中不相同的元素的个数]</code></li> 
// <li>题目数据保证答案唯一，换句话说，数组中前 <code>k</code> 个高频元素的集合是唯一的</li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p><strong>进阶：</strong>你所设计算法的时间复杂度 <strong>必须</strong> 优于 <code>O(n log n)</code> ，其中 <code>n</code><em>&nbsp;</em>是数组大小。</p>
//
//<div><div>Related Topics</div><div><li>数组</li><li>哈希表</li><li>分治</li><li>桶排序</li><li>计数</li><li>快速选择</li><li>排序</li><li>堆（优先队列）</li></div></div><br><div><li>👍 1975</li><li>👎 0</li></div>
class TopKFrequentElements {
    public static void main(String[] args) {
        Solution solution = new TopKFrequentElements().new Solution();
        ArraysUtil.print(solution.topKFrequent(new int[]{1, 1, 1, 2, 2, 3}, 2));
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public int[] topKFrequent(int[] nums, int k) {

            Map<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                map.merge(num, 1, Integer::sum);
            }
            PriorityQueue<Integer> heap = new PriorityQueue<>((a, b) -> {
                return map.get(a) - map.get(b);
            });
            map.forEach((key, val) -> {
                if (heap.size() == k) {
                    if (val > map.get(heap.peek())) {
                        heap.poll();
                        heap.add(key);
                    }
                } else {
                    heap.add(key);
                }
            });
            int[] res = new int[k];
            for (int i = k - 1; i >= 0; i--) {
                res[i] = heap.poll();
            }
            return res;
        }

        public int[] topKFrequent0(int[] nums, int k) {
            //存放元素统计数量
            Map<Integer, Integer> map = new HashMap<>();
            for (int num : nums) {
                map.merge(num, 1, Integer::sum);
            }
            int[] array = map.entrySet().stream().sorted((a, b) -> b.getValue().compareTo(a.getValue())).map(Map.Entry::getKey).mapToInt(Integer::intValue).toArray();
            int[] res = new int[k];
            System.arraycopy(array, 0, res, 0, k);
            return res;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}