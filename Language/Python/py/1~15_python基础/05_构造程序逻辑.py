# # 反转数字
# num = int(input('num = '))
# reversed_num = 0
# while num > 0:
#     reversed_num = reversed_num * 10 + num % 10
#     num //= 10
# print(reversed_num)

# 反转字符串

# strList = list(input("str="))
# strLen = len(strList)
# strIndex = strLen - 1
#
#
# def swap(_strList, _i, _i2):
#     tmp = _strList[_i]
#     _strList[_i] = _strList[_i2]
#     _strList[_i2] = tmp
#     pass
#
#
# for i in range(strLen // 2):
#     # # 传统做饭
#     # swap(strList, i, strIndex - i)
#     # # python提供了不用中间变量，直接交换变量值的方法
#     strList[i], strList[strIndex - i] = strList[strIndex - i], strList[i]
# print("".join(strList))

# 百钱百鸡
# """
# 公鸡5元一只，母鸡3元一只，小鸡一元三只，100元买100只鸡，每种鸡各多少
# """
# for x in range(20):
#     for y in range(33):
#         z = 100 - x - y
#         if 5 * x + 3 * y + z / 3 == 100:
#             print("x:%d y:%d z:%d " % (x, y, z))


# 斐波那契数列
# a = 0
# b = 1
# for x in range(20):
#     a, b = b, a + b
#     print(a)

# 判断素数
# number = int(input('输入一个正整数来判断是否是素数'))
# cur = 2
# flag = True
# while cur < number:
#     if number % cur == 0:
#         print("不是素数")
#         flag = False
#         break
#     cur+=15
# if flag:
#     print('是素数')
