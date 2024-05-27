import os

# 定义要删除的文件类型
file_type = '.m4a'

# 定义要删除的文件夹路径
folder_path = '/Users/networkcavalry/Study'

# 遍历文件夹下所有文件
for root, dirs, files in os.walk(folder_path):
    for file in files:
        # 判断文件是否为指定类型
        if file.endswith(file_type):
            # 构造文件路径
            file_path = os.path.join(root, file)
            # 删除文件
            os.remove(file_path)