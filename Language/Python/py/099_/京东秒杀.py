#导入要使用到的模块(工具)
import datetime
import time
from selenium import webdriver
from selenium.webdriver.common.by import By
import win32com.client
speaker = win32com.client.Dispatch("SAPI.SpVoice")


# 打开浏览器
browser = webdriver.Chrome()
# 进入京东网页
browser.get("https://www.jd.com/")
time.sleep(3)
# 扫码登录
browser.find_element(By.LINK_TEXT,"你好，请登录").click()
print(f"请扫码")
time.sleep(8)
# 打开购物车页面
browser.get("https://cart.jd.com/cart_index")
time.sleep(5)
# 全选购物车
while True:
    if browser.find_element(By.CLASS_NAME, 'jdcheckbox'):
        browser.find_element(By.CLASS_NAME, 'jdcheckbox').click()
        break

while True:
    #获取电脑现在的时间
    now = datetime.datetime.now().strftime('%Y-%m-%d %H:%M:%S.%f')
    # 对比时间，时间到的话就点击结算
    print(now)
    #判断是不是到了秒杀时间?
    if now >= '2023-03-03 16:45:00':
        # 点击结算按钮
        while 1==1:
            try:
                if browser.find_element(By.LINK_TEXT,"去结算"):
                    print("here")
                    browser.find_element(By.LINK_TEXT,"去结算").click()
                    print(f"主人,结算提交成功,我已帮你抢到商品啦,请及时支付订单")
                    speaker.Speak(f"主人,结算提交成功,我已帮你抢到商品啦,请及时支付订单")
                    break
            except:
                pass
        #点击提交订单按钮
        while True:
            try:
                if browser.find_element(By.LINK_TEXT,"提交订单"):
                    browser.find_element(By.LINK_TEXT,"提交订单").click()
                    print(f"抢购成功，请尽快付款")
            except:
                print(f"主人,我已帮你抢到商品啦,您来支付吧")
                break
        time.sleep(0.01)