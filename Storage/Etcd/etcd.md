**etcd** 用于分布式系统最关键数据的分布式、可靠的键值储存。

etcd 本来就是为了分布式系统而生的，它专注于键值储存。初看起来，相当于只是 Redis 的字符串功能，但却比 Redis 的字符串更为强大。

## Watch机制

我们可以监控 etcd 中的一个键，当它发生变化的时候，就调用我们提前定义好的函数。

```python
#设置变更值
import etcd3

client = etcd3.client()
client.put('test', "1")  # 添加数据
value, kv_meta = client.get('test')  # 读取数据，返回的数据value 是 bytes 型数据
print(value, kv_meta)

client.put('test', "2")
value, kv_meta = client.get('test')  # 读取数据，返回的数据value 是 bytes 型数据
print(value, kv_meta)
```

```python
#监听值的变化
import time
import etcd3


def callback(response):
		# 短时间变化多次，events会返回多个，events[-1]最后一个是最新的变更值 
    for event in response.events:
        print(f'Key: {event.key}发生改变！新的值是：{event.value}')


client = etcd3.client()
client.add_watch_callback('test', callback)

for i in range(100):
    print(i)
    time.sleep(1)
```