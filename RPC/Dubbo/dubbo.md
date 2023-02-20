# SPI

## TelnetHandler

`org.apache.dubbo.remoting.telnet.TelnetHandler` 

实现该接口可以在终端下通过 telnel 访问服务器，查看RPC中已注册的接口及方法列表，查询入参出参的结构就是通过这个实现的。

还可以用来做一些在线运维的工作。

**示例**

```java
@Activate
@Help(parameter = "[-l] [service]", summary = "List services and methods.", detail = "List services and methods.")
public class ListTelnetHandler implements TelnetHandler {
  
  private ServiceRepository serviceRepository = ApplicationModel.getServiceRepository();

  @Override
  public String telnet(Channel channel, String message) {
    return "res"
  }
}
```

SPI配置：META-INF/dubbo/org.apache.dubbo.remoting.telnet.TelnetHandler

```properties
#配置形式：待处理命令=解析器
ls=com.lwj.ListTelnetHandler
```

```shell
telnet 127.0.0.1 20880 
dubbo> ls 
# output
res
```

