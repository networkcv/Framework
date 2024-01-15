import asyncio


async def main():
    await asyncio.sleep(1)
    print(1)


asyncio.run(main())
