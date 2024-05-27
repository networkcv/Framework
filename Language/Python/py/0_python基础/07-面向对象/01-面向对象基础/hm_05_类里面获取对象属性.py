class Washer():
    def wash(self):
        print('洗衣服')

    # 获取实例属性
    def print_info(self):
        # self.属性名
        # print(self.width)
        print(f'洗衣机的宽度是{self.width}')
        print(f'洗衣机的高度是{self.height}')


haier1 = Washer()

# 添加属性
haier1.width = 400
haier1.height = 500

# 对象调用实例方法
haier1.print_info()


