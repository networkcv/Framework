import requests

# 发送 HTTP 请求，获取数据并转为 JSON 对象
response = requests.get("http://example.com/api/work-hours").json()

# 计算总共工作时长和工作天数
total_hours = 0
working_days = 0
for day in response:
    total_hours += day["hours"]
    working_days += 1 if day["isWorkingDay"] else 0

# 计算平均工作时长
avg_hours = total_hours / working_days

# 输出结果
print(f"本月共有 {working_days} 个工作日，总共工作时长为 {total_hours} 小时，平均每个工作日工作时长为 {avg_hours:.2f} 小时。")