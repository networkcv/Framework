import time


# 定义装饰器
def decorator(func):
    def inner():
        # 内部函数对已有函数进行装饰
        # 获取时间距离1970-1-1:0:0:1的时间差
        begin = time.time()
        func()
        end = time.time()

        result = end - begin
        print("函数执行完成耗时:", result)

    return inner


@decorator  # work = decorator(work), work = inner
def work():
    for i in range(10000):
        print(i)


work()
