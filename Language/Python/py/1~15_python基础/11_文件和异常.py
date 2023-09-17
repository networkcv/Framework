import json

"""
# 文件
通过Python内置的`open`函数，我们可以指定文件名、操作模式、编码信息等来获得操作文件的对象，接下来就可以对文件进行读写操作了。这里所说的操作模式是指要打开什么样的文件（字符文件还是二进制文件）以及做什么样的操作（读、写还是追加），具体的如下表所示。

| 操作模式 | 具体含义                         |
| -------- | -------------------------------- |
| `'r'`    | 读取 （默认）                      |
| `'w'`    | 写入（会先截断之前的内容）           |
| `'x'`    | 写入，如果文件已经存在会产生异常      |
| `'a'`    | 追加，将内容写入到已有文件的末尾      |
| `'b'`    | 二进制模式                        |
| `'t'`    | 文本模式（默认）                   |
| `'+'`    | 更新（既可以读又可以写）            |

"""

f = open("_011/a.txt")
# 对于同一个文件的读取，会记录上次读取的位置
print(f.read().count('aa'))

# f = open("_011/a.txt", 'w')
# f = open("_011/a.txt", 'w', encoding='utf-8')
# print(f.readline())

f = open("_011/a.txt")
# 读取所有文件内容到列表里，会把换行符号读取到
print(f.readlines())

f = open("_011/b.txt", "w")
f.write("a")
f.flush()

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
    with open('_011/car2.jpg', 'rb') as fr:
        data = fr.read()
        # print(data)
    with open('_011/car2.jpg', 'wb') as fw:
        fw.write(data)
except FileNotFoundError as e:

    print("[waring] file not found")


# 手动抛出异常
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
    with open('_011/data.json', 'w', encoding='utf-8') as fs:
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
