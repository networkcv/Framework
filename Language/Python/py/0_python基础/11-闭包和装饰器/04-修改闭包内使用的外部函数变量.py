# 定义一个外部函数
def func_out(num1):
    # 定义一个内部函数
    def func_inner(num2):
        # 这里本意想要修改外部num1的值，实际上是在内部函数定义了一个局部变量num1
        # nonlocal num1
        num1 = 10
        # 内部函数使用了外部函数的变量(num1)
        result = num1 + num2
        print("结果是:", result)

    print(num1)
    func_inner(1)
    print(num1)

    # 外部函数返回了内部函数，这里返回的内部函数就是闭包
    return func_inner


# 创建闭包实例
f = func_out(1)
# 执行闭包
f(2)


# 1. 函数嵌套
def func_out():
    # 外部函数的变量
    num1 = 10

    def func_inner():
        # 在闭包内修改外部函数的变量
        # num1 = 20  # 本意是修改外部函数变量， 其实是在闭包内定义了一个局部变量
        # 在闭包内修改外部函数的变量需要使用nonlocal关键字
        nonlocal num1
        num1 = 20

        # 2.内部要使用外部函数的变量
        result = num1 + 10
        print(result)

    print("修改前的外部变量:", num1)
    func_inner()
    print("修改后的外部变量:", num1)

    # 3. 返回内部函数
    return func_inner

# 创建闭包对象
# new_func = func_out()
# new_func()
