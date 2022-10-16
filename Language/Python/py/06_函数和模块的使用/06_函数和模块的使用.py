import model1
import model2

print(__name__)


# 求最大公约数
def gcd(x, y):
    res = 1
    (x, y) = (y, x) if x > y else (x, y)
    for i in range(1, y + 1):
        if (x % i == 0) and (y % i == 0):
            res = i if i > 1 else res
    return res


print(gcd(220, 3))


# 求最小公倍数
def lcm(x, y):
    return x * y // gcd(x, y)


print(list(range(9, 0, -1)))


def fu1():
    a = 2

    def fu3():
        nonlocal a
        a = 3

    fu3()
    print(a)


fu1()


def main():
    print("main")
    pass


if __name__ == '__main__':
    main()
