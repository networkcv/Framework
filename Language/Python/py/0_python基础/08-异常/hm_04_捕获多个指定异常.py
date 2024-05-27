try:
    print(1/0)
except (NameError, ZeroDivisionError):
    print('有错误')

