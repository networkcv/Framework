## [计算机网络](https://networkcv.github.io/2019/08/29/master/)
- #### [物理层](https://networkcv.github.io/2019/08/29/network-1-PhysicalLayer/)
- #### [数据链路层](https://networkcv.github.io/2019/08/29/network-2-DataLinkLayer/)
- #### [网络层](https://networkcv.github.io/2019/08/29/network-3-NetworkLayer/)
- #### [传输层](https://networkcv.github.io/2019/08/29/network-4-TransportLayer/)
- #### [应用层](https://networkcv.github.io/2019/08/29/network-5-ApplicationLayer/)

# 网络层

网络层负责在不同网络间转发数据包 基于数据包的IP地址转发  
路由器是网络层设备，数据包一般不超过1500字节  
路由器属于三层设备，它在交换机功能的基础上增加 通过ip地址查询路由表来寻找下一跳 然后 封装数据帧 转换比特流传输  
不负责丢失重传 不负责顺序  
ip地址决定起始位置和结束位置，mac地址决定下一跳是谁 

数据发送过程 ![典型的数据模型](./img/3-NetworkLayer/0.jpg)
TCP/IP协议栈 ![典型的数据模型](.\img\\3-NetworkLayer\1.jpg)

#### TCP/IP协议栈
**ARP协议：**将IP地址解析为物理MAC地址
将IP地址通过广播 目标MAC地址是FF-FF-FF-FF-FF-FF 解析目标IP地址的MAC地址
是数据通信之前的协议，为IP协议提供服务，通过发送广播包，获取对方的mac地址
如果是跨网段传输则 获取路由器mac地址，获取对方mac地址后缓存，下次可以直接使用

查看ARP缓存的mac地址 arp -a
手动设置ARP地址 arp -s 192.168.1.100 D4-6D-6D-C0-4B-4E
查询mac地址    ipconfig /all

网络执法官：通过arp欺骗，回应错误mac地址，实现中断设备间通讯
p2p终结者：通过arp欺骗网关地址，实现局域网内部的流量控制

gpedit.msc 设置windows 启动脚本

**IP协议：**负责给数据包选择路径 配置路由表 有以下协议可以使用 RIP OSPF 

**ICMP协议** 网际控制报文协议 :  
ping(Packet Internet Grope) 因特网包探索器   
TTL 为包的生存时间，过一个路由器减1，linux初始64，windows初始128  
ping 192.168.1.1 -i 1 返回到指定路由器沿途第一个路由器的ip  
ping 192.168.1.1 -i 2 返回到指定路由器沿途第二个路由器的ip  
可以使用ping命令排除网络故障  
目标主机不可到达：数据包在发往目标主机的时候，中途路由器不知道下一跳该给谁，该路由器返回目标主机不可到达  
请求超时：数据包在发往目标主机的途中TTL全部消耗，或者数据包在返回时路由器不知道该给谁  
可以使用ping命令估算带宽  
可以使用ping命令发大包（超过1500字节的）  

> pathping  windows上跟踪数据包路径 计算丢包情况  
> tracert   windows上跟踪数据包路径       traceroute  在路由器上跟踪数据包路径的命令  

**IGMP协议** 网络群组管理协议 ：
> 以最少的数据包，最少的带宽 完成一对多的信息传输
- 点到点  可以调整视频进度，类似于
- 广播   类似于学校机房，教师广播自己屏幕
- 组播/多播   在发送方和接收方绑上对应的多播地址 类似于直播或电视里边的频道，计算机需要观看时，在网上上添加对应的多播地址即可

#### IP数据包结构
**IP数据包组成**
一个IP数据包由首部和数据两部分组成
![典型的数据模型](.\img\\3-NetworkLayer\2.jpg)
首部前一部分是固定长度的，共20字节，是所有ip数据包所必须具有的
在首部的固定部分的后面是一些可选字段，其长度是可变的

**IP数据包首部详情** 
![典型的数据模型](.\img\\3-NetworkLayer\3.jpg)
**版本：**用来表示 TCP/IP协议的版本 4或者6  
**首部长度：**用来分割首部和数据部分，如果首部是20byte的话，则没有可选字段   
**区分服务：**相当于数据包的优先级，优先级高的数据包优先转发，如优先级为12  
**总长度：**2个byte共16位，最大可以表示2^16-1 65535个字节  
 - 数据链路层 数据帧中的数据（由首部和数据部分组成）最大为1500字节 最大传输单元 MTU
 - 数据包大于1500字节时（一般不会），数据链路层会对其进行分片传输，每片最多1500字节,除去首部最少占用的20个字节，数据部分最多1480字节

**标识：**可以理解为数据包的id，同一个id的数据包，在分片传输后，将分片的数据包组装成一个整的数据包  
**标志：**占3位，中间位为0时允许分片，末尾位为1时，表示后边还有分片，末尾位为0时，表示最后一个分片  
**片偏移：**当标志位中末尾位为1时，表示后边还有分片，但不知道该片的先后顺序，片偏移用来解决分片组装的先后顺序问题,该值为该片起始位置在大数据包中位置  
**生存时间：**ping命令中展示的TTL，Linux默认初始64，windows 128 每过一个路由器减1，为0时路由器不处理   

**协议号：**  
- ICMP 协议号 1  
- IGMP 协议号 2  
- TCP  协议号 6  
- UDP  协议号 17  
- IPv6 协议号 41  
- OSPF 协议号 89


**首部检验和：**  对首部的数据进行检验，若结果为0，则保留，否则丢弃  
![典型的数据模型](.\img\\3-NetworkLayer\4.jpg) 

**源IP地址：**  占32位  
**目标IP地址：**  占32位

#### 路由器网关配置命令

```doc
Router>enable
Router#configure terminal
Router(config)#interface fastEthernet 0/0
Router(config-if)#ip address 192.168.0.1 255.255.255.0
Router(config-if)#no  shutdown
```

#### 路由器广域网口配置

```doc
Router>en
Router#config t
Router(config)#interface  serial 2/0
Router(config-if)#clock rate ?
Speed (bits per second
1200           
56000          
64000          
Router(config-if)#clock rate 64000
Router(config-if)#ip address 172.16.0.1 255.255.255.0
Router(config-if)#no sh
```

#### 查看/添加静态ip路由表命令

```doc
Router#show ip route
Router#config t
Router(config)#ip route 192.168.1.0 255.255.255.0 172.16.0.2   配置静态路由  
```

#### 静态路由
需要管理员告诉路由器所有没有直连的网络下一跳给谁
静态路由的缺点 适合于小规模网络 不能自动调整路由

#### 动态路由
**RIP协议**  
周期性广播路由表给其他路由器 根据跳数少选择最佳路径 30s更新一次 最大跳数 15跳 不适合网络规模比较大的环境

**配置RIP协议命令**  
> RA(config) # router rip  
> RA(config-router) #network 192.168.0.0  
> RA(config-router) #network 192.168.1.0

**OSPF**   
根据带宽选择最佳路径