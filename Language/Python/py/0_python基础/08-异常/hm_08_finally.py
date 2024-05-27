# 需求：尝试以r打开文件，如果有异常以w打开这个文件，最终关闭文件
try:
    f = open('test100.txt', 'r')
except Exception as e:
    f = open('test100.txt', 'w')
finally:
    f.close()



