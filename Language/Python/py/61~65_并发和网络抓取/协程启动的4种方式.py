import asyncio
import time

from util import times


async def main():
    print('Hello ...')
    await asyncio.sleep(1)
    print('... World!')


print(f"{times.cur_ft_time()}")
asyncio.run(main())
print(f"{times.cur_ft_time()}")

# 运行一个协程的几种机制
# 1.asyncio.run() 运行
asyncio.run(main())


# 2.等待一个协程的运行
async def say_after(delay, what):
    await asyncio.sleep(delay)
    print(what)


async def main():
    print(f"started at {time.strftime('%X')}")

    await say_after(1, 'hello')
    await say_after(2, 'world')

    print(f"finished at {time.strftime('%X')}")


asyncio.run(main())


# 3.asyncio.create_task() 函数用来并发运行作为 asyncio 任务 的多个协程。
async def main():
    task1 = asyncio.create_task(
        say_after(1, 'hello'))

    task2 = asyncio.create_task(
        say_after(2, 'world'))

    print(f"started at {time.strftime('%X')}")

    # Wait until both tasks are completed (should take around 2 seconds.)
    await task1
    await task2

    print(f"finished at {time.strftime('%X')}")


asyncio.run(main())


# 4.asyncio.TaskGroup 类提供了 create_task() 的更现代化的替代。 使用此 API，之前的例子将变为:
async def main():
    async with asyncio.TaskGroup() as tg:
        task1 = tg.create_task(
            say_after(1, 'hello'))

        task2 = tg.create_task(
            say_after(2, 'world'))

        print(f"started at {time.strftime('%X')}")

    # The await is implicit when the context manager exits.

    print(f"finished at {time.strftime('%X')}")
