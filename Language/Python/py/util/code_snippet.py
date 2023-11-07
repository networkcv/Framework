import time

timestamp = time.time()
print(type(timestamp))
print(timestamp)

localtime = time.localtime(timestamp)
print(type(localtime))
print(localtime)
formatted_time = time.strftime('%Y-%m-%d %H:%M:%S', localtime)
print(formatted_time)
