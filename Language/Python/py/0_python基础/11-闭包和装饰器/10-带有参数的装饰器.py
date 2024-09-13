def return_decorator(flag):
    # 装饰器, 装饰器只能接收一个参数并且是函数类型
    def decorator(func):
        def inner(a, b):
            if flag == "+":
                print("正在努力执行加法计算")
            elif flag == "-":
                print("正在努力执行减法计算")
            func(a, b)

        return inner

    # 当调用函数的时候可以返回一个装饰器decorator
    return decorator


# 加法计算
@return_decorator("+")  # decorator = return_decorator("+"), @decorator => add_num=decorator(add_num)
def add_num(a, b):
    result = a + b
    print(result)


add_num(1, 2)

# # 减法计算
# @return_decorator("-")
# def sub_num(a, b):
#     result = a - b
#     print(result)
#
# add_num(1, 2)
# sub_num(1, 4)

# 带有参数的装饰器，其实就是定义了一个函数，让函数接收参数，在函数内部返回的是一个装饰器
