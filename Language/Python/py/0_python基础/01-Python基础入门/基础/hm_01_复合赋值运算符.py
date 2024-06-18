a = 10
a += 1  # a = a + 1
print(a)

b = 10
b -= 1  # b = b - 1
print(b)


# 注意： 先算复合赋值运算符右面的表达式； 算复合赋值运算
c = 10
# c = 10 + 1 + 2
# c += 3 -- c = c + 3
c += 1 + 2
print(c)

d = 10
d *= 1 + 2
print(d)


