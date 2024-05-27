# 1. 自己的文件名不能和已有模块名重复，如果重复导致模块无法使用 -- random
# import random
# num = random.randint(1, 5)
# print(num)


# 2. 当使用from xx import 功能 导入模块的功能的时候，如果功能名字重复，导入的时候后定义或后导入的这个同名功能
# 场景 time.sleep()
# def sleep():
#     print('我是自定义的sleep')
#
#
# from time import sleep
#
# # 定义函数 sleep
# # def sleep():
# #     print('我是自定义的sleep')
#
# sleep(2)


# 拓展： 名字重复
# 问题：import 模块名  是否担心 功能名字重复的问题  -- 不需要
import time
print(time)

time = 1
print(time)

# 问题：为什么变量也能覆盖模块？ -- 在Python语言中，数据是通过 引用 传递的。


