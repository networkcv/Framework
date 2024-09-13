X-DB 数据库

目录绍织形式

sql：SQL引擎 处理SQL解析和优化
parser：用于做SQL语句的解析
optimizier：用于生成SQL语句过程进行执行计划的优化

executor：执行引擎，用于执行通过SQL语句产生的具体执行计划
operator：执行算子，是执行计划的最小单位

storage：存储引擎，用于存储和组织数据
transaction：用于事务管理

network：网络通信模块

common：公共组件部分，工具函数等

