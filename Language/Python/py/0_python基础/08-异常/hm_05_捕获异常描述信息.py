try:
    print(1/0)
except (NameError, ZeroDivisionError) as result:
    print(result)

