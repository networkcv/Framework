# 协程
def consumer():
    r = ''
    while True:
        n = yield r
        if not n:
            return
        print('[CONSUMER] Consuming %s...' % n)
        r = '200 OK'


def produce(c):
    # c.next() 和 c.send(None)  功能类似都可以启动一个generator，但启动时send方法不能传入非None的值
    # tc=c.send(1) error
    tc = c.send(None)
    print(f'tc:{tc}')
    n = 0
    while n < 5:
        n = n + 1
        print('[PRODUCER] Producing %s...' % n)
        r = c.send(n)
        print('[PRODUCER] Consumer return: %s' % r)
    c.close()


c = consumer()
produce(c)

"""
重点是n = yield r
分成两部分：
yield r 是将r 返回给外部调用程序，交出控制权，暂停；
n = yield 可以接收外部程序通过send()发送的信息，并赋值给n
"""
