#!/usr/bin/env python3
# -*- coding:utf-8 -*-
# Hello Python
# print("hello python")


# 格式化输出
# string = "hello,%s"%("S")
# print(string)

# string = "hello%s%s%s"%("ws","+","d")
# print(string)

# # %2d 表示这个变量至少占用2个位置
# string ="-------------------------------"
# print(string)
# string = "%2d"%3
# # -------------------------
# #  3
# print(string)

# # 如果是%02d的话还会自动在前边补0
# string = "%02d"%3
# # --------------------------
# # 03
# print(string)
# string = "%d"%3
# # --------------------------
# # 3
# print(string)

# # %.3f表示小数点后保留3位小数
# string = "%.3f"%3.1415926541413
# # --------------------------
# # 3
# print(string)

# s1 = 72
# s2 = 85
# print("提升了%.1f个百分点"%(82/72))
# s=82/72
# print("%s"%s)
# print("%d"%s)
# print("%2d"%s)
# print("%f"%s)
# print("%2f"%s)


# list
# classmates = ["a","b",'c']
# a = len(classmates)
# print(a)
# print(classmates)
# classmates.append("d")
# print(classmates)
# classmates.insert(1,"0")
# print(classmates)
# classmates.pop(0)
# print(classmates)
# print(classmates)
# classmates.pop()
# print(classmates)


# tuple
# tuple1 =(["1","2"],["a","b"])
# print(tuple1[1][1])


# 条件判断
# x=input("请输入一个数字,")
# x = 6
# if int(x) > 5:
#     print("大于5")
# elif int(x) > 10:
#     print("大于10")
# else:
#     print("比6小")


# 循环循环
# names = ["tom","jack","rose"]
# for x in names:
#     print(x)


# set
# l=["1","2","3"]
# s=set(l)
# s.add(l)
# print(s)

# (1,2,3) (1,[2,3])放入dict set
# t1=(1,2,3)
# t2=(1,[2,3])

# dict
# d = {"a": 1, "b": 2, "c": 3}
# print(d)
# d["d"] = t1
# print(d)
# d["d"] = t2
# print(d)
# {'a': 1, 'b': 2, 'c': 3}
# {'a': 1, 'b': 2, 'c': 3, 'd': (1, 2, 3)}
# {'a': 1, 'b': 2, 'c': 3, 'd': (1, [2, 3])}

# c =set()
# t1=(1,2,3)k
# c.add(t1)
# c.add(t2)
# Traceback (most recent call last):
#   File "d:\Note\Python\test.py", line 49, in <module>
#     c.add(t2)
# TypeError: unhashable type: 'list'
# print(c)


# python数据类型总结
# list 和 tuple两者都是有序可重复的 区别是 list里 可以放可变的元素 而tuple里只能放不可变的元素  list[] tuple()创建后就不能修改，只能读取
# dict = {"a":1,"b":2,"c":3} dict["a"] dict.get("key") dict.pop("key") key 必须为不可变对象
# set 无序不重复的 用set()初始化，带输入集合初始化 add(key) remove(key) lsit是可变的无法作为key，因为无法判读key是否重复


# 借调函数
# a = abs
# print(a(-1))

# 输入一个整数转化为16进制数
# i =input("请输入一个整数，")
# print(hex(int(i)))


# 猜数字游戏
# import random
# num = random.randint(1, 100)
# count = 6
# flag = True
# while flag:
#     i = input("请输入你猜的数字(1~100)：")
#     if i=="help":
#         print("提示：%d~%d" % (num-5, num+5))
#     else:
#         i = int(i)
#         if i == num:
#             flag = False
#             print("恭喜你猜对了,你真是个天才")
#         elif i > num:
#             if count == 1:
#                 print("game over !! loser")
#                 print("正确答案：", num)
#                 flag = False
#             else:
#                 count = count-1
#                 print("猜大了")
#                 print("你还剩%d次机会\n" % (count))
#         elif i < num:
#             if count == 1:
#                 print("game over loser")
#                 print("正确答案：", num)
#                 flag = False
#             else:
#                 print("猜小了")
#                 count = count-1
#                 print("你还剩%d次机会\n" % (count))


# 函数的参数
# def hello(greeting, *args):
#     if (len(args) == 0):
#         print('%s!' % greeting)
#     else:
#         print('%s, %s!' % (greeting, ', '.join(args)))

# hello('Hi')  # => greeting='Hi', args=()
# hello('Hi', 'Sarah') # => greeting='Hi', args=('Sarah')
# hello('Hello', 'Michael', 'Bob', 'Adam') # => greeting='Hello', args=('Michael', 'Bob', 'Adam')

