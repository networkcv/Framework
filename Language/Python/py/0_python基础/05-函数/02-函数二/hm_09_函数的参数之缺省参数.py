def user_info(name, age, gender='男'):
    print(f'您的姓名是{name}, 年龄是{age}, 性别是{gender}')


user_info('TOM', 18)  # 没有为缺省参数传值，表示使用默认值
user_info('TOM', 18, gender='女')  # 为缺省参数传值，使用这个值，即修改了默认值

