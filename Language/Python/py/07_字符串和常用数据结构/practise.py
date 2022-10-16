import os
import random
import time


# 跑马灯
def run():
    content = '欢迎光临——'
    while True:
        # 清理屏幕上的输出
        os.system('clear')
        print(content)
        # 休眠200毫秒
        time.sleep(0.2)
        content = content[1:] + content[0]


# 练习2：设计一个函数产生指定长度的验证码，验证码由大小写字母和数字构成。
def random_code(code_len=4):
    chars = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ'
    result = ''
    for _ in range(code_len):
        single_char_index = random.randint(0, len(chars))
        single_char = list(chars)[single_char_index]
        result += single_char
    return result


print(random_code())


# 练习3：设计一个函数返回给定文件名的后缀名
def return_file_suffix(file_name):
    index = file_name.find('.')
    return file_name[index + 1:] if index > 0 else 'no thing'


print(return_file_suffix('a.txt'))


# 练习4：设计一个函数返回传入的列表中最大和第二大的元素的值。
def return_max_value(num_list):
    if len(num_list) == 1:
        return num_list[0]
    if len(num_list) == 2:
        if num_list[0] < num_list[1]:
            num_list[0], num_list[1] = num_list[1], num_list[0]
        return num_list[0], num_list[1]
    max_index = len(num_list) - 1
    for x in range(max_index):
        for i in range(x, max_index):
            if num_list[i] > num_list[i + 1]:
                num_list[i], num_list[i + 1] = num_list[i + 1], num_list[i]
    return num_list[-1], num_list[-2]


print(return_max_value([1]))
print(return_max_value([1, 2]))
print(return_max_value([1, 4, 3, 2]))


#  练习5：计算指定的年月日是这一年的第几天。
def is_leap_year(year):
    return year % 4 == 0 and year % 100 != 0 or year % 400 == 0


def which_day(year, month, date):
    days_of_month = [
        [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31],
        [31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31]
    ][is_leap_year(year)]
    total = 0
    for index in range(month - 1):
        total += days_of_month[index]
    return total + date


print(which_day(1980, 11, 28))
print(which_day(1981, 12, 31))
print(which_day(2018, 1, 1))
print(which_day(2016, 3, 1))
