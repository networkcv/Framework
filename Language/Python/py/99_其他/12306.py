
""" 自动化程序 网站 输入网址
1、支持配置出发地、目的地、乘车日
2、支持配置车次类型（动车、高铁等）
3、支持配置出发时间
4、需要手动输入登录用户名和密码 cookie WEB JavaScript
5、支持配置预定车次的选择顺序   很多车次没有票
6、支持预定、购票自动完成
7、支持配置文件路径指定
8、支持席别指定
9、支持是否允许分配无座

打开12306网站
出发地 到达地 出发的时间
点击查询
登录个人账户
选择对应的车次
加上乘车人
提交订单
最终支付
"""
#属于Python里面的第三方模块
import  openpyxl
from selenium import  webdriver
from selenium.webdriver.support.ui import  WebDriverWait #显示等待
from selenium.webdriver.support import expected_conditions as ec #等待的条件
from  selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import Select
from selenium.common.exceptions import NoSuchElementException,ElementNotVisibleException
driver=webdriver.Chrome(executable_path=r"D:\Python36\chromedriver.exe") #创建浏览器对象
class TrainSpider(object):
    #定义类属性
    login_url='https://kyfw.12306.cn/otn/resources/login.html' #登录的页面
    profile_url='https://kyfw.12306.cn/otn/view/index.html' #个人中心的网址
    left_ticket='https://kyfw.12306.cn/otn/leftTicket/init?linktypeid=dc' #余票查询页面
    confirm_url='https://kyfw.12306.cn/otn/confirmPassenger/initDc' #确认乘车人和座席

    #定义init初始化方法
    def __init__(self,from_station,to_station,train_date,trains,passenger_name_lst):
        self.from_station=from_station
        self.to_station=to_station
        self.train_date=train_date
        self.station_code=self.init_station_code()  #selft.station_code结果为dict
        self.trains=trains
        self.passenger_name_lst=passenger_name_lst
        self.selected_no=None
        self.selected_seat=None

    #登录的方法
    def login(self):
        # 打开登录的页面
        driver.get(self.login_url)

        WebDriverWait(driver,1000).until(
            ec.url_to_be(self.profile_url) #等待直到URL成为个人中心页面
        )
        print('登录成功')

    #查询余票
    def search_ticket(self):
        # 打开查询余票的网站
        driver.get(self.left_ticket)

        #找到出发站到达站的 隐藏的HTML标签
        from_station_input=driver.find_element_by_id('fromStation')
        to_station_input=driver.find_element_by_id('toStation')

        #找到出发时间的的input标签
        train_date_input=driver.find_element_by_id('train_date')

        #根据key获取value
        from_station_code=self.station_code[self.from_station]  #根据出发地找到出发地的代号
        to_station_code=self.station_code[self.to_station] #根据目的地找到目的地的代码

        #执行js代码
        driver.execute_script('arguments[0].value="%s"' % from_station_code,from_station_input)
        driver.execute_script('arguments[0].value="%s"' % to_station_code,to_station_input)

        driver.execute_script('arguments[0].value="%s"' % self.train_date, train_date_input)

        #执行点击查询按钮，执行查询操作
        query_ticket_tag=driver.find_element_by_id('query_ticket')

        driver.execute_script("arguments[0].click();", query_ticket_tag)


        #解析车次，显示等待，等待tbody的出现
        WebDriverWait(driver,1000).until(
            ec.presence_of_element_located((By.XPATH,'//tbody[@id="queryLeftTable"]/tr'))
        )
        #筛选出有数据的tr，去掉属性为datatran的tr
        trains=driver.find_elements_by_xpath('//tbody[@id="queryLeftTable"]/tr[not(@datatran)]')
        is_flag=False #标记是否有余票 ，没有余票为False，有余票为True
        while True:

            # 分别遍历每个车次
            for train in trains:
                # print(train.text)
                infos = train.text.replace('\n', ' ').split(' ')
                # print(infos)
                train_no = infos[0]  # 列表中索引为0的为车次
                if train_no in self.trains:
                    # 根据key获取值  席别是一个列表
                    seat_types = self.trains[train_no]
                    for seat_type in seat_types:  # 遍历席位的列表
                        if seat_type == 'O':  # 说明是二等座
                            count = infos[9]  # 索引为9的是二等座
                            if count.isdigit() or count == '有':
                                is_flag = True
                                break  # 退出了遍历席位的循环

                        elif seat_type == 'M':  # 说明是一等座
                            count = infos[8]  # 索引为8的是一等座
                            if count.isdigit() or count == '有':
                                is_flag = True
                                break
                    # 判断是否有余票
                    if is_flag:
                        self.selected_no = train_no
                        # 有票就可以执行单击预订
                        order_btn = train.find_element_by_xpath('.//a[@class="btn72"]')
                        order_btn.click()
                        return  # 退出的是遍历车次的循环


    def confirm(self):
        #等待来到确认乘车人界面
        WebDriverWait(driver,1000).until(
            ec.url_to_be(self.confirm_url)
        )
        #等待乘车人标签显示出来
        WebDriverWait(driver,1000).until(
            ec.presence_of_element_located((By.XPATH,'//ul[@id="normal_passenger_id"]/li/label'))
        )
        #获取所有的乘车人
        passengers=driver.find_elements_by_xpath('//ul[@id="normal_passenger_id"]/li/label')
        for passenger in passengers: #分别遍历每一位乘车人 (label标签)
            name=passenger.text  #获取乘车人的姓名
            if name in self.passenger_name_lst:
                passenger.click()   #label标签的单击

        #确认席位
        seat_select=Select(driver.find_element_by_id('seatType_1'))
        seat_types=self.trains[self.selected_no] #根据key获取值 self.trains是要抢票的车次的字典，self.selected_no 要抢票的车次的key
        for seat_type in seat_types:
            try:
                seat_select.select_by_value(seat_type)
                self.selected_seat=seat_type #记录有票的座席

            except NoSuchElementException:
                continue
            else:
                break
        WebDriverWait(driver,1000).until(
            ec.element_to_be_clickable((By.ID,'submitOrder_id')) #等待到a标签可以单击的时候
        )
        #提交订单
        submit_btn=driver.find_element_by_id('submitOrder_id')
        submit_btn.click()

        #显示等待，等待到模式对话框窗口出现
        WebDriverWait(driver,1000).until(
            ec.presence_of_element_located((By.CLASS_NAME,'dhtmlx_window_active'))
        )
        #等待按钮可以点击
        WebDriverWait(driver,1000).until(
            ec.element_to_be_clickable((By.ID,'qr_submit_id'))

        )
        submit_btn=driver.find_element_by_id('qr_submit_id')
        while submit_btn:
            try:
                submit_btn.click()
                submit_btn = driver.find_element_by_id('qr_submit_id')
            except ElementNotVisibleException:
                break
        print(f'恭喜{self.selected_no}的{self.selected_seat}抢票成功!!!')



    #负责调用其它方法（组织其它的代码）
    def run(self):
        # 1.登录
        self.login()
        # 2.余票查询
        self.search_ticket()

        # 3.确认乘车人和订单
        self.confirm()

    def init_station_code(self):
        wb=openpyxl.load_workbook('车站代码.xlsx')
        ws=wb.active #使用活动表
        lst=[] #存储所有车站名称及代号
        for row in ws.rows: #遍历所有行
            sub_lst=[] #用于存储每行中的车站名称，车站代号
            for cell in row: #遍历一行中的单元格
                sub_lst.append(cell.value)
            lst.append(sub_lst)
        #print(dict(lst)) #将列表转成字典类型
        return  dict(lst)

#启动爬虫程序
def start():                                   # O 表示的是二等座,M 代表是一等座
    spider=TrainSpider('北京南','上海虹桥','2022-04-04',{'G103':['O','M']},['孙泽龙'])
    spider.run()
    #spider.init_station_code()

if __name__ == '__main__':
    start()
