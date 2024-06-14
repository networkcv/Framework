# 1. 导入进程包
import multiprocessing
import time


# 跳舞任务
def dance():
    for i in range(3):
        print("跳舞中...")
        time.sleep(0.2)


# 唱歌任务
def sing():
    for i in range(3):
        print("唱歌中...")
        time.sleep(0.2)


# 2. 创建子进程（自己手动创建的进程称为子进程, 在__init__.py文件中已经导入的Process类）
# 1. group: 进程组，目前只能使用None，一般不需要设置
# 2. target: 进程执行的目标任务
# 3. name: 进程名，如果不设置，默认是Process-1, ......
dance_process = multiprocessing.Process(target=dance)
sing_process = multiprocessing.Process(target=sing)

# 3. 启动进程执行对应的任务
dance_process.start()
sing_process.start()

# 进程执行是无序的，具体那个进程先执行是由操作系统调度决定
