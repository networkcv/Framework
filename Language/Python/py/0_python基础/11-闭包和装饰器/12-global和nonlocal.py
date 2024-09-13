"""
在 Python 中，global 和 nonlocal 关键字都用于在函数内部修改变量，但它们的用途和行为有所不同。

global 关键字
global 关键字用于在函数内部修改全局变量。全局变量是在模块级别定义的变量，也就是说，它们在函数外部的整个模块范围内都是可见的。
"""

# 全局变量
global_var = 10


def my_function():
    global global_var  # 声明 global_var 为全局变量
    global_var = 20  # 修改全局变量的值


my_function()
print(global_var)  # 输出 20


# nonlocal 关键字
# nonlocal 关键字用于在嵌套函数中修改封闭作用域（非全局作用域）的变量。这通常用在闭包中，当你想在内部函数中修改外部函数的变量时。


def outer_function():
    # 外部函数的局部变量
    local_var = 10

    def inner_function():
        nonlocal local_var  # 声明 local_var 为非局部变量
        local_var = 20  # 修改封闭作用域的变量

    inner_function()
    return local_var


result = outer_function()
print(result)  # 输出 20

# 区别
# global 用于修改全局变量，即在函数外部定义的变量。
# nonlocal 用于修改封闭作用域中的变量，即在非全局的外部函数中定义的变量。

# 使用场景
# 使用 global 当你需要在函数内部修改在模块级别定义的变量时。
# 使用 nonlocal 当你需要在一个函数内部修改另一个非全局（通常是嵌套函数）作用域中的变量时。
# 注意事项
# 过度使用
# global 和 nonlocal 可能会使代码难以理解和维护，因为它们破坏了变量的封装性。
# 在可能的情况下，考虑使用函数参数和返回值来传递数据，或者使用面向对象的方法来封装数据和行为。
