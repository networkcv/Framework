# 需求： 运行后暂停2秒打印hello
"""
1. 导入time模块或导入time模块的sleep功能
2. 调用功能
3. 打印hello
"""

# 1. 模块别名
# import time as tt
# tt.sleep(2)
# time.sleep(2)
# print('hello')

# 2. 功能别名
from time import sleep as sl
sl(2)
print('hello')



