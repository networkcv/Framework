<p>给定一个整数数组 <code>nums</code>&nbsp;和一个整数目标值 <code>target</code>，请你在该数组中找出 <strong>和为目标值 </strong><em><code>target</code></em>&nbsp; 的那&nbsp;<strong>两个</strong>&nbsp;整数，并返回它们的数组下标。</p>

<p>你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。</p>

<p>你可以按任意顺序返回答案。</p>

<p>&nbsp;</p>

<p><strong class="example">示例 1：</strong></p>

<pre>
<strong>输入：</strong>nums = [2,7,11,15], target = 9
<strong>输出：</strong>[0,1]
<strong>解释：</strong>因为 nums[0] + nums[1] == 9 ，返回 [0, 1] 。
</pre>

<p><strong class="example">示例 2：</strong></p>

<pre>
<strong>输入：</strong>nums = [3,2,4], target = 6
<strong>输出：</strong>[1,2]
</pre>

<p><strong class="example">示例 3：</strong></p>

<pre>
<strong>输入：</strong>nums = [3,3], target = 6
<strong>输出：</strong>[0,1]
</pre>

<p>&nbsp;</p>

<p><strong>提示：</strong></p>

<ul> 
 <li><code>2 &lt;= nums.length &lt;= 10<sup>4</sup></code></li> 
 <li><code>-10<sup>9</sup> &lt;= nums[i] &lt;= 10<sup>9</sup></code></li> 
 <li><code>-10<sup>9</sup> &lt;= target &lt;= 10<sup>9</sup></code></li> 
 <li><strong>只会存在一个有效答案</strong></li> 
</ul>

<p>&nbsp;</p>

<p><strong>进阶：</strong>你可以想出一个时间复杂度小于 <code>O(n<sup>2</sup>)</code> 的算法吗？</p>

<details><summary><strong>Related Topics</strong></summary>数组 | 哈希表</details><br>

<div>👍 18625, 👎 0<span style='float: right;'><span style='color: gray;'><a href='https://github.com/labuladong/fucking-algorithm/discussions/939' target='_blank' style='color: lightgray;text-decoration: underline;'>bug 反馈</a> | <a href='https://labuladong.online/algo/fname.html?fname=jb插件简介' target='_blank' style='color: lightgray;text-decoration: underline;'>使用指南</a> | <a href='https://labuladong.online/algo/images/others/%E5%85%A8%E5%AE%B6%E6%A1%B6.jpg' target='_blank' style='color: lightgray;text-decoration: underline;'>更多配套插件</a></span></span></div>

<div id="labuladong"><hr>

