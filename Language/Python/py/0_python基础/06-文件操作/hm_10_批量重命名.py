# 需求1：把code文件夹所有文件重命名 Python_xxxx
# 需求2： 删除Python_ 重命名：1， 构造条件的数据 2. 书写if
import os

# 构造条件的数据
flag = 2

# 1. 找到所有文件： 获取code文件夹的目录列表 -- listdir()
file_list = os.listdir()
print(file_list)

# 2. 构造名字
for i in file_list:
    if flag == 1:
        # new_name = 'Python_' + 原文件i
        new_name = 'Python_' + i
    elif flag == 2:
        # 删除前缀
        num = len('Python_')
        new_name = i[num:]
# 3. 重命名
    os.rename(i, new_name)
