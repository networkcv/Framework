# 1. 声明全局变量；2. 定义两个函数；3. 函数一修改全局变量；函数2访问全局变量
glo_num = 0


def test1():
    global glo_num
    glo_num = 100


def test2():
    print(glo_num)


print(glo_num)  # 0, 因为修改的函数没执行
test2()  # 0 , 因为修改的函数没执行
test1()
test2()  # 100，先调用了函数1
print(glo_num)  # 100 ， 调用了函数1
