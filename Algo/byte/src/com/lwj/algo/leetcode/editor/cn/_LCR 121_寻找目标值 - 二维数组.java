package com.lwj.algo.leetcode.editor.cn;

//<p><code>m</code>*<code>n</code> 的二维数组 <code>plants</code> 记录了园林景观的植物排布情况，具有以下特性：</p>
//
//<ul> 
// <li>每行中，每棵植物的右侧相邻植物不矮于该植物；</li> 
// <li>每列中，每棵植物的下侧相邻植物不矮于该植物。</li> 
//</ul>
//
//<p>&nbsp;</p>
//
//<p>请判断 <code>plants</code> 中是否存在目标高度值 <code>target</code>。</p>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 1：</strong></p>
//
//<pre>
//<strong>输入：</strong>plants = [[2,3,6,8],[4,5,8,9],[5,9,10,12]], target = 8
//
//<strong>输出：</strong>true
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>示例 2：</strong></p>
//
//<pre>
//<strong>输入：</strong>plants = [[1,3,5],[2,5,7]], target = 4
//
//<strong>输出：</strong>false
//</pre>
//
//<p>&nbsp;</p>
//
//<p><strong>提示：</strong></p>
//
//<ul> 
// <li><code>0 &lt;= n &lt;= 1000</code></li> 
// <li><code>0 &lt;= m &lt;= 1000</code></li> 
//</ul>
//
//<p>注意：本题与主站 240 题相同：<a href="https://leetcode-cn.com/problems/search-a-2d-matrix-ii/" rel="noopener noreferrer" target="_blank">https://leetcode-cn.com/problems/search-a-2d-matrix-ii/</a></p>
//
//<p>&nbsp;</p>
//
//<div><div>Related Topics</div><div><li>数组</li><li>二分查找</li><li>分治</li><li>矩阵</li></div></div><br><div><li>👍 1037</li><li>👎 0</li></div>
class ErWeiShuZuZhongDeChaZhaoLcof {
    public static void main(String[] args) {
        Solution solution = new ErWeiShuZuZhongDeChaZhaoLcof().new Solution();
        System.out.println(solution);
    }

    //leetcode submit region begin(Prohibit modification and deletion)
    class Solution {
        public boolean findTargetIn2DPlants1(int[][] plants, int target) {
            int i = plants.length - 1;
            int j = plants[0].length - 1;
            while (j >= 0 && i < plants.length) {
                int cur = plants[i][j];
                if (cur == target) {
                    return true;
                } else if (cur > target) {
                    j--;
                } else {
                    i++;
                }
            }
            return false;
        }

        public boolean findTargetIn2DPlants(int[][] plants, int target) {
            for (int[] plant : plants) {
                for (int j = 0; j < plants[0].length; j++) {
                    if (plant[j] == target) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
//leetcode submit region end(Prohibit modification and deletion)

}