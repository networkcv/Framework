list1 = ['a', 'b', 'c', 'd', 'e']

# enumerate 返回结果是元组，元组第一个数据是原迭代对象的数据对应的下标，元组第二个数据是原迭代对象的数据
# for i in enumerate(list1):
#     print(i)

for i in enumerate(list1, start=1):
    print(i)

