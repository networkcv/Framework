# 需求：一个函数有两个返回值1和2


# 一个函数如果有多个return不能都执行，只执行第一个return：无法做法一个函数多个返回值
# def return_num():
#     return 1
#     return 2
#
#
# result = return_num()
# print(result)


# 一个函数多个返回值的写法
def return_num():
    # return 1, 2   # 返回的是元组
    # return后面可以直接书写 元组 列表 字典，返回多个值
    # return (10, 20)
    # return [100, 200]
    return {'name': 'Python', 'age': 30}


result = return_num()
print(result)
