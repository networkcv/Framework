import threading
import time


def task():
    time.sleep(1)
    # 获取当前线程
    print(threading.current_thread())


if __name__ == '__main__':
    # 循环创建大量线程，测试线程之间执行是否无序
    for i in range(20):
        # 每循环一次创建一个子线程
        sub_thread = threading.Thread(target=task)
        # 启动子线程执行对应的任务
        sub_thread.start()

    # 线程之间执行是无序的，具体那个线程执行是由cpu调度决定的