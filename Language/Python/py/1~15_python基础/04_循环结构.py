# 数组求和

count_sum = 0
for x in [1, 2, 3]:
    count_sum += x
print(count_sum)

# range(0,10) 左开右闭
count_sum = 0
for x in range(1, 4):
    count_sum += x
print(count_sum)

print('1.1111保留两位小数%.2f ' % 1.1111)
print('range(100)的总数量 %.2i ' % len(range(100)))

for x in range(90, 100, 2):
    print(x)

# 判断素数
# number = int(input('输入一个正整数来判断是否是素数'))
# cur = 2
# flag = True
# while cur < number:
#     if number % cur == 0:
#         print("不是素数")
#         flag = False
#         break
#     cur+=1
# if flag:
#     print('是素数')

# 打印星号
# space = 5
# start = 1
# for row in range(5):
#     for sp in range(space):
#         print(' ', end="")
#     for sp in range(start):
#         print('*', end="")
#     print()
#     start += 2
#     space -= 1

# 列表推导式 list expression
print([x for x in range(0, 10)])  # output [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

# 生成器表达式 generator
print((x for x in range(0, 10)))  # output <generator object <genexpr> at 0x1043d4110>

# 这里是个语法糖
print(x for x in range(0, 10))

# 列表推导式使用
lst = [*range(0, 10)]
print(lst)  # 两种打印结果相同 [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

lst = [x for x in range(0, 10)]
print(lst)  # 两种打印结果相同 [0, 1, 2, 3, 4, 5, 6, 7, 8, 9]

# 过滤出lst中大于5的数字
print([x for x in lst if x > 5])  # output [6, 7, 8, 9]

# 还可以使用 builtin 函数 filter(),该函数返回的是一个generator
f = filter(lambda x: x > 5, lst)
print(f)  # <filter object at 0x104d13fd0>
print(*f)  # 6 7 8 9
