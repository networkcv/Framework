list1 = [1, 2, 3, 4, 5]

# 1. 导入模块
import functools


# 2. 定义功能函数
def func(a, b):
    return a + b


# 3. 调用reduce，作用：功能函数计算的结果和序列的下一个数据做累计计算
result = functools.reduce(func, list1)
print(result)

