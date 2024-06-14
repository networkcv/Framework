import threading


def show_info(name, age):
    print("name: %s age: %d" % (name, age))

if __name__ == '__main__':
    # 创建子线程
    # 以元组方式传参，要保证元组里面元素的顺序和函数的参数顺序一致
    # sub_thread = threading.Thread(target=show_info, args=("李四", 20))
    # # 启动线程执行对应的任务
    # sub_thread.start()

    # 以字典的方式传参，要保证字典里面的key和函数的参数名保持一致
    sub_thread = threading.Thread(target=show_info, kwargs={"name": "王五", "age": 30})
    # 启动线程执行对应的任务
    sub_thread.start()