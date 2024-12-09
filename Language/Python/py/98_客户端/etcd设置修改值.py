import etcd3

client = etcd3.client()
client.put('test', "1")  # 添加数据
value, kv_meta = client.get('test')  # 读取数据，返回的数据value 是 bytes 型数据
print(value, kv_meta)

client.put('test', "2")
value, kv_meta = client.get('test')  # 读取数据，返回的数据value 是 bytes 型数据
print(value, kv_meta)
