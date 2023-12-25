# Python中下划线的5种含义 https://mp.weixin.qq.com/s/Z9BqZrsZVZgSeja9VYdK0w
import random

# python 中推荐的变量命名方式
# 常量命名 用大写字母+大驼峰的方式
CONSTANT_VALUE = 0


# 类命名，用大驼峰的方式
class MyClass:
    pass


# module,function,variable,method,小写下划线方式
def my_method():
    pass


## python 下划线

# 1.下划线
# _ 有时用作临时或无意义变量的名称。也表示Python REPL中最近一个表达式的结果。告诉看这段代码的人，没有使用range(10)里边的元素
for _ in range(1):
    print(random.random())


# 2. _var  单前导下划线变量
# 弱私有，命名约定，仅供内部使用。通常不会由Python解释器强制执行（通配符导入除外），只作为对程序员的提示。强行使用还是可以使用的。
class MyClass:
    def _get_raw(self):
        print("Oh no")


o = MyClass()
o._get_raw()  # Oh no


# 3. __var 双前导下划线变量
# 强私有，当在类上下文中使用时，触发 "名称修饰"。由Python解释器强制执行，实际是python解释器是修改了对应变量的命名来防止误用，如果前边加上_类名+__变量名，还是能访问到
class MyClass:
    def __get_raw(self):
        print("Oh no")


o = MyClass()
# o._get_raw() # error AttributeError: 'MyClass' object has no attribute '_get_raw'
o._MyClass__get_raw()  # Oh no

# 4. __var__ 双前导和双末尾下划线
# 表示python中的魔术方法，表示Python语言定义的特殊方法。不要在你自己的属性中使用这种命名方案。

# 单末尾下划线
# var_
# 按约定使用以避免与Python关键字的命名冲突。
list_ = list(range(3))

# 注意不要在起名的时候，覆盖builtin 方法,对文件起名的时候，主要不要和module重复。
