# 函数是一段封装的代码单元，但方法是class的成员，函数定义在类中，被称为方法

# @dataclass 装饰器 类似于java的@data注解
from dataclasses import dataclass


@dataclass
class Person:
    age: int
    name: str


print(Person(1, "a"))
