# 寻找水仙数
for x in range(100, 1000):
    num_100 = x // 100
    num_10 = x // 10 % 10
    num_0 = x % 10
    if (num_0 ** 3 + num_10 ** 3 + num_100 ** 3) == x:
        print('水仙数字:%s' % x)

# 正整数的反转
num = int(input("输入正整数:"))
reversed_num = 0
while num > 0:
    reversed_num = num % 10 + reversed_num * 10
    num //= 10
print(reversed_num)

# 百钱百鸡
"""
公鸡5元一只，母鸡3元一只，小鸡一元三只，100元买100只鸡，每种鸡各多少
"""
for x in range(20):
    for y in range(33):
        z = 100 - x - y
        if 5 * x + 3 * y + z / 3 == 100:
            print("x:%d y:%d z:%d " % (x, y, z))


# 斐波那契数列
a = 0
b = 1
for x in range(20):
    a, b = b, a + b
    print(a)

# 判断素数
number = int(input('输入一个正整数来判断是否是素数'))
cur = 2
flag = True
while cur < number:
    if number % cur == 0:
        print("不是素数")
        flag = False
        break
    cur += 15
if flag:
    print('是素数')
