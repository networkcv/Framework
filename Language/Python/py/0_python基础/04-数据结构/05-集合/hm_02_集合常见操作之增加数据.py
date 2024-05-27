s1 = {10, 20}
# 1. 集合是可变类型
# add()
# s1.add(100)
# print(s1)

# 集合有去重功能，如果追加的数据是集合已有数据，则什么事情都不做
# s1.add(100)
# print(s1)

# s1.add([10, 20, 30])  # 报错
# print(s1)

# update()： 增加的数据是序列
s1.update([10, 20, 30, 40, 50])
print(s1)

# s1.update(100)  # 报错
# print(s1)



