# python的函数不需要重载，因为可以支持默认值和可变参数
def fun(num=0):
    if num != 0:
        return num
    return -1


print(fun())
print(fun(1))


# 支持可变参数，用*变量来表示，传进来的是数组
def fun1(*num):
    for x in num:
        print(x)


fun1(1)  # output:1
fun1(1, 2)  # output:1,2
fun1(1, 2, 3)  # output:1,2,3

# 后导入的包会覆盖前边的，从哪里导入什么
from _06.model1 import foo

foo()

from _06.model2 import foo

foo()

import _06.model1 as m1
import _06.model2 as m2

m1.foo()
m2.foo()

# python隐含变量，函数入口的名称才是__main__，其他的是当前的模块名+文件名 __name__ is _06.model1
print(__name__)
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
