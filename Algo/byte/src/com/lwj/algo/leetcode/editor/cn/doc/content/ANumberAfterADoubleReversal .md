<p><strong>反转</strong> 一个整数意味着倒置它的所有位。</p>

<ul>
	<li>例如，反转 <code>2021</code> 得到 <code>1202</code> 。反转 <code>12300</code> 得到 <code>321</code> ，<strong>不保留前导零</strong> 。</li>
</ul>

<p>给你一个整数 <code>num</code> ，<strong>反转</strong> <code>num</code> 得到 <code>reversed1</code> ，<strong>接着反转</strong> <code>reversed1</code> 得到 <code>reversed2</code> 。如果 <code>reversed2</code> 等于 <code>num</code> ，返回 <code>true</code> ；否则，返回 <code>false</code> 。</p>

<p>&nbsp;</p>

<p><strong>示例 1：</strong></p>

<pre><strong>输入：</strong>num = 526
<strong>输出：</strong>true
<strong>解释：</strong>反转 num 得到 625 ，接着反转 625 得到 526 ，等于 num 。
</pre>

<p><strong>示例 2：</strong></p>

<pre><strong>输入：</strong>num = 1800
<strong>输出：</strong>false
<strong>解释：</strong>反转 num 得到 81 ，接着反转 81 得到 18 ，不等于 num 。 </pre>

<p><strong>示例 3：</strong></p>

<pre><strong>输入：</strong>num = 0
<strong>输出：</strong>true
<strong>解释：</strong>反转 num 得到 0 ，接着反转 0 得到 0 ，等于 num 。
</pre>

<p>&nbsp;</p>

<p><strong>提示：</strong></p>

<ul>
	<li><code>0 &lt;= num &lt;= 10<sup>6</sup></code></li>
</ul>
<div><div>Related Topics</div><div><li>数学</li></div></div><br><div><li>👍 10</li><li>👎 0</li></div>