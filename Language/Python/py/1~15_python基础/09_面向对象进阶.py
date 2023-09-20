# @dataclass 装饰器 类似于java的@data注解
from dataclasses import dataclass


class Person3:
    # 限定当前类中的绑定属性，对子类不生效
    # __slots__ = ('dynamic_var', 'dynamic_var2')

    # age = None
    phone = None

    #
    def __init__(self, id, name, age, phone):
        self.__id = id
        self._name = name
        self._age = age
        self._phone = phone

    def name(self):
        return self._name

    def phone3(self):
        return self._phone

    # 访问器 - getter方法
    @property
    def phone4(self):
        return self._phone

    # 修改器 - setter方法
    @phone4.setter
    def phone4(self, phone4):
        self._phone = phone4


person3 = Person3(1, "bbb", 19, 152)
# print(person3.__id)  # ERROR 'Person3' object has no attribute '__id' 双下划线在python语法保证受保护
print(person3._Person3__id)  # 虽然__开头受语法保护，但是我们还是能通过拼接规则绕过这个语法，但是不建议
print(person3._name)  # warn Access to a protected member _name of a class  访问了一个受保护的成员，因为单下划线下标开头约定为受保护
print(f'phone:{person3.phone}')
# print(f'phone2:{person3.phone2}') # 没有定义 phone2变量，所以无法通过 对象.成员变量 的方式去访问
print(f'phone3:{person3.phone3}')
# phone3:<bound method Person3.phone3 of <__main__.Person3 object at 0x1032ccd90>> 虽然也没有定义phone3变量，但是定义了phone3方法
print(f'phone3:{person3.phone3()}')  # 这里才真正打印出了 phone3的返回值123，而不是方法定义
print(f'phone4:{person3.phone4}')  # 类中定义的phone4虽然也是方法，但是可以通过 对象.成员变量 访问的原因是，方法上加了@property
person3.phone4 = 123123  # new phone4:123123 因为加了 @phone4.setter 装饰器，所以phone4可以像成员变量一样访问
print(f'new phone4:{person3.phone4}')


class StaticMethodClass:

    @staticmethod
    def static_fun(a, b, c):
        return a, b, c

    @classmethod
    def class_fun(cls, a):
        return cls, a


# 通过类名调用静态方法
print(StaticMethodClass.static_fun(1, 2, 3))
# 通过类名调用类方法
print(StaticMethodClass.class_fun(1))


# 类型注解 语法 变量:类型，类型java中的泛型
@dataclass
class Person4:
    name: str
    age: int


var1: int = 1
var2: str = '2'

person4_1: Person4 = Person4("aaa", 18)
print(person4_1)

# 基础容器类型注解
my_1ist: list = [1, 2, 3]
my_tuple: tuple = (1, 2, 3)
my_set: set = {1, 2, 3}
my_dict: dict = {"itheima": 666}
my_str: str = "itheima"

# 容器详细类型注解
my_1ist: list[int] = [1, 2, 3]
my_tuple: tuple[int, int, int] = (1, 2, 3)  # 元组需要将每个类型都标注出来
my_set: set[int] = {1, 2, 3}
my_dict: dict[str, int] = {"itheima": 666}  # 字典则需要将key value类型标注出来
