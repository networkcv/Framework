# a

"""
多行注释
"""
int_a = 10012321321321318787878
int_aa = 1.1
print(type(int_a))
print(type(int_aa))

str_b = 'str'
str_c = "str"
print(type(str_b))
print(type(str_c))

bool_d = True
print(type(bool_d))

"""
- `int()`：将一个数值或字符串转换成整数，可以指定进制。
- `float()`：将一个字符串转换成浮点数。
- `str()`：将指定的对象转换成字符串形式，可以指定编码。
- `chr()`：将整数转换成该编码对应的字符串（一个字符）。
- `ord()`：将字符串（一个字符）转换成对应的编码（整数），
这个编码是unicode中对应的数值，unicode相当于完整的编码集，每个编码都由两个字节构成，而UTF-8是一种可变长的编码方式，它规定ASCII码用一个字节表示，汉字用三个字节表示。
"""
print(str(1))
print(ord('a'))
print(chr(97))

"""
运算符
`[]` `[:]`下标，切片
`**`指数
`*` `/` `%` `//`乘，除，模，整除
"""
print(10 / 3)
print(10 % 3)
print(10 // 3)

"""
打印的占位符
.2 表示截切2位
类似于java的log日志打印
"""
print("%.2s is girl" % "abc")
str_name = "aaa"
print("%.2s is girl" % str_name)
print("%.2s is %s" % ("abc", str_name))

f = 1
c = 2
print(f'{f:.1f}华氏度 = {c:.1f}摄氏度')

"""
input 输入函数
"""
radius = float(input('请输入圆的半径: '))
perimeter = 2 * 3.1416 * radius
area = 3.1416 * radius * radius
print('周长: %.2f' % perimeter)
print('面积: %.2f' % area)

"""
字符串格式
我们可以使用辅助符号"m.n"来控制数据的宽度和精度,先精度再宽度，宽度不足补空格
m，控制宽度，要求是数字（很少使用），设置的宽度小于数字自身，不生效
.n，控制小数点精度，要求是数字，会进行小数的四舍五入
示例：
%5d：表示将整数的宽度控制在5位，如数字11，被设置为5d，就会变成：[空格][空格空格]11，用三个空格补足
宽度。
%5.2f：表示将宽度控制为5，将小数点精度设置为2
小数点和小数部分也算入宽度计算。如，对11.345设置了％7.2f后，结果是：[空格]空格111.35。2个空格补足宽
心
度，小数部分限制2位精度后，四舍五入为.35
"""

"""
f"字符串内容{变量名称}"
"""
name = "LWJ"
print(f' name is {name}')
