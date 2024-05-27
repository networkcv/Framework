"""
1. input

2. 检测input数据类型str

3. int() 转换数据类型

4. 检测是否转换成功
"""
num = input('请输入数字：')
print(num)

print(type(num))  # str

print(type(int(num)))  # int

