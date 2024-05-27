import json

"""
# 异常
如果读取一个不存在的文件，则会抛出异常,不做处理的话程序会终止，但我们也可以进行提前做一些异常处理，来保证程序顺利执行
异常处理总结 "https://segmentfault.com/a/1190000007736783"
下边的实例中我们手动关闭了文件IO，看起来不够优雅
"""
f1 = None
try:
    f1 = open('a.txt')
except FileNotFoundError as e:
    print(f'exception{e}')
    print("[waring] file not found")
finally:
    if f1:
        f1.close()

# 以二进制模式读取图片，并复制
try:
    with open('../06-文件操作/resource/car2.jpg', 'rb') as fr:
        data = fr.read()
        # print(data)
    with open('../06-文件操作/resource/car2.jpg', 'wb') as fw:
        fw.write(data)
except FileNotFoundError as e:

    print("[waring] file not found")


# 手动抛出异常 和java的 throw 关键字类似
def test_throw(raise_flag):
    if raise_flag:
        raise CustomBizException("手动抛出异常")


# 自定义异常
class CustomBizException(RuntimeError):
    pass


# test_throw(True)


"""
# Json
json模块主要有四个比较重要的函数，分别是：

- `dump` - 将Python对象按照JSON格式序列化到文件中
- `dumps` - 将Python对象处理成JSON格式的字符串
- `load` - 将文件中的JSON数据反序列化成对象
- `loads` - 将字符串的内容反序列化成Python对象
"""

mydict = {
    'name': '骆昊',
    'age': 38,
    'qq': 957658,
    'friends': ['王大锤', '白元芳'],
    'cars': [
        {'brand': 'BYD', 'max_speed': 180},
        {'brand': 'Audi', 'max_speed': 280},
        {'brand': 'Benz', 'max_speed': 320}
    ]
}
try:
    with open('../06-文件操作/resource/data.json', 'w', encoding='utf-8') as fs:
        #  ensure_ascii=False，默认是true这个参数会让生成的文件中的汉字不以ascii码展示，不然中文会展示为 \u767d\u5143\u82b3 这样的utf-8编码方式
        # json.dump(mydict, fs)
        json.dump(mydict, fs, ensure_ascii=False)
        dict_json_str = json.dumps(mydict, ensure_ascii=False)
        print(f'type:{type(dict_json_str)} data:{dict_json_str} ')
        print(f'type:{type(json.loads(dict_json_str))} data:{json.loads(dict_json_str)} ')
except IOError as e:
    print(e)
print('保存数据完成!')
# dump完的字符串都会转成utf-8的格式来保存
# print(open('data.json').read())
