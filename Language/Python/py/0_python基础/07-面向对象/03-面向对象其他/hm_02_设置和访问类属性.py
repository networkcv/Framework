# 1. 定义类，定义类属性
class Dog(object):
    tooth = 10


# 2. 创建对象
wangcai = Dog()
xiaohei = Dog()

# 3. 访问类属性： 类和对象
print(Dog.tooth)
print(wangcai.tooth)
print(xiaohei.tooth)
