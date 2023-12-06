import psutil
from collections import defaultdict

# 遍历每个连接，按连接状态累加
stats = defaultdict(int)
for conn in psutil.net_connections('tcp'):
    stats[conn.status] += 1

# 遍历每种状态，输出连接数
for status, count in stats.items():
    print(status, count)