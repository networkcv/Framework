# 死锁: 一直等待对方释放锁的情景叫做死锁
import threading


# 创建互斥锁
lock = threading.Lock()


# 需求: 多线程同时根据下标在列表中取值，要保证同一时刻只能有一个线程去取值
def get_value(index):
    # 上锁
    lock.acquire()
    my_list = [1, 4, 6]
    # 判断下标是否越界
    if index >= len(my_list):
        print("下标越界:", index)
        # 取值不成功，也需要释放互斥锁，不要影响后面的线程去取值
        # 锁需要在合适的地方进行释放，防止死锁
        lock.release()
        return

    # 根据下标取值
    value = my_list[index]
    print(value)
    # 释放锁
    lock.release()


if __name__ == '__main__':
    # 创建大量线程，同时执行根据下标取值的任务
    for i in range(10):
        # 每循环一次创建一个子线程
        sub_thread = threading.Thread(target=get_value, args=(i,))
        # 启动线程执行任务
        sub_thread.start()