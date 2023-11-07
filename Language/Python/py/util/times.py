import time


# 返回格式化后的当前时间
def cur_ft_time():
    return time.strftime("%Y-%m-%d %H:%M:%S", time.localtime(time.time()))
