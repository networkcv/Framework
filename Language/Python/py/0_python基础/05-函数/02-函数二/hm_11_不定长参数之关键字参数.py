# 收集所有关键字参数，返回一个字典
def user_info(**kwargs):
    print(kwargs)


user_info()
user_info(name='TOM')
user_info(name='TOM', age=20)
