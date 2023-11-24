# 函数是一段封装的代码单元，但方法是class的成员，函数定义在类中，被称为方法
from abc import ABCMeta, abstractmethod


class Person:
    # 访问成员变量需要用self访问，可以在方法里定义成员变量,但是子类访问不到
    age = None
    public_var = None

    # 两个下划线开头的才是私有成员变量,私有成员方法同理,当在类上下文中使用时，触发"名称修饰"。由Python解释器强制执行。
    __private_var2 = 2

    # 不是私有的成员变量,表示Python语言定义的特殊方法,比如main方法 避免在你自己的属性中使用这种命名方案。
    __private_var1__ = 1

    # 单下划线开头，是一种命名习惯，表示外部代码访问该属性时，需要保持慎重，这种做法不是语法上的规则
    _age = None

    # 单下划线结尾 按约定使用以避免与Python关键字的命名冲突。
    class_ = None

    # Python 并不像其他一些编程语言那样提供内置的构造方法重载机制。Python 类只能有一个特殊的构造方法，即 __init__ 方法，用于初始化对象的属性。
    # 如果希望实现多个构造方法的功能，可以使用一些技巧和约定来达到相似的效果。下面介绍两种常见的方法：
    def __init__(self, name, age):
        self.name = name
        self.age = age

    # 方法一：使用类方法（Class Method）：可以定义一个类方法作为额外的构造方法。类方法通过 @classmethod 装饰器进行标记，
    # 通常命名为 from_*，其中 * 表示不同的参数组合。这样可以根据不同的参数类型或数量来创建对象。
    @classmethod
    def from_data(cls, age):
        return

    # 方法二：使用关键字参数（Keyword Arguments）：在 __init__ 方法中可以使用默认参数或可选参数，从而允许在创建对象时使用不同的参数组合。
    # 通过传递具有特定名称的参数，可以实现类似于构造方法重载的效果。
    # def __init__(self, name, age=None):
    #     self.name = name
    #     self.age = age

    # python内置方法
    def __str__(self):
        return f'{self.name}:{self.age}'

    def __lt__(self, other):
        return self.age < other.age

    def __le__(self, other):
        return self.age <= other.age

    def __eq__(self, other):
        return self.name == other.name and self.age == other.age


print(Person('lwj', 20))  # lwj:20
print(Person('lwj', 20) < Person('lws', 12))  # False
print(Person('lwj', 20) <= Person('lws', 20))  # Ture
print(Person('lwj', 20) == Person('lws', 20))  # False
print(Person('lwj', 20) == Person('lwj', 20))  # Ture

print("==")


# print(Person('wb',1).__private_var1__)  # 1
# print(Person('wb',1).__private_var2)  # object has no attribute '__private_var2'. Did you mean: '__private_var1__'?


class Profession(metaclass=ABCMeta):
    # 表示跳过语法检验
    pass

    @abstractmethod
    def abstract_fun(self):
        """
        注释
        :return:
        """
        pass


# 表示Student 继承了 Person 和 Profession
# 如果 Person 和 Profession 两者中存在相同的成员变量或成员方法，优先取前者
class Student(Person, Profession):
    """
    如果需要使用被复写的父类的成员，需要特殊的调用方式：
    方式1：
    调用父类成员
    使用成员变量：父类名.成员变量
    使用成员方法：父类名.成员方法(self)

    方式2：
    使用super()调用父类成员
    使用成员变量：super().成员变量
    使用成员方法：super().成员方法(）
    """

    age = None
    name = None

    def __init__(self, name, age):
        print("子类构造方法")
        super().__init__(name, age)

    def fun1(self):
        print(f'Person 通过父类名称调用:{Person.age}')
        print(f'Person 通过父类名称调用:{Person._age}')
        print(f'Person 通过 super()方法调用:{super().age}')
        print(f'Student self.age {self.age}')
        print(f'Student self.name {self.name}')
        return 1

    # 这里实现了父类Profession中的抽象方法abstract_fun
    def abstract_fun(self):
        pass


student = Student('lwj', 18)
print(student)
print(student.fun1())
