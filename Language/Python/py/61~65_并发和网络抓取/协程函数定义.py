import asyncio


# 1. 普通函数 FunctionType
def function():
    return 1


# 2. generator function ：GeneratorType 生成器 用到了yield的函数就是生成器
def generator():
    yield 2  # 生成器


# 3. 异步（协程）函数 ：CoroutineType  async修饰将普通函数和生成器函数包装成异步(协程)函数和异步生成器，协程函数返回的对象叫做协程对象。
async def async_function():
    return 3


# 4. 异步生成器 ： AsyncGeneratorType 异步生成器
async def async_function_async():
    yield 4


# print(type(function()))
# print(type(generator()))
# print(type(async_function()))
# print(type(async_function_async()))
"""
output
<class 'int'>
<class 'generator'>
<class 'coroutine'>
<class 'async_generator'>
"""


# await只能出现在通过async修饰的函数中；其后是一个awaitable（可等待对象）。
# awaitable有三种主要类型: 协程, 任务 和 Future(这里的Future和 concurrent.futures.Future 不是一个类).
async def async_function_2():
    return 1


async def await_coroutine_2():
    result = await async_function_2()
    print(result)


asyncio.run(await_coroutine_2())

# awaitable类
# 实现了__await__方法的类。

# AsyncGenerator
# 需要实现__aiter__、__anext__两个核心方法，以及asend、athro、aclose方法。

# async/await语法
"""
 完成异步的代码不一定要用async/await；
使用了async/await的代码也不一定能做到异步；
async/await 是协程的语法糖，使协程之间的调用变得更加清晰：
    使用async修饰的函数调用时会返回一个协程对象；
    await只能放在async修饰的函数里面使用；
    await后面必须要跟着一个协程对象或者Awaitable；
    await的目的是等待协程控制流的返回；
    实现暂停并挂起函数的操作的是yield；
"""

# async作用
"""
常规函数开始执行后一直运行到return实现退出，如果需要能够中断的函数，就需要添加async关键字。
async用来声明一个函数为异步函数，异步函数的特点就是能在函数执行过程中被挂起，去执行其他异步函数，等挂起条件消失后再回来执行。
"""

# await作用
"""
await用来声明程序挂起。await后面只能跟异步程序或有__await__属性的对象。
两个异步程序async a、async b：
a中一步有await，当程序碰到关键字await b后；
异步程序a挂起，去执行异步b程序（就相当于从一个函数内部跳出去执行其他函数）；
当挂起条件的时候，不管b是否执行完，要马上从b程序中跳出来，回到原程序a执行原来的操作；
如果await后面跟的b函数不是异步函数，那么操作就只能等b执行完再返回，无法在b执行的过程中返回，这样就相当于直接调用b函数，没必要使用await关键字了。
因此，需要await后面跟的是异步函数
"""
