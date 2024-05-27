"""
测试目标
1. r+ 和 w+ a+ 区别:
2. 文件指针对数据读取的影响
"""
# r+:  r没有该文件则报错; 文件指针在开头，所以能读取出来数据
# f = open('test1.txt', 'r+')
# f = open('test.txt', 'r+')

# w+: 没有该文件会新建文件；w特点：文件指针在开头，用新内容覆盖原内容
# f = open('test1.txt', 'w+')
# f = open('test.txt', 'w+')

# a+:没有该文件会新建文件；文件指针在结尾，无法读取数据(文件指针后面没有数据)
f = open('resource/test.txt', 'a+')

con = f.read()
print(con)

f.close()

