## [计算机网络](https://networkcv.github.io/2019/08/29/master/)
- #### [物理层](https://networkcv.github.io/2019/08/29/network-1-PhysicalLayer/)
- #### [数据链路层](https://networkcv.github.io/2019/08/29/network-2-DataLinkLayer/)
- #### [网络层](https://networkcv.github.io/2019/08/29/network-3-NetworkLayer/)
- #### [传输层](https://networkcv.github.io/2019/08/29/network-4-TransportLayer/)
- #### [应用层](https://networkcv.github.io/2019/08/29/network-5-ApplicationLayer/)

应用层准备数据
运输层将数据分段
网络层加ip地址
链路层加帧头帧尾
物理层传输数据信号

netstat -n  查看会话
![传输层协议和应用层协议关系](.\img\OSI.jpg)
![传输层协议和应用层协议关系](.\img\TCP-IP.jpg)
物理层
    集线器和网线一样属于物理层设备，它只能识别物理层的比特流
    集线器起着加强物理信号的作用，设备之间距离不能超过100米，但通过集线器的信号加强可以实现

数据链路层进行无差错传输，有错就扔
    交换机工作在数据链路层，属于二层设备，它可以识别物理层的比特流，也可以识别mac地址并决定从哪个口转发
    数据链路层
        封装成帧
        透明封装
        无差错接受
    点到点线路的数据链路层 PPP
    广播信道的数据链路层 CSMA/CD
    以太网 集线器 网桥 交换机
    100M 1000M 10000M 以太网


网络层负责在不同网络间转发数据包 基于数据包的IP地址转发
    路由器是网络层设备，数据包一般不超过1500字节
    路由器属于三层设备，它在交换机功能的基础上增加 通过ip地址查询路由表来寻找下一跳 然后 封装数据帧 转换比特流传输
    不负责丢失重传 不负责顺序
    ip地址决定起始位置和结束位置，mac地址决定下一跳是谁

保证可靠传输的是传输层
