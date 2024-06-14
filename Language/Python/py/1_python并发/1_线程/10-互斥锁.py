import threading


# 全局变量
g_num = 0


# 创建互斥锁, Lock本质上是一个函数，通过调用函数可以创建一个互斥锁
lock = threading.Lock()


# 循环100万次执行的任务
def task1():
    # 上锁
    lock.acquire()
    for i in range(1000000):
        # 每循环一次给全局变量加1
        global g_num  # 表示要声明修改全局变量的内存地址
        g_num = g_num + 1  # g_num += 1

    # 代码执行到此，说明数据计算完成
    print("task1:", g_num)
    # 释放锁
    lock.release()


# 循环100万次执行的任务
def task2():
    # 上锁
    lock.acquire()
    for i in range(1000000):
        # 每循环一次给全局变量加1
        global g_num  # 表示要声明修改全局变量的内存地址
        g_num = g_num + 1  # g_num += 1

    # 代码执行到此，说明数据计算完成
    print("task2:", g_num)
    # 释放锁
    lock.release()


if __name__ == '__main__':
    # 创建两个子线程
    first_thread = threading.Thread(target=task1)
    second_thread = threading.Thread(target=task2)

    # 启动线程执行任务
    first_thread.start()

    second_thread.start()

    # 互斥锁可以保证同一时刻只有一个线程去执行代码，能够保证全局变量的数据没有问题
    # 线程等待和互斥锁都是把多任务改成单任务去执行，保证了数据的准确性，但是执行性能会下降