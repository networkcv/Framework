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
