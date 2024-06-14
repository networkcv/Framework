<p>给定一个字符串 <code>s</code> ，请你找出其中不含有重复字符的&nbsp;<strong>最长 <span data-keyword="substring-nonempty">子串</span></strong><strong>&nbsp;</strong>的长度。</p>

<p>&nbsp;</p>

<p><strong>示例&nbsp;1:</strong></p>

<pre>
<strong>输入: </strong>s = "abcabcbb"
<strong>输出: </strong>3 
<strong>解释:</strong> 因为无重复字符的最长子串是 <span><code>"abc"</code></span>，所以其长度为 3。
</pre>

<p><strong>示例 2:</strong></p>

<pre>
<strong>输入: </strong>s = "bbbbb"
<strong>输出: </strong>1
<strong>解释: </strong>因为无重复字符的最长子串是 <span><code>"b"</code></span>，所以其长度为 1。
</pre>

<p><strong>示例 3:</strong></p>

<pre>
<strong>输入: </strong>s = "pwwkew"
<strong>输出: </strong>3
<strong>解释: </strong>因为无重复字符的最长子串是&nbsp;<span><code>"wke"</code></span>，所以其长度为 3。
&nbsp;    请注意，你的答案必须是 <strong>子串 </strong>的长度，<span><code>"pwke"</code></span>&nbsp;是一个<em>子序列，</em>不是子串。
</pre>

<p>&nbsp;</p>

<p><strong>提示：</strong></p>

<ul> 
 <li><code>0 &lt;= s.length &lt;= 5 * 10<sup>4</sup></code></li> 
 <li><code>s</code>&nbsp;由英文字母、数字、符号和空格组成</li> 
</ul>

<details><summary><strong>Related Topics</strong></summary>哈希表 | 字符串 | 滑动窗口</details><br>

<div>👍 10180, 👎 0<span style='float: right;'><span style='color: gray;'><a href='https://github.com/labuladong/fucking-algorithm/discussions/939' target='_blank' style='color: lightgray;text-decoration: underline;'>bug 反馈</a> | <a href='https://labuladong.online/algo/fname.html?fname=jb插件简介' target='_blank' style='color: lightgray;text-decoration: underline;'>使用指南</a> | <a href='https://labuladong.online/algo/images/others/%E5%85%A8%E5%AE%B6%E6%A1%B6.jpg' target='_blank' style='color: lightgray;text-decoration: underline;'>更多配套插件</a></span></span></div>

<div id="labuladong"><hr>

