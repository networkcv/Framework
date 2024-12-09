import time

import etcd3


def callback(response):
    for event in response.events:
        print(f'Key: {event.key}发生改变！新的值是：{event.value}')


client = etcd3.client()
client.add_watch_callback('test', callback)

for i in range(100):
    print(i)
    time.sleep(1)
