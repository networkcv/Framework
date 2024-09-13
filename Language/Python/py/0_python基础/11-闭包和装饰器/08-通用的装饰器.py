# 通用的装饰器: 可以装饰任意类型的函数

# --------装饰带有参数的函数-----------


# 定义装饰器
# def decorator(func):
#     # 使用装饰器装饰已有函数的时候，内部函数的类型和要装饰的已有函数的类型保持一致
#     def inner(a, b):
#         # 在内部函数对已有函数进行装饰
#         print("正在努力执行加法计算")
#         func(a, b)
#
#     return inner
#
#
# # 用装饰器语法糖方式装饰带有参数的函数
# @decorator  #  add_num= decorator(add_num), add_num=inner
# def add_num(num1, num2):
#     result = num1 + num2
#     print("结果为:", result)
#
# add_num(1, 2)

# ------装饰带有参数和返回值的函数
# def decorator(func):
#     # 使用装饰器装饰已有函数的时候，内部函数的类型和要装饰的已有函数的类型保持一致
#     def inner(a, b):
#         # 在内部函数对已有函数进行装饰
#         print("正在努力执行加法计算")
#         num = func(a, b)
#         return num
#
#     return inner
#
#
# # 用装饰器语法糖方式装饰带有参数的函数
# @decorator  #  add_num= decorator(add_num), add_num=inner
# def add_num(num1, num2):
#     result = num1 + num2
#     return result
#
# result = add_num(1, 2)
# print("结果为:", result)


# ----装饰带有不定长参数和返回值的函数-------

# 该装饰器还可以成为是通用的装饰器
def decorator(func):
    # 使用装饰器装饰已有函数的时候，内部函数的类型和要装饰的已有函数的类型保持一致
    def inner(*args, **kwargs):
        # 在内部函数对已有函数进行装饰
        print("正在努力执行加法计算")

        # *args: 把元组里面的每一个元素，按照位置参数的方式进行传参
        # **kwargs: 把字典里面的每一个键值对，按照关键字的方式进行传参
        # 这里对元组和字典进行拆包，仅限于结合不定长参数的函数使用
        num = func(*args, **kwargs)
        return num

    return inner


@decorator  # show= decorator(show) show = inner
def show():
    return "哈哈"


result = show()
print(result)

# 用装饰器语法糖方式装饰带有参数的函数
# @decorator  #  add_num= decorator(add_num), add_num=inner
# def add_num(*args, **kwargs):
#     result = 0
#
#     # args: 元组类型
#     # kwargs: 字典类型
#
#     for value in args:
#         result += value
#
#     for value in kwargs.values():
#         result += value
#
#     return result

# result = add_num(1, 2)
# print("结果为:", result)


# if __name__ == '__main__':
# my = {"a":1}
# print(**my)
