import threading
import time


# 定义全局变量
g_list = []


# 添加数据的任务
def add_data():
    for i in range(3):
        # 每循环一次把数据添加到全局变量
        g_list.append(i)
        print("add:", i)
        time.sleep(0.3)

    # 代码执行到此，说明添加数据完成
    print("添加数据完成:", g_list)


# 读取数据的任务
def read_data():
    print("read:", g_list)


if __name__ == '__main__':
    # 创建添加数据的子线程
    add_thread = threading.Thread(target=add_data)
    # 创建读取数据的子线程
    read_thread = threading.Thread(target=read_data)

    # 启动线程执行对应的任务
    add_thread.start()
    # time.sleep(1)
    # 让当前线程(主线程)等待添加数据的子线程执行完成以后代码在继续执行
    add_thread.join()
    read_thread.start()

# 结论: 线程之间共享全局变量