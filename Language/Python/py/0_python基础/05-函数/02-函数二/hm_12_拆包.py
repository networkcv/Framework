# 1. 拆包元组数据
def return_num():
    return 100, 200, 300


#

result = return_num()
print(result)
num1, num2, num3 = return_num()
print(num1)
print(num2)
print(num3)

# 2. 字典数据拆包: 变量存储的数据是key值
# 先准备字典，然后拆包
dict1 = {'name': 'TOM', 'age': 20, 'level': 1}
# dict1中有两个键值对，拆包的时候用两个变量接收数据
a, b, c = dict1
print(a)
print(b)
print(c)

# v值
print(dict1[a])
print(dict1[b])
print(dict1[c])

a = 1
print(id(1))
print(id(2))
print(id(a))
