def decorator(fn, flag):
    def inner(num1, num2):
        if flag == "+":
            print("--正在努力加法计算--")
        elif flag == "-":
            print("--正在努力减法计算--")
        result = fn(num1, num2)
        return result

    return inner


@decorator('+')
def add(a, b):
    result = a + b
    return result


result = add(1, 3)
print(result)