**通知：[新版网站会员](https://labuladong.online/algo/intro/site-vip/) 限时优惠；算法可视化编辑器上线，[点击体验](https://labuladong.online/algo/intro/visualize/)！**



<p><strong><a href="https://labuladong.online/algo/slug.html?slug=two-sum" target="_blank">⭐️labuladong 题解</a></strong></p>
<details><summary><strong>labuladong 思路</strong></summary>

## 基本思路

大家都喜欢幽默的人，如果你想调侃自己经常拖延，可以这样调侃下自己（手动狗头）：

背单词背了半年还是 abandon, abandon，刷题刷了半年还是 two sum, two sum...

言归正传，这道题不难，但由于它是 LeetCode 第一题，所以名气比较大，解决这道题也可以有多种思路，我这里说两种最常见的思路。

第一种思路就是靠排序，把 `nums` 排序之后就可以用 [数组双指针技巧汇总](https://labuladong.online/algo/fname.html?fname=双指针技巧) 中讲到的左右指针来求出和为 `target` 的两个数。

不过因为题目要求我们返回元素的索引，而排序会破坏元素的原始索引，所以要记录值和原始索引的映射。

进一步，如果题目拓展延伸一下，让你求三数之和、四数之和，你依然可以用双指针技巧，我在 [一个函数秒杀 nSum 问题](https://labuladong.online/algo/fname.html?fname=nSum) 中写一个函数来解决所有 N 数之和问题。

第二种思路是用哈希表辅助判断。对于一个元素 `nums[i]`，你想知道有没有另一个元素 `nums[j]` 的值为 `target - nums[i]`，这很简单，我们用一个哈希表记录每个元素的值到索引的映射，这样就能快速判断数组中是否有一个值为 `target - nums[i]` 的元素了。

简单说，数组其实可以理解为一个「索引 -> 值」的哈希表映射，而我们又建立一个「值 -> 索引」的映射即可完成此题。

**详细题解：[一个方法团灭 nSum 问题](https://labuladong.online/algo/fname.html?fname=nSum)**

**标签：[双指针](https://labuladong.online/algo/)，哈希表，[数组](https://labuladong.online/algo/)**

## 解法代码

提示：🟢 标记的是我写的解法代码，🤖 标记的是 chatGPT 翻译的多语言解法代码。如有错误，可以 [点这里](https://github.com/labuladong/fucking-algorithm/issues/1113) 反馈和修正。

<div class="tab-panel"><div class="tab-nav">
<button data-tab-item="cpp" class="tab-nav-button btn " data-tab-group="default" onclick="switchTab(this)">cpp🤖</button>

<button data-tab-item="python" class="tab-nav-button btn " data-tab-group="default" onclick="switchTab(this)">python🤖</button>

<button data-tab-item="java" class="tab-nav-button btn active" data-tab-group="default" onclick="switchTab(this)">java🟢</button>

<button data-tab-item="go" class="tab-nav-button btn " data-tab-group="default" onclick="switchTab(this)">go🤖</button>

<button data-tab-item="javascript" class="tab-nav-button btn " data-tab-group="default" onclick="switchTab(this)">javascript🤖</button>
</div><div class="tab-content">
<div data-tab-item="cpp" class="tab-item " data-tab-group="default"><div class="highlight">

```cpp
// 注意：cpp 代码由 chatGPT🤖 根据我的 java 代码翻译，旨在帮助不同背景的读者理解算法逻辑。
// 本代码已经通过力扣的测试用例，应该可直接成功提交。

class Solution {
public:
    vector<int> twoSum(vector<int>& nums, int target) {
        // 维护 val -> index 的映射
        unordered_map<int, int> valToIndex;
        for (int i = 0; i < nums.size(); i++) {
            // 查表，看看是否有能和 nums[i] 凑出 target 的元素
            int need = target - nums[i];
            if (valToIndex.count(need)) {
                return vector<int>{valToIndex[need], i};
            }
            // 存入 val -> index 的映射
            valToIndex[nums[i]] = i;
        }
        return vector<int>{};
    }
};
```

</div></div>

<div data-tab-item="python" class="tab-item " data-tab-group="default"><div class="highlight">

```python
# 注意：python 代码由 chatGPT🤖 根据我的 java 代码翻译，旨在帮助不同背景的读者理解算法逻辑。
# 本代码已经通过力扣的测试用例，应该可直接成功提交。

class Solution:
    def twoSum(self, nums: List[int], target: int) -> List[int]:
        # 维护 val -> index 的映射
        valToIndex = {}
        for i in range(len(nums)):
            # 查表，看看是否有能和 nums[i] 凑出 target 的元素
            need = target - nums[i]
            if need in valToIndex:
                return [valToIndex[need], i]
            # 存入 val -> index 的映射
            valToIndex[nums[i]] = i
        return []
```

</div></div>

<div data-tab-item="java" class="tab-item active" data-tab-group="default"><div class="highlight">

```java
class Solution {
    public int[] twoSum(int[] nums, int target) {
        // 维护 val -> index 的映射
        HashMap<Integer, Integer> valToIndex = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            // 查表，看看是否有能和 nums[i] 凑出 target 的元素
            int need = target - nums[i];
            if (valToIndex.containsKey(need)) {
                return new int[]{valToIndex.get(need), i};
            }
            // 存入 val -> index 的映射
            valToIndex.put(nums[i], i);
        }
        return null;
    }
}
```

</div></div>

<div data-tab-item="go" class="tab-item " data-tab-group="default"><div class="highlight">

```go
// 注意：go 代码由 chatGPT🤖 根据我的 java 代码翻译，旨在帮助不同背景的读者理解算法逻辑。
// 本代码已经通过力扣的测试用例，应该可直接成功提交。

func twoSum(nums []int, target int) []int {
    // 维护 val -> index 的映射
    valToIndex := make(map[int]int)

    for i, num := range nums {
        // 查表，看看是否有能和 nums[i] 凑出 target 的元素
        need := target - num
        if valToIndex[need] != 0 {
            return []int{valToIndex[need] - 1, i}
        }
        // 存入 val -> index 的映射
        valToIndex[num] = i + 1
    }

    return nil
}
```

</div></div>

<div data-tab-item="javascript" class="tab-item " data-tab-group="default"><div class="highlight">

```javascript
// 注意：javascript 代码由 chatGPT🤖 根据我的 java 代码翻译，旨在帮助不同背景的读者理解算法逻辑。
// 本代码已经通过力扣的测试用例，应该可直接成功提交。

var twoSum = function(nums, target) {
    // 维护 val -> index 的映射
    var valToIndex = new Map();
    for (var i = 0; i < nums.length; i++) {
        // 查表，看看是否有能和 nums[i] 凑出 target 的元素
        var need = target - nums[i];
        if (valToIndex.has(need)) {
            return [valToIndex.get(need), i];
        }
        // 存入 val -> index 的映射
        valToIndex.set(nums[i], i);
    }
    return null;
};
```

</div></div>
</div></div>

<hr /><details open hint-container details><summary style="font-size: medium"><strong>🌟🌟 算法可视化 🌟🌟</strong></summary><div id="data_two-sum" data="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"></div><div class="resizable aspect-ratio-container" style="height: 100%;">
<div id="iframe_two-sum"></div></div>
</details><hr /><br />

**类似题目**：
  - [15. 三数之和 🟠](/problems/3sum)
  - [167. 两数之和 II - 输入有序数组 🟠](/problems/two-sum-ii-input-array-is-sorted)
  - [18. 四数之和 🟠](/problems/4sum)
  - [剑指 Offer 57. 和为s的两个数字 🟢](/problems/he-wei-sde-liang-ge-shu-zi-lcof)
  - [剑指 Offer II 007. 数组中和为 0 的三个数 🟠](/problems/1fGaJU)

</details>
</div>

