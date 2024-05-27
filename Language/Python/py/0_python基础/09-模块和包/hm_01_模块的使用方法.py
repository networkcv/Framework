# 需求：math模块下sqrt() 开平方计算
"""
1. 导入模块
2. 测试是否导入成功：调用该模块内的sqrt功能
"""
# 方法一：import 模块名; 模块名.功能
# import math
# print(math.sqrt(9))


# 方法二： from 模块名 import 功能1, 功能2...; 功能调用（不需要书写模块名.功能）
# from math import sqrt
# print(sqrt(9))


# 方法三：from 模块名 import *; 功能调用（不需要书写模块名.功能）
from math import *
print(sqrt(9))



