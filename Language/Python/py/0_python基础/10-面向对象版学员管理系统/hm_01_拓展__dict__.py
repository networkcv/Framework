# 1. 定义类
class A(object):
    a = 0

    def __init__(self):
        self.b = 1


# 2. 创建对象
aa = A()

# 3. 调用__dict__
print(A.__dict__)

print(aa.__dict__)