# names = ('Bart', 'Lisa')
# hello('Hello', *names) # => greeting='Hello', args=('Bart', 'Lisa')

# 必选参数 默认参数 可变参数/命名关键字参数 关键字参数
# def hello(a,b,c=0,*args,**kw):
#     print("a=",a,"b=",b,"c=",c,"args=",args,"kw=",kw)

# def hello1(a,b,c=0,*,d,**kw):
#     print("a=",a,"b=",b,"c=",c,"d=",d,"kw=",kw)

# hello(1,2) #--a= 1 b= 2 c= 0 args= () kw= {}
# hello(1,2,3) #--a= 1 b= 2 c= 3 args= () kw= {}
# hello(1,2,3,4) #--a= 1 b= 2 c= 3 args= (4,) kw= {}
# hello(1,2,3,4,5) #--a= 1 b= 2 c= 3 args= (4, 5) kw= {}
# hello(1,2,3,4,5,6=6) #--error
# hello(1,2,3,4,5,a6=6) #--a= 1 b= 2 c= 3 args= (4, 5) kw= {'a6': 6}
# hello(1,2,3,4,5,6a=6) #--error
# hello(1,2,a6=6,) #--a= 1 b= 2 c= 0 args= () kw= {'a6': 6}

# hello1(1,2,3,4) #--error
# hello1(1,2,3,d=4) #--a= 1 b= 2 c= 3 d= 4 kw= {}
# hello1(1,2,3,d=4,d=5) #--error
# hello1(1,2,3,d=4,d1=5,d2=4) #--a= 1 b= 2 c= 3 d= 4 kw= {'d1': 5, 'd2': 4}

# def print_scores(**kw):
#     print('      Name  Score')
#     print('------------------')
#     for name, score in kw.items():
#         print('%10s  %d' % (name, score))
#     print()

# print_scores(Adam=99, Lisa=88, Bart=77)
# data = {
#     'Adam Lee': 99,
#     'Lisa S': 88,
#     'F.Bart': 77
# }

# print_scores(**data)

# def print_info(name, *, gender, city='Beijing', age):
#     print('Personal Info')
#     print('---------------')
#     print('   Name: %s' % name)
#     print(' Gender: %s' % gender)
#     print('   City: %s' % city)
#     print('    Age: %s' % age)
#     print()

# print_info('Bob', gender='male', age=20)
# print_info('Lisa', gender='female', city='Shanghai', age=18)


# 递归函数
# n的阶乘(方法的递归会导致栈溢出问题)
# def fact(n):
#     if n == 1:
#         return 1
#     return n*fact(n-1)

# print(fact(3))

# n的阶乘(尾递归的方式)(尾递归方式不会导致栈溢出，可惜没有语言对其做优化)
# def fact(n):
#     return fact_iter(n,1)
# def fact_iter(n,product):
#     if n==1:
#         return product
#     return fact_iter(n-1,n*product)
# print(fact(3))

# 汉诺塔（递归实现）
# def move(n, a, b, c):
#     if n == 1:
#         print('move', a, '-->', c)
#     else:
#         move(n-1, a, c, b)
#         move(1, a, b, c)
#         move(n-1, b, a, c)
# move(4, 'A', 'B', 'C')


# 高级特性
# 循环构造一个1到99的奇数数组
# l = []
# x = 1
# while x < 100:
#     l.append(x)
#     x = x+2
# print(l)

# 取前一半元素
# x = 0
# len=len(l)/2
# while x<len:
#     print(l[x])
#     x=x+1

# n = 25
# for x in range(n):
#     print(l[x])

# 切片Slice[x:y]
# print(l[0:25])    #从第0个到第24个
# print(l[:25])     #从第0个到第24个
# print(l[1:25])    #从第1个到第24个
# print(l[-2:])     #从倒数第二个到倒数第一个
# print(l[-2:55])   #结果同上
# print(l[0:25:2])  #从第0个到第24个，每隔2个数取一个
# l1=l[:]
# print(l1)         #等同于复制了一个list

# 切片不仅适用于list还适用于tuple，不过对tuple切片后得到的还是tuple，同样的道理适用于String


# 迭代 python的for比java抽象的多，python可迭代无下标的数据类型
# d = {'a':1,'b':2,'c':3}
# for x in d:             #只能迭代key
#     print(x)
# for x in d.values():    #只能迭代value
#     print(x)
# for x,y in d.items():   #可以迭代key和value
#     print(x,":",y)

