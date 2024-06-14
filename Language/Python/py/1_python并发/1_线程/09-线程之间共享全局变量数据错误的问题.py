import threading


# 全局变量
g_num = 0


# 循环100万次执行的任务
def task1():
    for i in range(1000000):
        # 每循环一次给全局变量加1
        global g_num  # 表示要声明修改全局变量的内存地址
        g_num = g_num + 1  # g_num += 1

    # 代码执行到此，说明数据计算完成
    print("task1:", g_num)


# 循环100万次执行的任务
def task2():
    for i in range(1000000):
        # 每循环一次给全局变量加1
        global g_num  # 表示要声明修改全局变量的内存地址
        g_num = g_num + 1  # g_num += 1

    # 代码执行到此，说明数据计算完成
    print("task2:", g_num)


if __name__ == '__main__':
    # 创建两个子线程
    first_thread = threading.Thread(target=task1)
    second_thread = threading.Thread(target=task2)

    # 启动线程执行任务
    first_thread.start()
    # 线程等待，让第一个线程先执行，然后在让第二个线程再执行，保证数据不会有问题
    first_thread.join() # 主线程等待第一个子线程执行完成以后代码再继续往下执行
    second_thread.start()