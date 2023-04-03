正则表达式（regex）用来高效匹配，Java标准库 `java.util.regex` 包内置了正则表达式引擎。

而我们只需要一个描述一个字符串规则就可以判断目标字符串是否匹配规则。

```java
//检测是否是11位手机号
String phoneRegex = "152xxxxxxxx";
boolean res = phone.matches("\\d{11}");
```

上例中，用 ` \d` 表示一个数字，`{11}` 表示有前边类型的个数。（在Java的字符串中，反斜线`\` 表示转义字符，比如 `\r` 表示回车 `\n` 表示换行，而 `\\` 则表示一个 反斜线 `\`）。

```java
String s1 = "152\\d{8}" //表示152开头的总共11位数字
```

## 简单匹配规则

正则表达式的匹配规则是从左到右按规则匹配。

对于正则表达式`abc`来说，它只能精确地匹配字符串`"abc"`，不能匹配`"ab"`，`"Abc"`，`"abcd"`等其他任何字符串。

如果正则表达式有特殊字符，那就需要用`\`转义。例如，正则表达式`a\&c`，在Java中的用字符串表示的话是 `"a\\&c"`，其中`\&`是用来匹配特殊字符`&`的，它能精确匹配字符串`"a&c"`，但不能匹配`"ac"`、`"a-c"`、`"a&&c"`等。

如果想匹配非ASCII字符，例如中文，那就用`\u####`的十六进制表示，例如：`a\u548cc`匹配字符串`"a和c"`，中文字符`和`的Unicode编码是`548c`。很多网站对输入的关键字过滤就是通过这种方式。

**匹配任意字符**

大多数情况下我们需要模糊匹配 ，可以使用 `.`  来匹配一个任意字符。

例如，正则表达式 `a.c` 可以匹配 "abc"、”a&c”、“acc”，但是不能匹配“ac”、“abbc”。

**匹配数字**

如果我们只想匹配一个0～9的数字可以使用 `\d`。

`00\d` 可以匹配”008“、”009“，但是不能匹配 ”0010“、”00a“。

**匹配常用字符**

`\w` 可以匹配一个字母、数字或下划线，w代表word。不能匹配到 ”#“和 ” “(空格)。

### 小结

单个字符的匹配规则如下：

| 正则表达式 | 规则                     | 可以匹配                       |
| :--------- | :----------------------- | :----------------------------- |
| `A`        | 指定字符                 | `A`                            |
| `\u548c`   | 指定Unicode字符          | `和`                           |
| `.`        | 任意字符                 | `a`，`b`，`&`，`0`             |
| `\d`       | 数字0~9                  | `0`~`9`                        |
| `\w`       | 大小写字母，数字和下划线 | `a`~`z`，`A`~`Z`，`0`~`9`，`_` |
| `\s`       | 空格、Tab键              | 空格，Tab                      |
| `\D`       | 非数字                   | `a`，`A`，`&`，`_`，……         |
| `\W`       | 非\w                     | `&`，`@`，`中`，……             |
| `\S`       | 非\s                     | `a`，`A`，`&`，`_`，……         |

多个字符的匹配规则如下：

| 正则表达式 | 规则             | 可以匹配                 |
| :--------- | :--------------- | :----------------------- |
| `A*`       | 任意个数字符     | 空，`A`，`AA`，`AAA`，…… |
| `A+`       | 至少1个字符      | `A`，`AA`，`AAA`，……     |
| `A?`       | 0个或1个字符     | 空，`A`                  |
| `A{3}`     | 指定个数字符     | `AAA`                    |
| `A{2,3}`   | 指定范围个数字符 | `AA`，`AAA`              |
| `A{2,}`    | 至少n个字符      | `AA`，`AAA`，`AAAA`，……  |
| `A{0,3}`   | 最多n个字符      | 空，`A`，`AA`，`AAA`     |

## 复杂匹配规则

用正则表达式进行多行匹配时，我们用`^`表示开头，`$`表示结尾。例如，`^A\d{3}$`，可以匹配`"A001"`、`"A380"`，但是不能匹配 `"A0001"`，原因是A后边第一个0没有对应上，我们稍微修改一下就可以了，如  `^A.\\d{3}$`或者 `^A\\d{4}$` 都可以。

复杂匹配规则主要有：

| 正则表达式 | 规则                 | 可以匹配                             |
| :--------- | :------------------- | :----------------------------------- |
| ^          | 开头                 | 字符串开头                           |
| $          | 结尾                 | 字符串结束                           |
| [ABC]      | […]内任意字符        | A，B，C                              |
| [A-F0-9xy] | 指定范围的字符       | `A`，……，`F`，`0`，……，`9`，`x`，`y` |
| [^A-F]     | 指定范围外的任意字符 | 非`A`~`F`                            |
| AB\|CD\|EF | AB或CD或EF           | `AB`，`CD`，`EF`                     |

## 分组匹配

通过`()`将正则表达式进行分组，分组并不会影响匹配结果，只是方便在匹配成功后提取相关字符串，不然还要用String的`indexOf()`和`substring()`等方法。

