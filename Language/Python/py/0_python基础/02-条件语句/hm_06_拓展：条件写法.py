
age = int(input('请输入您的年龄：'))

# 童工
if age < 18:
    print(f'您输入的年龄是{age}, 童工')

# 18-60 合法
elif 18 <= age <= 60:
    print(f'您输入的年龄是{age}, 合法')

# 大于60 退休
elif age > 60:
    print(f'您输入的年龄是{age}, 退休年龄')


