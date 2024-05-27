# a = 10
# b = 20

# 1. 方法一
"""
1.1 定义中间的第三变量，为了临时存储a或b的数据
1.2 把a的数据存储到c，做保存
1.3 把b的数据赋值到a， a = 20
1.4 把c的数据赋值到b， b = 10
"""
# c = 0
# c = a
# a = b
# b = c
#
# print(a)
# print(b)


a, b = 1, 2
print(a)
print(b)


a, b = b, a
print(a)
print(b)
