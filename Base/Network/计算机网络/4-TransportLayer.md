##  [计算机网络](https://networkcv.github.io/2019/08/29/master/)
- #### [物理层](https://networkcv.github.io/2019/08/29/network-1-PhysicalLayer/)
- #### [数据链路层](https://networkcv.github.io/2019/08/29/network-2-DataLinkLayer/)
- #### [网络层](https://networkcv.github.io/2019/08/29/network-3-NetworkLayer/)
- #### [传输层](https://networkcv.github.io/2019/08/29/network-4-TransportLayer/)
- #### [应用层](https://networkcv.github.io/2019/08/29/network-5-ApplicationLayer/)

# 传输层  

## TCP协议  
(Transmission Control Protocol，传输控制协议)   
将传输的文件**分段传输，建立会话 可靠传输 流量控制**  
TCP提供全双工通信 面向字节流

#### TCP报文段的首部格式 
![TCP报文段的首部格式](.\img\4-TransportLayer\4.jpg)
**序号(seq)：**  该报文段字节在总字节的序号  
**确认号(ack)：**  告诉发送者下一报文段开始的字节序号  
**数据偏移：**  告诉接收方从TCP数据包部分的开始位置，4位，表示0-15   
    每个数值代表4个字节，首部最多60字节，长度可变的选项 最多占40字节  

**标记位**
- URG 加急发送，不用在发送缓冲区中进行排队  
- ACK 确认号是否有效，0 无效  1有效 ，在第一次建立连接时为0，其余为1
- PSH 优先处理，在接收方的接收缓冲区中，优先处理
- RST 为1时拒绝接收数据包，需要重新建立连接，类比中断加载网页
- SYN 同步字段 建立连接时为1，其他传输时为0
- FIN 释放连接 

**窗口：**   根据对方的接收窗口（缓存）大小来规定自己的发送串口大小 
![窗口：](.\img\4-TransportLayer\5.jpg) 
**检验和：**  检验首部和数据部分   
**紧急指针：**  在URG标记位为1时有效，记录加急发送的部分  

#### TCP如何实现可靠传输
**停止等待:**  
![停止等待](.\img\4-TransportLayer\2.jpg)
**丢失重传:**  
![丢失重传](.\img\4-TransportLayer\3.jpg) 
根据确认和重传机制，在不可靠的传输网络上实现可靠通信  
这种可靠传输协议常称为自动重传请求ARQ   
以字节为单位的滑动窗口技术  SACK：选择性确认，告诉发送方中间丢失的数据包


#### TCP协议如何实现流量控制
https://blog.csdn.net/yechaodechuntian/article/details/25429143  
利用滑动窗口实现流量控制  解决通信两端处理速度不同问题

#### TCP协议如何实现拥塞控制
发送方维持一个拥塞窗口 cwnd ( congestion window )的状态变量。拥塞窗口的大小取决于网络的拥塞程度，并且动态地在变化。  

慢开始：发包的数量以2倍数增长，如果出现拥塞，则将慢开始门限调整为当前拥塞窗口的一半 并将发包数量降为1；拥塞避免：达到慢开始门限时，则增长方式改为加1  

快重传：收到4却没有收到3，则立马发送3个确认包ack=3；快恢复：发送方收到3个重复的确认包后，说明当前网络正常，则从慢开始门限的一半开始发包，而不是降为1  

发送窗口的上限值=Min[rwnd（接收窗口）,cwnd（拥塞窗口）]  

#### TCP的传输连接管理
**三个阶段：**  建立连接、传送数据和释放连接  
TCP连接的建立都是采用客户服务器方式，主动发起的叫客户client  
被动等待连接的叫服务器server  

**三次握手建立TCP连接** 
![三次握手建立TCP连接](.\img\4-TransportLayer\6.jpg) 
**需要三次的原因是如果客户端第一次发送的请求在路由选择时，网络拥塞，客户端等了一会没消息，于是又发送了一次请求，此时网络畅通，服务端对两个请求都进行响应回复** 
**然后客户端收到两次回复后，只对第一次回复进行反馈，第二次回复不理会，但此时服务端还在处于第二次回复的等待中，如果这种情况多的话，会耗尽系统资源**  

**抓包实例** 

![抓包实例](.\img\4-TransportLayer\7.jpg)
**三次握手建立TCP连接的各状态** ![典型的数据模型](.\img\4-TransportLayer\8.jpg)

![四次挥手关闭TCP连接](.\img\4-TransportLayer\9.jpg) 
**四次挥手关闭TCP连接** 

- 第一次客户端发送释放连接请求FIN=1，发送后，客户端便不能再向服务端发送数据了， FIN=1 seq=u
- 第二次服务端还可能有数据没有发送完毕，所以服务端还可以继续发送  ACK=1 seq=v ack=u+1
- 第三次服务端数据发送完毕，需要给客户端确认释放连接，FIN=1 ACK=1 seq=w ack=u+1
- 第四次客户端对服务端返回的确认消息进行再次确认，ACk=1 seq=u+1 ack=w+1 发送这条消息后，客户端处于time-wait状态2MSL，大概4分钟，服务端收到则会关闭连接
> 原因在于如果客户端发送最后一条消息后直接关闭，消息传输中丢失，服务端重新发送释放连接确认，此时客户端已经关闭无法回应，服务端一直等待释放连接，耗尽系统资源

#### 基于TCP协议的网络攻击
- SYN攻击：伪造不存在的ip地址，发送TCP连接请求，目标主机收到连接请求后做出响应，等待发送方再次确认后开始数据传输，发送方不进行再次确认，使目标主机等待建立连接，耗尽系统资源，  
- Land攻击：伪造虚假报文，使目标主机和自己建立TCP连接，耗尽系统资源

## UDP协议(User Data Protocol,用户数据报协议)
一个数据包就能完成数据通信 不分段 不需要建立会话 不可靠传输 不需要流量控制 屏幕广播 多播
UDP的首部开销小，只有8个字节 具体首部格式如 ![UDP协议](.\img\4-TransportLayer\1.jpg)
图中**伪首部**的意思是在UDP的首部中不包含其,但是UDP首部中的**检验和**的计算过程需要用到伪首部的数据

## 传输层协议和应用层协议关系   

![传输层协议和应用层协议关系](.\img\4-TransportLayer\0.jpg)
```doc
HTTP      80         TCP
HTTPS     443        TCP
RDP       3389       TCP        远程桌面
FTP       21         TCP        
共享文件夹 445        TCP
TELNET    23         TCP
SMTP      25         TCP        发邮件
POP3      110        TCP        收邮件
SQLServer 1433       TCP
DNS       53         UDP        域名解析
TFTP      69         UDP
SNMP      161        UDP
RIP       520        UDP
```
> mstsc 连接远程桌面

## 服务和应用层协议之间的关系  
服务使用TCP或UDP的端口侦听客户端请求  
客户器使用IP地址定位服务器  使用目标端口 定位服务  
可以在服务器网卡上设置只开放必要的端口 实现服务器网络安全

## windows上安装服务
- DNS服务
- Web服务
- SMTP服务
- POP3服务

## 如何查看服务侦听的端口
```doc
netstat -n  查看建立会话命令
netstat -nb 查看建立会话的进程
netstat -an 查看侦听端口
netstat -n | find "ESTABLISHED"
telnet 192.168.80.100 3389 测试到远程计算机某个端口是否打开
```