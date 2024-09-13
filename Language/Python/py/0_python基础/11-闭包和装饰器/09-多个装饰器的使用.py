def make_div(func):
    print("make_div装饰器执行了")

    def inner():
        # 在内部函数对已有函数进行装饰
        result = "<div>" + func() + "</div>"
        return result

    return inner


# 定义装饰器
def make_p(func):
    print("make_p装饰器执行了")

    def inner():
        # 在内部函数对已有函数进行装饰
        result = "<p>" + func() + "</p>"
        return result

    return inner


# 多个装饰器的装饰过程: 由内到外的一个装饰过程，先执行内部的装饰器，在执行外部的装饰器
# 原理剖析: content = make_div(make_p(content))
# 分步拆解: content = make_p(content),内部装饰器装完成content=make_p.inner
#  content = make_div(make_p.inner)
@make_div
@make_p
def content():
    return "人生苦短，我用python!"


# result = content()
# print(result)

# 定义装饰器
def make_a(func):
    print("make_a装饰器执行了")

    def inner():
        # 在内部函数对已有函数进行装饰
        result = "<a>" + func() + "</a>"
        return result

    return inner


# <p>人生苦短，我用python!</p>

content = make_a(content)  # make_a(make_div(make_p(content)))
print(content())