**通知：[新版网站会员](https://labuladong.online/algo/intro/site-vip/) 限时优惠；算法可视化编辑器上线，[点击体验](https://labuladong.online/algo/intro/visualize/)！**



<p><strong><a href="https://labuladong.online/algo/slug.html?slug=longest-substring-without-repeating-characters" target="_blank">⭐️labuladong 题解</a></strong></p>
<details><summary><strong>labuladong 思路</strong></summary>

## 基本思路

> 本文有视频版：[滑动窗口算法核心模板框架](https://www.bilibili.com/video/BV1AV4y1n7Zt)

PS：这道题在[《算法小抄》](https://item.jd.com/12759911.html) 的第 85 页。

这题比其他滑动窗口的题目简单，连 `need` 和 `valid` 都不需要，而且更新窗口内数据也只需要简单的更新计数器 `window` 即可。

当 `window[c]` 值大于 1 时，说明窗口中存在重复字符，不符合条件，就该移动 `left` 缩小窗口了。

另外，要在收缩窗口完成后更新 `res`，因为窗口收缩的 while 条件是存在重复元素，换句话说收缩完成后一定保证窗口中没有重复。

**详细题解：[我写了首诗，把滑动窗口算法变成了默写题](https://labuladong.online/algo/fname.html?fname=滑动窗口技巧进阶)**

**标签：[滑动窗口](https://labuladong.online/algo/)**

## 解法代码

提示：🟢 标记的是我写的解法代码，🤖 标记的是 chatGPT 翻译的多语言解法代码。如有错误，可以 [点这里](https://github.com/labuladong/fucking-algorithm/issues/1113) 反馈和修正。

<div class="tab-panel"><div class="tab-nav">
<button data-tab-item="cpp" class="tab-nav-button btn active" data-tab-group="default" onclick="switchTab(this)">cpp🟢</button>

<button data-tab-item="python" class="tab-nav-button btn " data-tab-group="default" onclick="switchTab(this)">python🤖</button>

<button data-tab-item="java" class="tab-nav-button btn " data-tab-group="default" onclick="switchTab(this)">java🤖</button>

<button data-tab-item="go" class="tab-nav-button btn " data-tab-group="default" onclick="switchTab(this)">go🤖</button>

<button data-tab-item="javascript" class="tab-nav-button btn " data-tab-group="default" onclick="switchTab(this)">javascript🤖</button>
</div><div class="tab-content">
<div data-tab-item="cpp" class="tab-item active" data-tab-group="default"><div class="highlight">

```cpp
class Solution {
    public:
    int lengthOfLongestSubstring(string s) {
        unordered_map<char, int> window;

        int left = 0, right = 0;
        int res = 0; // 记录结果
        while (right < s.size()) {
            char c = s[right];
            right++;
            // 进行窗口内数据的一系列更新
            window[c]++;
            // 判断左侧窗口是否要收缩
            while (window[c] > 1) {
                char d = s[left];
                left++;
                // 进行窗口内数据的一系列更新
                window[d]--;
            }
            // 在这里更新答案
            res = max(res, right - left);
        }
        return res;
    }
};
```

</div></div>

<div data-tab-item="python" class="tab-item " data-tab-group="default"><div class="highlight">

```python
# 注意：python 代码由 chatGPT🤖 根据我的 cpp 代码翻译，旨在帮助不同背景的读者理解算法逻辑。
# 本代码已经通过力扣的测试用例，应该可直接成功提交。

class Solution:
    def lengthOfLongestSubstring(self, s: str) -> int:
        window = {}

        left = right = 0
        res = 0 # 记录结果
        while right < len(s):
            c = s[right]
            right += 1
            # 进行窗口内数据的一系列更新
            window[c] = window.get(c, 0) + 1
            # 判断左侧窗口是否要收缩
            while window[c] > 1:
                d = s[left]
                left += 1
                # 进行窗口内数据的一系列更新
                window[d] -= 1
            # 在这里更新答案
            res = max(res, right - left)
        return res
```

</div></div>

<div data-tab-item="java" class="tab-item " data-tab-group="default"><div class="highlight">

```java
// 注意：java 代码由 chatGPT🤖 根据我的 cpp 代码翻译，旨在帮助不同背景的读者理解算法逻辑。
// 本代码已经通过力扣的测试用例，应该可直接成功提交。

class Solution {
    public int lengthOfLongestSubstring(String s) {
        Map<Character, Integer> window = new HashMap<>();

        int left = 0, right = 0;
        int res = 0; // 记录结果
        while (right < s.length()) {
            char c = s.charAt(right);
            right++;
            // 进行窗口内数据的一系列更新
            window.put(c, window.getOrDefault(c, 0) + 1);
            // 判断左侧窗口是否要收缩
            while (window.get(c) > 1) {
                char d = s.charAt(left);
                left++;
                // 进行窗口内数据的一系列更新
                window.put(d, window.get(d) - 1);
            }
            // 在这里更新答案
            res = Math.max(res, right - left);
        }
        return res;
    }
}
```

</div></div>

<div data-tab-item="go" class="tab-item " data-tab-group="default"><div class="highlight">

```go
// 注意：go 代码由 chatGPT🤖 根据我的 cpp 代码翻译，旨在帮助不同背景的读者理解算法逻辑。
// 本代码已经通过力扣的测试用例，应该可直接成功提交。

func lengthOfLongestSubstring(s string) int {
    window := make(map[byte]int)

    left, right := 0, 0
    res := 0 // 记录结果
    for right < len(s) {
        c := s[right]
        right++
        // 进行窗口内数据的一系列更新
        window[c]++
        // 判断左侧窗口是否要收缩
        for window[c] > 1 {
            d := s[left]
            left++
            // 进行窗口内数据的一系列更新
            window[d]--
        }
        // 在这里更新答案
        res = max(res, right - left)
    }
    return res
}

func max(a, b int) int {
    if a > b {
        return a
    }
    return b
}
```

</div></div>

<div data-tab-item="javascript" class="tab-item " data-tab-group="default"><div class="highlight">

```javascript
// 注意：javascript 代码由 chatGPT🤖 根据我的 cpp 代码翻译，旨在帮助不同背景的读者理解算法逻辑。
// 本代码已经通过力扣的测试用例，应该可直接成功提交。

var lengthOfLongestSubstring = function(s) {
    let window = new Map();
    let left = 0, right = 0, res = 0;
    while (right < s.length) {
        let c = s[right];
        right++;
        // 进行窗口内数据的一系列更新
        window.set(c, (window.get(c) || 0) + 1);
        // 判断左侧窗口是否要收缩
        while (window.get(c) > 1) {
            let d = s[left];
            left++;
            // 进行窗口内数据的一系列更新
            window.set(d, window.get(d) - 1);
        }
        // 在这里更新答案
        res = Math.max(res, right - left);
    }
    return res;
};
```

</div></div>
</div></div>

<hr /><details open hint-container details><summary style="font-size: medium"><strong>🌟🌟 算法可视化 🌟🌟</strong></summary><div id="data_longest-substring-without-repeating-characters" data="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"></div><div class="resizable aspect-ratio-container" style="height: 100%;">
<div id="iframe_longest-substring-without-repeating-characters"></div></div>
</details><hr /><br />

**类似题目**：
  - [438. 找到字符串中所有字母异位词 🟠](/problems/find-all-anagrams-in-a-string)
  - [567. 字符串的排列 🟠](/problems/permutation-in-string)
  - [76. 最小覆盖子串 🔴](/problems/minimum-window-substring)
  - [剑指 Offer 48. 最长不含重复字符的子字符串 🟠](/problems/zui-chang-bu-han-zhong-fu-zi-fu-de-zi-zi-fu-chuan-lcof)
  - [剑指 Offer II 014. 字符串中的变位词 🟠](/problems/MPnaiL)
  - [剑指 Offer II 015. 字符串中的所有变位词 🟠](/problems/VabMRr)
  - [剑指 Offer II 016. 不含重复字符的最长子字符串 🟠](/problems/wtcaE1)
  - [剑指 Offer II 017. 含有所有字符的最短字符串 🔴](/problems/M1oyTv)

</details>
</div>

