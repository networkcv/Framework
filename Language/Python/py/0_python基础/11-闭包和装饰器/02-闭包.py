# 闭包的作用：可以保存外部函数的变量

# 闭包的形成条件
# 1. 函数嵌套
# 2. 内部函数使用了外部函数的变量或者参数
# 3. 外部函数返回内部函数， 这个使用了外部函数变量的内部函数称为闭包


# 1. 函数嵌套
def func_out():
    # 外部函数
    num1 = 10

    def func_inner(num2):
        # 内部函数
        # 2. 内部函数必须使用了外部函数的变量
        result = num1 + num2
        print("结果:", result)

    # 3. 外部函数要返回内部函数，这个使用了外部函数变量的内部函数称为闭包
    return func_inner


# 获取闭包对象
# 这个new_func就是闭包
# 这里的new_func = func_inner
new_func = func_out()
# 执行闭包
new_func(1)
new_func(10)
