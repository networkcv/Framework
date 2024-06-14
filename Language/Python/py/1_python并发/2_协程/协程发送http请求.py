import asyncio

import aiohttp
from superstream import Stream

from util import times


async def call(url):
    async with aiohttp.ClientSession() as aio_session:
        async with aio_session.post('http://rock.paas.cai-inc.com/conf/dubboTest') as res:
            return await res.text()


async def main():
    print(times.cur_ft_time())
    l = await asyncio.gather(*Stream.of(*range(1, 10000)).map(call).to_list())
    print(l)
    print(times.cur_ft_time())


asyncio.run(main())