# 迭代字符串
# str = "ABCDE"
# for x in str :      #遍历字符串的每一个字符
#     print(x)

# 判断是否可以进行迭代
# from collections import Iterable
# print(isinstance("123",Iterable))   #true
# print(isinstance(123,Iterable))     #false


# 生成器 generator 返回的是一个generator对象
# # list
# l = [x*x for x in range(10)]
# print(l)
# # generator
# g =(x*x for x in range(10))
# print(g)
# for x in g:
#     print(x)

# 非波拉契数列 1，1，2，3，5，8，13。。。
# def fib(max):
#     n,a,b=0,0,1
#     while n<max:
#         print(b)
#         a,b=b,a+b
#         n=n+1
#     return "done"
# fib(6)

# generator
# def fib(max):
#     n,a,b=0,0,1
#     while n<max:
#         yield b
#         a,b=b,a+b
#         n=n+1
#     return "done"
# print(fib(6))

# def odd():
#     print("s 1")
#     yield 1
#     print("s 2")
#     yield 3
#     print("s 3")
#     yield 5
# o=odd()
# next(o)
# next(o)
# next(o)

# def spam():
#     print(1)
#     yield"first"
#     print(2)
#     yield"second"
#     print(3)
#     yield"third"


# # print(spam) #<function spam at 0x00D48B28>
# # for x in spam():
# #         print(x)
# gen = spam()
# # print(gen) # <generator object spam at 0x02860090>
# next(gen) # 1
# next(gen) # 2
# next(gen) # 3


# 杨辉三角
# def triangles():
#     t =[1]
#     while True:
#         yield t
#         t.append(0)
#         t=[t[x-1]+t[x] for x in range(len(t))]

# n=0
# for x in triangles():
#     print(x)
#     n=n+1
#     if n==10:
#         break

# 迭代器
# isinstance判断是否是可迭代对象

# Iterable:可以直接作用于for循环的对象统称为可迭代对象
from collections import Iterable
# print(isinstance((x for x in range(10)),Iterable))  #True
# print(isinstance("123",Iterable))   #true
# print(isinstance(123,Iterable))     #False

# Iterator:可以被next()函数调用并不断返回下一个值的对象称为迭代器
from collections import Iterator
# print(isinstance((x for x in range(10)),Iterator))  #True
# print(isinstance([1, 2, 3], Iterator))  #False
# print(isinstance({"a": 1, "b": 2}, Iterator))   #False
# print(isinstance("abc",Iterator))   #False
# print(isinstance(123,Iterator)) #False

# print(isinstance(iter([1, 2, 3]), Iterator))  #True
# print(isinstance(iter({"a": 1, "b": 2}), Iterator))   #True
# print(isinstance(iter("abc"),Iterator))   #True

# 凡是可以for循环的对象都是iterbale类型，凡是可以用于next()函数的对象都是iterator类型，表示一个惰性计算序列
# dict,list,string 是Iterable 但不是 Iterator,不过可以用 iter() 函数 从iterable 对象获得一个Iterator 对象

# d={"a":"aa","b":"bb"}
# d1={"1":"11","2":"22"}
# nd = iter(d) #nd = iter(d.values()) #nd = iter(d.items())
# while True:
#     try:
#         print(next(nd))
#     except StopIteration:
#         print("StopIteration!")
#         break


# 高阶函数  变量也可以指向函数，而指向函数的变量可以作为参数传入另外一个函数,这样的函数称为高阶函数，不带括号的函数名也是一个变量，也可指向其他地址
# f=abs
# def add(x,y,abs):
#     print(abs(x)+abs(y))
# add(-1,1,f)
# abs=1
# print(f(-10))
# print(abs(-10))


# map map函数接收两个参数，第一个是函数，第二个是Iterable(可以被for循环迭代的),把结果作为一个新的Iterator返回
# def double(x):
#     return x*x

# d = {"a": 1, "b": 2}
# d1 = [1, 2, 3, 4, 5]
# f = map(double, d1)
# t = tuple(f)
# l = list(f)
# print(l)
# print(t)

# for x in f:
#     print(x)

# while True:
#     try:
#         print(next(f))
#     except StopIteration:
#         print("StopIteration")
#         break


# reduce函数也是接收两个参数，将第二个参数(序列)里的每个元素都传递给第一个参数(函数)
# from functools import reduce
# def add(x, y):
#     return x+y

# print(reduce(add, [1, 2, 3, 4]))


# 格式化人名
# from functools import reduce
# def normalize(name):
#     name1=name.lower()
#     name=name[0].upper()
#     return name+name1[1:len(name1)]

