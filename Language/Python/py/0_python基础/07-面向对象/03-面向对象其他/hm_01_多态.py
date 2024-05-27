# 需求：警务人员和警犬一起工作，警犬分2种：追击敌人和追查毒品，携带不同的警犬，执行不同的工作


# 1. 定义父类，提供公共方法： 警犬 和 人
class Dog(object):
    def work(self):
        pass


# 2. 定义子类，子类重写父类方法：定义2个类表示不同的警犬
class ArmyDog(Dog):
    def work(self):
        print('追击敌人...')


class DrugDog(Dog):
    def work(self):
        print('追查毒品...')


# 定义人类
class Person(object):
    def work_with_dog(self, dog):
        dog.work()


# 3. 创建对象，调用不同的功能，传入不同的对象，观察执行的结果
ad = ArmyDog()
dd = DrugDog()

daqiu = Person()
daqiu.work_with_dog(ad)
daqiu.work_with_dog(dd)




