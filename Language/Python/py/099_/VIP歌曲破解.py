#!/usr/bin/python3
# *_*coding:utf8 *_*
# 导包
import requests
import time
# 提示用户输入想下载的歌手跟第几页的歌曲
singer = input("请输入歌手姓名：")
num = input("请输入想下载的页数：")
# 目标网址
url = "http://www.kuwo.cn/api/www/search/searchMusicBykeyWord?key={}&pn={}&rn=30&httpsStatus=1&reqId=72d80f20-1209-11eb-83b9-712f2c6fec62".format(singer, num)
# 请求头
headers = {
    # 代理信息  模拟浏览器发送请求
    "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36",
    # 开的谁的门
    "Cookie": "Hm_lvt_cdb524f42f0ce19b169a8071123a4797=1603111711; Hm_lpvt_cdb524f42f0ce19b169a8071123a4797=1603111711; _ga=GA1.2.2097782952.1603111711; _gid=GA1.2.1633493904.1603111711; _gat=1; kw_token=7MN1T8RT2O8",
    # 令牌  钥匙
    "csrf": "7MN1T8RT2O8",
    # 域名
    "Host": "www.kuwo.cn",
    # 防盗链接  数据从哪里来的
    "Referer": "http://www.kuwo.cn/search/list?key=%E5%91%A8%E6%9D%B0%E4%BC%A6"
}
# 获取网页数据
re = requests.get(url, headers=headers)
time.sleep(2)
response = re.json()
# print(response)
# 提取我们想要的数据
data = response["data"]["list"]
# print(data)
# 依次拿出数据中的每一条数据
for i in data:
    # 获取相应信息
    rid = i["rid"]
    name = i["name"]
    # print(rid,name)
    # 拼接新网址
    new_url = "http://antiserver.kuwo.cn/anti.s?response=url&rid=MUSIC_" + str(rid) + "&format=aac|mp3&type=convert_url"
    r = requests.get(new_url).text
    result = requests.get(r).content
    # 歌曲存放路径
    path = r'D:\crawFile\music\\' + name + '.mp3'
    # 写入文件夹  二进制
    with open(path, "wb") as f:
        f.write(result)
        print("正在下载", name)
