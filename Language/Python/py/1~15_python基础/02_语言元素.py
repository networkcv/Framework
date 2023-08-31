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
- `ord()`：将字符串（一个字符）转换成对应的编码（整数）。
"""
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
