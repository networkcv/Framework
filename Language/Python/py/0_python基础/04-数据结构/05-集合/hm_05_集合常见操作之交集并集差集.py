set1 = {1, 2, 3, 4, 5}
set2 = {4, 5, 6, 7, 8}

# 交集
intersection = set1.intersection(set2)
print("交集：", intersection)  # 输出: 交集： {4, 5}

# 并集
union = set1.union(set2)
print("并集：", union)  # 输出: 并集： {1, 2, 3, 4, 5, 6, 7, 8}

# 差集（set1 - set2）
difference1 = set1.difference(set2)
print("set1 相对于 set2 的差集：", difference1)  # 输出: set1 相对于 set2 的差集： {1, 2, 3}

# 差集（set2 - set1）
difference2 = set2.difference(set1)
print("set2 相对于 set1 的差集：", difference2)  # 输出: set2 相对于 set1 的差集： {6, 7, 8}
