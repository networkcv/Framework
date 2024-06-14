import multiprocessing
import time


def task():
    # for i in range(10):
    while True:
        print("任务执行中...")
        time.sleep(0.2)


# 判断是否是直接执行的模块， 程序入口模块

# 标准python写法，直接执行的模块，需要加上判断是否是主模块的代码
if __name__ == '__main__':

    # 创建子进程
    sub_process = multiprocessing.Process(target=task)
    # 把子进程设置成为守护主进程，以后主进程退出子进程直接销毁
    # sub_process.daemon = True
    sub_process.start()

    # 主进程延时0.5秒钟
    time.sleep(0.5)
    # 退出主进程之前，先让子进程进行销毁
    sub_process.terminate()
    print("over")

# 结论: 主进程会等待子进程执行完成以后程序再退出

# 解决办法: 主进程退出子进程销毁
# 1. 让子进程设置成为守护主进程，主进程退出子进程销毁，子进程会依赖主进程
# 2. 让主进程退出之前先让子进程销毁