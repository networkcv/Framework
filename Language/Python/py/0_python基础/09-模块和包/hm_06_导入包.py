# 方法一
"""
1. 导入
import 包名.模块名
2. 调用功能
包名.模块名.功能()
"""
# 导入mypackage包下的模块1，使用这个模块内的info_print1函数
# import mypackage.my_module1
# mypackage.my_module1.info_print1()


# 方法二：注意 设置__init__.py文件里面的all列表，添加的是允许导入的模块
"""
from 包名 import *
模块名.目标
"""
from mypackage import *
my_module1.info_print1()

