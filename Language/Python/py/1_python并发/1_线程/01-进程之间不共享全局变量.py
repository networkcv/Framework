import multiprocessing
import time


print("xxx")
# 定义全局变量列表
g_list = list()  #=> []


# 添加数据的任务
def add_data():
    for i in range(3):
        # 因为列表是可变类型，可以在原有内存的基础上修改数据，并且修改后内存地址不变
        # 所以不需要加上global关键字
        # 加上global 表示声明要修改全局变量的内存地址
        g_list.append(i)
        print("add:", i)
        time.sleep(0.2)

    print("添加完成:", g_list)


# 读取数据的任务
def read_data():
    print("read:", g_list)



# 提示： 对应linux和mac主进程执行的代码不会进程拷贝，但是对应window系统来说主进程执行的代码也会进行拷贝执行
# 对应window来说创建子进程的代码如果进程拷贝执行相当于递归无限制进行创建子进程，会报错

# 如果解决windows递归创建子进程，通过判断是否是主模块来解决

# 理解说明： 直接执行的模块就是主模块，那么直接执行的模块里面就应该添加判断是否是主模块的代码

# 1. 防止别人导入文件的时候执行main里面的代码
# 2. 防止windows系统递归创建子进程
# if __name__ == '__main__':

    # 添加数据的子进程
add_process = multiprocessing.Process(target=add_data)
# 读取数据的子进程
read_process = multiprocessing.Process(target=read_data)

# 启动进程执行对应的任务
add_process.start()
# 当前进程（主进程）等待添加数据的进程执行完成以后代码再继续往下执行
add_process.join()
print("main:", g_list)
read_process.start()

# 结论： 进程之间不共享全局变量

# 扩展：
# fork 函数， 创建子进程都需要对主进程的代码进行拷贝
# fork mac 和 linux python解释器里面都有fork函数，通过fork函数去创建的子进程