# def normalize1(name):
#     return name.capitalize()
# l1=["adam","LISA","badrT"]
# print(list(map(normalize,l1)))
# print(list(map(normalize1,l1)))

# reduce()求积
# from  functools  import reduce
# def prod(l):
#    return reduce(lambda x,y:x*y,l)
# print("1*2*3=",prod([1,2,3]))

# str2float() 将'12.34“转为浮点数12.34
# from functools import reduce
# CHAR_TO_FLOAT = {
#     "0": 0,
#     "1": 1,
#     "2": 2,
#     "3": 3,
#     "4": 4,
#     "5": 5,
#     "6": 6,
#     "7": 7,
#     "8": 8,
#     "9": 9,
#     ".": -1
# }

# def str2float(s):
#     nums = map(lambda ch: CHAR_TO_FLOAT[ch], s)
#     # [1,2,-1,3,4]
#     point = 0
#     def tofloat(res, num):
#         nonlocal point
#         if num==-1:
#             point=1;return res
#         if point==0:
#            return res*10+num
#         else:
#             point=point*10
#             return res+num/point
#     return reduce(tofloat,nums)
# print("str2float(\'12.34\')=", str2float("12.34"))
# print(float("123.456")*2)
# print("123.456"*2)


# sorted排序函数
# s = ["bsfds","acvasdf","Adsf"]
# print(sorted(s,key=str.lower,reverse=True))

# 对学生信息进行排序
# L = [("Bob", 75), ("Adam", 90), ("Bart", 60), ("Lisa", 100)]
# def by_name(n):
#     return n[0]
# print(sorted(L,key=by_name))
# def by_score(n):
#     return n[1]
# print(sorted(L,key=by_score,reverse=True))


# 返回函数  高级函数不仅可以将函数作为参数，还可以将函数作为返回值，这种程序称之为 闭包
# 原函数的相关变量和参数都封装子闭包里，当返回函数被调用的时候才执行，而且每次返回的函数都是一个新函数
# def lazy_sum(*args):
#     def sum():
#         ax=0
#         for n in args:
#             ax=ax+n
#         return ax
#     return sum

# f1=lazy_sum(1,2,3,4)
# f2=lazy_sum(1,2,3,4)
# f1()
# f2()
# print(f1==f2)  #False

#　返回闭包时不要引用任何循环变量，或者后续会发生变化的变量
# def count():
#     fs = []
#     for i in range(1, 4):
#         def f(i):
#             def g():
#                 return i*i
#             return g
#         fs.append(f(i))
#     return fs

# f1, f2, f3 = count()
# print(f1())
# print(f2())
# print(f3())

# 装饰器
# def now():
#     print("2018-3-19")
# f=now
# f()
# print(now.__name__)
# print(f.__name__)

import functools

# def log(func):
#     @functools.wraps(func)
#     def wrapper(*args, **kw):
#         print('call %s():' % func.__name__)
#         return func(*args, **kw)
#     return wrapper

# # @log
# def now():
#     print('2015-3-25')
# now = log(now)
# now()

# print("\n")

# def logger(*argss):
#     def decorator(func):
#         # @functools.wraps(func)
#         def wrapper(*args, **kw):
#             print('%s %s!' % ((func.__name__),"".join(argss[0])))
#             f=func(*args, **kw)
#             if len(argss)>1:
#                 print("%s"%(''.join(argss[1])))
#             return f
#         return wrapper
#     return decorator

# @logger('begin','end')
# # @logger('begin')
# def today():
#     print('2015-3-25')

# today()
# print(today.__name__)


# 偏函数:当函数参数过多时，需要用来简化 可以使用偏函数来固定住部分参数
# def int2(x,base=2):
#     return int(x,base)
# t=int2("100")
# print(t)

# import functools
# int2 = functools.partial(int, base=2)
# i = int2("1000")
# j=int2("1000",base=10)
# print(i)
# print(j)


# 模块
# def test():
#     print("1")
# if __name__=="_main_":
#     test()

# Pillow(图片必须在python文件夹)
# import sys
# sys.path.append("C:\\Users\\Administrator\\Desktop")
# print(sys.path)

# from PIL import Image
# im = Image.open("test.png")
# print(im.format,im.size,im.mode)
# im.thumbnail((200,100))
# im.save("test1.jpg","JPEG")


# 类和实例
# class Sutdent(object):
#     def __init__(self,name,score):
#         self.name=name
#         self.score=score
#     def pritn_score(self):
#         print("%s:%s"%(self.name,self.score))
#         return ""
#     def get_grade(self):
#         if self.score>=90:
#             return "A"
#         elif self.score>=60:
#             return "B"
#         else:
#             return "C"

