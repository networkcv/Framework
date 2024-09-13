# 接收所有位置参数，返回一个元组
def user_info(*args):
    print(args)


user_info('TOM')
user_info('TOM', 20)
user_info('TOM', 20, 'man')
user_info()


def user_info(*args):
    print(f'{args} {type(args)}')


user_info()


def user_info(name, age, gender, *args):
    print(f'您的姓名是{name}, 年龄是{age}, 性别是{gender} {args}')


user_info(20, 'TOM', '男', '黑色衣服', '白色帽子')
