class Dog(object):
    tooth = 10


wangcai = Dog()
xiaohei = Dog()

# 1. 类 类.类属性 = 值
# Dog.tooth = 20
# print(Dog.tooth)
# print(wangcai.tooth)
# print(xiaohei.tooth)


# 2. 测试通过对象修改类属性
wangcai.tooth = 200

print(Dog.tooth)  # 10
print(wangcai.tooth)  # 200
print(xiaohei.tooth)  # 10