# s=Sutdent("lwj",90)
# t=Sutdent("lws",60)
# print(s.pritn_score())
# print(s.get_grade())
# s.age=20
# print(s.age)


# 获取对象信息
# print(type(123))
# import types  # types中定义了常量
# print(type(abs) == types.FunctionType)
# print(type(abs) == types.BuiltinFunctionType)

# class Animal(object):
#     def run(self):
#         print("Animal is running")

# class Dog(Animal):
#     def run(self):
#         print("Dog is running")

# Animal().run()
# Dog().run()

# def runt(object):
#     object.run()

# runt(Animal())
# runt(Dog())

# class RunTest:
#     def run(self):
#         print("this is runTest function running")
# runt(RunTest())
# # 类型判断
# print(isinstance(Dog(),Animal))
# print(isinstance("123",int))
# print(isinstance(123,int))
# print(isinstance("123",(int,str))) #判断是否是某些变量中的一种，
# print(isinstance([1,2,3],(list,tuple)))

# 使用dir()  获得一个对象的所有属性和方法
# print(dir("asd"))
# print('asd'.__len__())  #注意前后各是两个下划线
# print(len("asd"))

# class myLen(object):
#     def __len__(self):
#         return 100
# print(len(myLen()))

# setattr() getattr() hasattr()
# class Test():
#     x=5
# t=Test()
# print(t.x)
# print(hasattr(t,"x"))
# print(hasattr(t,"x"))
# print(hasattr(t,"y"))
# print(getattr(t,'y',404))
# setattr(t,'y',6)
# print(hasattr(t,"y"))
# print(getattr(t,'y',404))


#实例属性和类属性   
# class Student():
#     name='student'
# print(Student.name)   #student
# s=Student()
# s.name="lwj"
# print(s.name)   #lwj    实例属性优先级比类属性高 所以打印的是lwj 但类属性并没有消失
# print(Student.name)   #student
# del s.name     #删除类属性的实例
# print(s.name)   #student
# print(Student.name)   #student


# 对实例或类添加方法或属性 
# class Student():
#     pass
# from types import MethodType
# def set_age(self,age):
#     self.age=age
#     print(self.age)
# s=Student()
# print(dir(s))
# s.set_age=MethodType(set_age,s)  #返回一个方法并指向实例s的set_age属性
# print(dir(s))

# Student.set_age=MethodType(set_age,Student)
# s1=Student()
# print(dir(s1))


# 使用__slots__ 限制允许绑定的属性名称  注意是 两个下划线 __slots__只能作用于当前类，对其子类不起作用 __slots__可以被子类继承
# class S():       #不写的继承类的话默认继承Object
#     __slots__=('name','age')
# s=S()
# s.age=20
# s.name='wangya'
# print("%s is %s years old"%(s.name,s.age))
# s.a=1   #会报 AttributeError: 'S' object has no attribute 'a'



#@property 相当于把set,get方法直接绑定在属性上，通过对属性的操作来调用方法，可以少写方法名
# class Student():
#     @property
#     def age(self):
#         return self._age    ##  注意：是  _age  不是 age  如果误写为age会报递归错误

#     @age.setter
#     def age(self,age):
#         self._age=age     

# s=Student()
# s.age=1
# print(s.age)

# class Student():
#     @property
#     def width(self):
#         return self._width
#     @width.setter
#     def width(self,value):
#         self._width=value
#     @property
#     def height(self):
#         return self._height
#     @height.setter
#     def height(self,value):
#         self._height=value
#     @property
#     def resolution(self):
#         return 10
# s=Student()
# s.width=100
# s.height=50
# print(s.height,s.width)
# print(s.resolution)
# s.resolution=11     #AttributeError: can't set attribute


# 定制类
# __str__
# class Student:
#     def __init__(self,name):
#         self.name=name
#     def __str__(self):
#         return "Student Object name:%s"%self.name
# s=Student("jack")
# print(s)

# __iter__
# class Fb:
#     def __init__(self,num):
#         self.a=num
#         self.n=0
#         self.l=len(num)-1
#     def __iter__(self):
#         return self
#     def __next__(self):
#         if self.n>self.l:
#             raise StopIteration()
#         self.n=self.n+1
#         return self.a[self.n-1]

# for n in Fb(range(10)):
#     print(n)
# print()
# for a in range(10):
#     print(a)
# print(isinstance(Fb(range(10)),Iterable))   #true
# print(isinstance(Fb(range(10)),Iterator))   #true

# 使用元类