# 声明全局变量：函数体内外都能访问
a = 100

print(a)


def testA():
    print(a)


def testB():
    print(a)


testA()
testB()
