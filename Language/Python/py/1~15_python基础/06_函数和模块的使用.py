# 每个python文件都是一个可执行的脚本，同时也可以被其他脚本引入，但是只有一个程序是代码的入口，这个入口的__nam__就是__main__
# python隐含变量，函数入口的名称才是__main__，其他的是当前的模块名+文件名 __name__ is _06.model1
if __name__ == '__main__':
    print(__name__)


# python的函数不需要重载，因为可以支持默认值和可变参数
def fun(num=0):
    if num != 0:
        return num
    return -1


print(fun())
print(fun(1))


# 支持可变参数，用*变量来表示，传进来的是元组
def fun1(*num):
    for x in num:
        print(x)


fun1(1)  # output:1
fun1(1, 2)  # output:1,2
fun1(1, 2, 3)  # output:1,2,3


# 用**变量则标识，可变参数类型是字典
def fun2(**attr_dict):
    print(attr_dict)


fun2(a='a1')
fun2(a='a1', b='b2', c='c3')


# 将函数作为参数传递
def fun3(fun2):
    fun2(a='a1', b='b2', c='c3')


fun3(fun2)

"""
函数的定义中
def关键字，可以定义带有名称的函数 
Lambda关键字，可以定义匿名國数（无名称）
有名称的函数，可以基于名称重复使用。
无名称的匿名函数，只可临时使用一次。

匿名函数定义语法：
lambda 传入参数：函数体（一行代码）
lambda 是关键字，表示定义匿名函数
传入参数表示匿名函数的形式参数，如：×，y 表示接收2个形式参数
函数体，就是函数的执行逻辑，要注意：只能写一行，无法写多行代码
"""
fun3(lambda **x: print(f'匿名函数 and 执行逻辑{x}'))

# 后导入的包会覆盖前边的，从哪里导入什么
from _06.model1 import foo

foo()

from _06.model2 import foo

foo()

import _06.model1 as m1
import _06.model2 as m2

m1.foo()
m2.foo()

print(10 // 3)


# python 变量作用域
# Python中可以在函数内部再定义函数 内部函数可以访问外部函数变量 反之不行
def foo():
    # """三个引号 回车开始写python函数的注释
    """
    定义一个嵌套函数
    :return:
    """
    b = 'hello'

    # Python中可以在函数内部再定义函数
    def bar():
        c = True
        print(a)
        print(b)
        print(c)

    bar()
    # print(c)  # NameError: name 'c' is not defined


if __name__ == '__main__':
    a = 100
    # print(b)  # NameError: name 'b' is not defined
    foo()


# global 定义全局变量关键字 函数内部定义，外部也可以访问
# global a

# / 位置参数 3.8版本引入，表示位置参数结束的位置，该参数之前的参数只能用位置来传递，不能使用参数名来指定
def foo(a, b, /, c, d):
    print(a, b, c, d)


foo(1, 2, 3, 4)  # 输出: 1 2 3 4
# foo(a=1, b=2, c=3, d=4)  # 报错，不允许使用关键字参数传递位置参数之后的参数
foo(1, 2, d=4, c=3)  # 输出: 1 2 3 4，可以使用关键字参数传递位置参数之前的参数


# *
# keyword-only参数
# keyword-only参数是Python3中新加入的特性，比较不多见。
# 定义时有一个单独的*号，其实这也只是一种规定，*号看上去像是一个参数，其实它不占参数个数，是给解释器看的。规定*号后面的参数，能且只能用key=value的方式传入

def func(a, b, *, c=3, d=4):
    print("a:", a)
    print("b:", b)
    print("c:", c)
    print("d:", d)


if __name__ == '__main__':
    func(1, 2, c=3, d=4)

# 输出:
# a: 1
# b: 2
# c: 3
# d: 4


"""
在 Python 中，可以使用 * 操作符将一个列表（list）中的元素作为可变位置参数传递给函数。这个操作符的使用方式是在列表前面加上 *，例如 func(*my_list)。

下面是一个简单的示例，演示了如何将一个列表传入 *args 中：


在这个示例中，my_list 是一个包含整数的列表，我们希望将它作为位置参数传递给 my_function。通过使用 * 操作符，我们可以将列表中的元素拆分开来，作为多个位置参数传递给 my_function。

当然，这里要注意的是，*args 只是一种约定俗成的写法，你也可以使用其他名字，比如 *values、*items 等，只要在定义函数时加上 * 即可。
"""


def my_function(*args):
    print(*args, sep=',', end='.\n')


my_list = [*range(0, 6)]

my_function(my_list)  # [0, 1, 2, 3, 4, 5].
my_function(*my_list)  # 0,1,2,3,4,5.

# builtin 内建函数

# filter() 该函数返回的是一个generator  <filter object at 0x104d13fd0>
f = filter(lambda x: x > 5, range(10))
print(*f)

# map() <map object at 0x105543d00>
m = map(lambda x: x * 2, range(5))
print(m)  # <map object at 0x105543d00>
print(*m)  # 0 2 4 6 8

#  map对两个list做映射
mm = map(lambda x, y: x + y, range(5), range(5, 10))
print(*mm)  # 5 7 9 11 13

# zip() 将多个列表合并成为一个tuple
z = zip(range(0, 5), 'abcde', range(5, 10))
print(*z)  # (0, 'a', 5) (1, 'b', 6) (2, 'c', 7) (3, 'd', 8) (4, 'e', 9)

# dict() 转换成字典
print(dict(a='a', b='b'))  # {'a': 'a', 'b': 'b'}
print(dict(zip(range(0, 5), 'abcde')))  # {0: 'a', 1: 'b', 2: 'c', 3: 'd', 4: 'e'}
