# 生成器（Generator）在Python中提供了一种优雅的方式来处理迭代操作，它们带来了许多好处：
# 1. **内存效率**：
# - 生成器在迭代大型数据集时特别有用，因为它们不需要一次性将所有数据加载到内存中。相反，它们一次产生一个值，从而减少了内存消耗。
# 2. **懒加载（Lazy Evaluation）**：
# - 生成器提供了懒加载的能力，即只有在需要时才计算下一个值。这可以提高程序的响应速度，尤其是在处理复杂的计算或I/O操作时。
from icecream import ic


def large_range(start, end):
    n = start
    while n < end:
        yield n
        n += 1


g = large_range(1, 1000001)
ic(g)
print(g)
# 迭代1到1000000的数字，不会一次性加载到内存
for number in g:
    pass


def reverse_data(data):
    for item in data:
        yield item


# 反转字符串
reversed_string = ' '.join(reverse_data('hello'))
reversed_string2 = ''.join(['a','b','c','d','e'])
print(reversed_string)
print(reversed_string2)
