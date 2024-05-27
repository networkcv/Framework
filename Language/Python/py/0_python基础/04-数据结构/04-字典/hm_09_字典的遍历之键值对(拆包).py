dict1 = {'name': 'TOM', 'age': 20, 'gender': '男'}

# xx.items(): 返回可迭代对象，内部是元组，元组有2个数据
# 元组数据1是字典的key，元组数据2是字典的value
for key, value in dict1.items():
    # print(key)
    # print(value)
    # 目标： key=value
    print(f'{key}={value}')

