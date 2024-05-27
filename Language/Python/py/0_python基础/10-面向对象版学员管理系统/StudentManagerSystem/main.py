# 1. 导入管理系统模块
from managerSystem import *

# 2. 启动管理系统
# 保证是当前文件运行才启动管理系统：if --创建对象并调用run方法
if __name__ == '__main__':
    student_manager = StudentManager()
    student_manager.run()



