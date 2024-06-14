import multiprocessing
import time


def task():
    time.sleep(1)
    # 获取当前线程
    print(multiprocessing.current_process())


if __name__ == '__main__':
    # 循环创建大量进程，测试进程之间执行是否无序
    for i in range(20):
        # 每循环一次创建一个子进程
        sub_process = multiprocessing.Process(target=task)
        # 启动子进程执行对应的任务
        sub_process.start()

    # 结论: 进程之间执行也是无序的，是由操作系统调度进程来决定的
