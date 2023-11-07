from util import times

"""
python中的装饰器,类似于aop，可以将增强函数使用’@‘标注在原函数，来在不改动原函数的基础上，在原函数前后增加处理，比如校验和日志
两层嵌套，装饰器上没有入参
"""


def log(func):
    def wrapper(*args, **kwargs):
        print(f'start {times.cur_ft_time()}')
        res = func(*args, **kwargs)
        print(res)
        print(f'end {times.cur_ft_time()}')
        return res

    return wrapper


def eat(name):
    return f'{name} eating'


log(eat)('lwj', a='a')


# eat('lwj', a='a')

def logs(author):
    print(author)

    def decorator(func):
        def wrapper(*args, **kwargs):
            return func(*args, **kwargs)

        return wrapper

    return decorator


print(logs('test1')(eat)('lwj'))


@logs('test2')
def drink(name):
    return f'{name} drink'


print(drink('lwj'))
