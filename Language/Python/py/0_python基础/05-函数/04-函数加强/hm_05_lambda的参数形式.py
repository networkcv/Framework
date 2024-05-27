# 1. 无参数
# fn1 = lambda: 100
# print(fn1())


# 2. 一个参数
# fn2 = lambda a: a
# print(fn2('hello world'))


# 3. 默认参数/缺省参数
# fn3 = lambda a, b, c=100: a + b + c
# print(fn3(10, 20))
# print(fn3(10, 20, 200))


# 4. 可变参数：*args
# fn4 = lambda *args: args
# print(fn4(10, 20))
# print(fn4(10, 20, 30, 40))
# print(fn4(10))


# 5. 可变参数：**kwargs
fn5 = lambda **kwargs: kwargs
print(fn5(name='Python'))
print(fn5(name='Python', age=30))

