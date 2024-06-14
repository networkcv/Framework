# 1. 导入进程包
import multiprocessing
import os
import time


# 跳舞任务
def dance():
    # 获取当前进程（子进程）的编号
    dance_process_id = os.getpid()
    # 获取当前进程对象，查看当前代码是由那个进程执行的 ： multiprocessing.current_process()
    print("dance_process_id:", dance_process_id, multiprocessing.current_process())
    # 获取当前进程的父进程编号
    dance_process_parent_id = os.getppid()
    print("dance_process的父进程编号是:", dance_process_parent_id)

    for i in range(3):
        print("跳舞中...")
        time.sleep(0.2)
        # 扩展： 根据进程编号强制杀死指定进程
        os.kill(dance_process_id, 9)


# 唱歌任务
def sing():
    # 获取当前进程（子进程）的编号
    sing_process_id = os.getpid()
    # 获取当前进程对象，查看当前代码是由那个进程执行的 ： multiprocessing.current_process()
    print("sing_process_id:", sing_process_id, multiprocessing.current_process())

    # 获取当前进程的父进程编号
    sing_process_parent_id = os.getppid()
    print("sing_process的父进程编号是:", sing_process_parent_id)

    for i in range(3):
        print("唱歌中...")
        time.sleep(0.2)

# 这里必须要在main函数里，不然会报错，Linux系统fork; win:spawn mac支持：fork和spawn （python3.8默认设置spawn）
# multiprocessing.set_start_method('fork') mac手动指定fork创建进程的方式，也可以解决
if __name__ == '__main__':
    # 获取当前进程(主进程)的编号
    main_process_id = os.getpid()
    # 获取当前进程对象，查看当前代码是由那个进程执行的 ： multiprocessing.current_process()
    print("main_process_id:", main_process_id, multiprocessing.current_process())

    # 2. 创建子进程（自己手动创建的进程称为子进程, 在__init__.py文件中已经导入的Process类）
    # 1. group: 进程组，目前只能使用None，一般不需要设置
    # 2. target: 进程执行的目标任务
    # 3. name: 进程名，如果不设置，默认是Process-1, ......
    dance_process = multiprocessing.Process(target=dance, name="dance_process")
    print("dance_process:", dance_process)
    sing_process = multiprocessing.Process(target=sing, name="sing_process")
    print("sing_process:", sing_process)

    # 3. 启动进程执行对应的任务
    dance_process.start()
    sing_process.start()

    # 进程执行是无序的，具体那个进程先执行是由操作系统调度决定
    print()