```java
Pattern pattern = Pattern.compile("(.*)-([a-z]{4})");
Matcher matcher = pattern.matcher("hello-java");
if (matcher.matches()) {	//判断匹配的结果
    System.out.println(matcher.group(0));	//获取整个字符串
    System.out.println(matcher.group(1));	//获取第一个子串分组  .*
    System.out.println(matcher.group(2));	//获取第二个子串分组 [a-z]{4}
}
/* Output
hello-java
hello
java
*/
```

## 非贪婪匹配

正则表达式默认使用的是贪婪匹配，同时满足前后子串的情况下，让前边子串尽可能的多匹配。而通过在表达式后边加 `?` 可以设置为非贪婪匹配。

举个例子:

**贪婪匹配：** `(\d*)(\d*)`

```java
        Pattern pattern = Pattern.compile("(\\d*)(\\d*)");
        Matcher matcher = pattern.matcher("12345");
        if (matcher.matches()) {
            System.out.println("group1=" + matcher.group(1));
            System.out.println("group2=" + matcher.group(2));
        }
		/* Output
			group1=12345
			group2=
		*/
```

**非贪婪匹配：** `(\d*?)(\d*)`

```java
        Pattern pattern = Pattern.compile("(\\d*?)(\\d*)");
        Matcher matcher = pattern.matcher("12345");
        if (matcher.matches()) {
            System.out.println("group1=" + matcher.group(1));
            System.out.println("group2=" + matcher.group(2));
        }
		/* Output
			group1=
			group2=12345
		*/
```

这里还有一个要注意的点，在简单匹配规则中我们提过 `?` 还表示匹配0个或1个，如 `\d?` 表示0个或1个数字。

那这里如何区分表达两种不同含义的 `?` ，表示非贪婪匹配的问号要放在后面。

例如，`(\d??)(9*)`，`\d?`表示匹配0个或1个数字，后面第二个`?`表示非贪婪匹配，因此，给定字符串`"9999"`，匹配到的两个子串分别是`""`和`"9999"`，因为对于`\d?`来说，可以匹配1个`9`，也可以匹配0个`9`，但是因为后面的`?`表示非贪婪匹配，它就会尽可能少的匹配，结果是匹配了0个`9`。

## 搜索、分割和替换

使用正则表达式还可以搜索字符串。

```java
    String s = "the quick brown fox jumps over the lazy dog.";
    Pattern p = Pattern.compile("\\wo\\w");
    Matcher m = p.matcher(s);
    while (m.find()) {
        String sub = s.substring(m.start(), m.end());
        System.out.println(sub);
    }
	/* Output
        row
        fox
        dog
	*/
```

Java的String类提供了 分割和替换 的支持，对应的方法是 `split()` 和 `replaceAll()`。

**练习：模板引擎**

模板引擎，也就是一个字符串模板，可以应用在很多方面，如动态的拼装sq。

这里除了之前用的`Matcher.find()`、`Matcher.start()`、 `Matcher.end()`还需要用到Matcher的两个方法：

```java
// 对每次匹配到的字符串替换追加到sb的末尾
public Matcher appendReplacement(StringBuffer sb, String replacement) 
// 将最后匹配到的字符串追加到sb的末尾
public StringBuffer appendTail(StringBuffer sb) 
```

代码如下：

```java
        HashMap<String,String> map =new HashMap<>();
        map.put("field","name");
        map.put("table","user");
        String sql="select #{field} from #{table} order by id ";
        Pattern templateEngine = Pattern.compile("\\#\\{(\\w+)}");
        Matcher matcher = templateEngine.matcher(sql);
        StringBuffer res = new StringBuffer();
        while(matcher.find()){
            //用于截取获得 "#{field}" 中的 field
            String key=sql.substring(matcher.start() + 2, matcher.end() - 1);   //field
            //这里第一次find到的内容是 "select #{field}"，appendReplacement方法将 #{field} 替换为map.get(key)的内容，然后将结果 "select name" 添加到res中
            matcher.appendReplacement(res,map.get(key));
        }
		//第三次找到的内容是 “order by id”，但是与pattern 不符合，所以返回false，退出了while循环，因此这句“order by id” 并没有添加到res中，所以需要我们手动添加。
        matcher.appendTail(res);	
        System.out.println(res.toString());
```

**练习：IP地址解析**

IP地址的长度为32位(共有2^32个IP地址)，分为4段，每段8位。用十进制数字表示，每段数字范围为0～255，段与段之间用句点隔开。// 0.0.0.0 ～ 255.255.255.255

根据规则：每段相同，范围都在 0 ~ 255，每段对应的正则表达式为 `(2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2}`

这个表达式由两部分组成，`(2(5[0-5]|[0-4]\d))` 匹配 200～255，`[0-1]?\d{1,2}` 匹配 0 ~ 199。

通过 `|` 将两个子串连起来，就表示0～255，后边还有 `.255.255.255` 这里可以用`(\.((2(5[0-5]|[0-4]\d))|[0-1]?\d{1,2})){3}`来表示。