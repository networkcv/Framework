## 常用命令

https://fr3nky.uk/2024/02/02/linux/



## vim

<img src="img/Linux命令/vim工作模式.png" alt="ssh命令" style="zoom:50%;" />

/ 搜索  n 是下一个， N 是上一个。

:set number 开启行号

:数字 快速跳转行号

CTRL + END 跳到最后

- **Ctrl + A**：将光标移到命令行或文本行的开头（A 表示 "Beginning"，即移动到行首）。
- **Ctrl + E**：将光标移到命令行或文本行的末尾（E 表示 "End"，即移动到行尾）。
- **Ctrl + F**：向前移动一个字符（F 表示 "Forward"，即向前移动）。
- **Ctrl + B**：向后移动一个字符（B 表示 "Backward"，即向后移动）。
- **Ctrl + L**：清屏并将当前行移到屏幕顶部（L 表示 "Clear"，即清除屏幕）。
- **Ctrl + U**：删除从光标位置到行首的所有字符（U 表示 "Delete"，即删除）。
- **Ctrl + K**：删除从光标位置到行尾的所有字符（K 表示 "Kill"，即清除）。
- **Ctrl + Y**：粘贴删除的文本到光标位置（Y 表示 "Yank"，即粘贴）。
- **Ctrl + D**：在空白行上按下表示退出终端（D 表示 "Delete"，即删除）；在非空行上按下表示删除光标后的字符。
- **Ctrl + C**：终止当前正在运行的命令（C 表示 "Cancel"，即取消）。
- **Ctrl + Z**：将当前进程挂起并放入后台（Z 表示 "Suspend"，即挂起）。
- **Tab**：自动补全命令或文件名。
- **Ctrl + R**：反向搜索命令历史记录，按下后可以开始输入关键字来查找之前执行过的命令。
- **Ctrl + H**：删除光标前的字符（H 表示 "Backspace"）。
- **Ctrl + W**：删除光标前的单词（W 表示 "Word"）。
- **Ctrl + N**：在命令历史记录中向后滚动（N 表示 "Next"，即下一个）。
- **Ctrl + P**：在命令历史记录中向前滚动（P 表示 "Previous"，即上一个）。
- **Ctrl + S**：暂停屏幕输出（S 表示 "Stop"），按下一次可恢复屏幕输出。
- **Ctrl +Q**：恢复被 Ctrl + S 暂停的屏幕输出（Q 表示 "Continue"）。
- **Ctrl +T**：交换光标前两个字符的位置（T 表示 "Transpose"，即交换）。
- **Ctrl +X, Ctrl +E**：打开当前命令行在编辑器中进行编辑，编辑完成后会执行编辑后的命令。

vi 常用命令

| 命令                                          | 说明               |
| :-------------------------------------------- | :----------------- |
| yy                                            | 复制光标所在行     |
| p                                             | 粘贴               |
| dd                                            | 删除/剪切当前行    |
| V                                             | 按行选中           |
| u                                             | 撤销               |
| ctr+r                                         | 反撤销             |
| >>                                            | 往右缩进           |
| <<                                            | 往左缩进           |
| :/搜索的内容                                  | 搜索指定内容       |
| :%s/要替换的内容/替换后的内容/g               | 全局替换           |
| :开始行数,结束行数s/要替换的内容/替换后的内容 | 局部替换           |
| .                                             | 重复上一次命令操作 |
| G                                             | 回到最后一行       |
| gg                                            | 回到第一行         |
| 数字+G                                        | 回到指定行         |
| shift+6                                       | 回到当前行的行首   |
| shift+4                                       | 回到当前行的行末   |
| ctr+f                                         | 下一屏             |
| ctr+b                                         | 上一屏             |

## man

`man` 命令是一个非常有用的命令，当你不会使用某个`Linux` 命令时，可以使用`man` 来查看其帮助文档。`man` 命令查到的手册来源于目录`/usr/share/man/`，可以看到该目录下有9 个这样的目录：

```shell
ls /usr/share/man/

man1
man2
man3
man4
man5
man6
man7
man8
man9
```

`man` 手册分9 种，用数字`[1-9]` 来区分，分别代表的含义如下（可以用`man man` 命令来查看）：

<img src="img/Linux指南/image-20230404152845055.png" alt="image-20230404152845055" style="zoom:50%;" />

可以通过设置不同的数字来查看不同的手册，如下：

```text
man 1 系统命令名
man 2 系统函数名
man 3 库函数名
```

如果没有带这个数字，则会从`1` 查到`9`，直到查到第一个为止。

**.man**文件是一个Unix风格（只有LF换行符）的纯文本文件，包含一个命令或其他项目的描述。它的格式是使用传统的 "troff "文本格式化工具。在GNU/Linux机器上，全系统的manpages通常位于"/usr/share/man "目录下，按语言分类并打包成gzip（.gz）档案。**.man**后缀本身通常只用于第三方的manpage文件。任何**.man**文件都可以用文本编辑器轻松打开。

## groff

groff（GUN roff） 是 roff 排版系统现在最常用的实现，其它实现有troff、nroff、ditroff等等。尽管历史悠久，roff当前还在广泛使用中。UNIX系统的man手册页、很多软件书籍和标准是用roff来写的。

[Groff概览](https://www.chungkwong.cc/groff.html)

[使用 groff 编写 man 手册页](https://linux.cn/article-9122-1.html)

[Unix, Linux 和MacOS](https://juejin.cn/post/6844903841901576199)







## setfile 修改文件创建和修改时间

**更新创建日期**

```
setfile -d '12/5/2020 00:00:00'  filepath
```

**更新修改日期**

```
setfile -m '12/5/2020 00:00:00'  filepath
```



## cat

显示最后一屏内容	

cat 文件名	 cat 是由第一行到最后一行连续显示在屏幕上，而 tac 则是由最后一行到第一行反向在屏幕上显示出来



## tail

[tail命令详解](https://www.myfreax.com/linux-tail-command/)

查看文件的后几行	

tail -10 文件名	

持续输出某个文件的后续变化

tail -f  文件名



## more

`more` 命令用于查看Linux 文件的内容，用`more filename` 打开一个文件后，可以控制`向上`或`向下`翻滚页面。

**操作键说明:**

| 操作键 | 说明           |
| :----- | :------------- |
| 空格   | 显示下一屏信息 |
| 回车   | 显示下一行信息 |
| b      | 显示上一屏信息 |
| f      | 显示下一屏信息 |
| q      | 退出           |



## mkdir 

mkdir -p /a/b 可以创建多级文件夹



## telnet

在linux和centos下，我们就直接可以用telnet命令来测试端口是否畅通。具体用法：telnet 指定的IP或者 域名 端口号.

```bash
比如：telnet 192.168.1.102 3306

内网 telnet 127.0.0.1 8090
```

在Mac os 下

```bash
nc -vz -w 2 192.168.1.104 3306
```



## sftp

https://zhuanlan.zhihu.com/p/51749905

1）连接远程服务器

```text
sftp remote_user@remote_host
```

2）使用端口进行连接

```text
sftp -P remote_port remote_user@remote_host
```

3）从远程服务器拉取文件

```text
get /path/remote_file
```

4）上传本地文件到服务器

```text
put local_file
```

5）查看远程服务器目录内容

```text
ls
```

6）查看本地目录内容

```text
lls
```

7）执行本地 Shell 命令

```text
![command]
```



## scp

复制文件到服务器 

```bash
scp /Users/networkcavalry/Documents/IDEAProjects/article-center/target/article-center-1.0-SNAPSHOT.jar   root@47.97.244.231:/root
```



## lsof

Linux 查看端口占用情况可以使用 **lsof** 和 **netstat** 命令。

lsof(list open files)是一个列出当前系统打开文件的工具。

```bash
lsof -i:端口号
```



## diff 文件比较

```·
diff a.txt b.txt 

diff a.txt b.txt -y
```



## z 快速路径切换

https://commandnotfound.cn/linux/1/589/z-%E5%91%BD%E4%BB%A4

## chmod

https://www.runoob.com/linux/linux-comm-chmod.html

Linux chmod（英文全拼：change mode）命令是控制用户对文件的权限的命令

Linux/Unix 的文件调用权限分为三级 : 文件所有者（Owner）、用户组（Group）、其它用户（Other Users）。

<img src="img/Linux命令/file-permissions-rwx.jpg" alt="img" style="zoom:33%;" />

只有文件所有者和超级用户可以修改文件或目录的权限。可以使用绝对模式（八进制数字模式），符号模式指定文件的权限。

<img src="img/Linux命令/rwx-standard-unix-permission-bits.png" alt="img" style="zoom: 90%;" />

| 数字 | 含义           | 字符 |
| ---- | -------------- | ---- |
| 7    | 读 + 写 + 执行 | rwx  |
| 6    | 读 + 写        | rw-  |
| 5    | 读 + 执行      | r-x  |
| 4    | 只读           | r--  |
| 3    | 写 + 执行      | -wx  |
| 2    | 只写           | -w-  |
| 1    | 只执行         | --x  |
| 0    | 无             | ---  |

总结：

- 利用 **chmod** 命令可以控制文件的操作权限。
- 字母法格式: chmod 不同角色设置的权限 文件
- 数字法格式: chmod 不同角色的权限值 文件名



## >（重定向）

重定向也称为输出重定向，把在终端执行命令的结果保存到目标文件。

| 命令 | 说明                                                       |
| :--- | :--------------------------------------------------------- |
| >    | 如果文件存在会覆盖原有文件内容，相当于文件操作中的‘w’模式  |
| >>   | 如果文件存在会追加写入文件末尾，相当于文件操作中的‘a’ 模式 |



## |（管道）

管道(|)：一个命令的输出可以通过管道做为另一个命令的输入。

xargs 可以接收管道输出的参数，并且作为另外一个命令的参数，而非输入。

```sh
 ls | grep test | xargs find
```

## xargs 标准输入转为命令行参数

通常搭配管道一起使用

```shell
echo "hello world" | echo 
# 不会有输出
```

```shell
$ echo "hello world" | xargs echo
# hello world
```

[xargs 教程](https://www.ruanyifeng.com/blog/2019/08/xargs-tutorial.html)



## ln 链接

链接命令是创建链接文件，链接文件分为:

- 软链接
- 硬链接

| 命令  | 说明       |
| :---- | :--------- |
| ln -s | 创建软链接 |
| ln    | 创建硬链接 |

**软链接**

类似于**Windows下的快捷方式**，当一个源文件的目录层级比较深，我们想要方便使用它可以给源文件创建一个软链接。

**注意点:**

- **如果软链接和源文件不在同一个目录，源文件要使用绝对路径，不能使用相对路径。**
- **删除源文件则软链接失效**
- **可以给目录创建软链接**



**硬链接**

**硬链接数就是文件数据被文件名使用的次数, 好比引用计数**。

硬链接的作用是可以给重要文件创建硬链接，能够防止文件数据被误删，删除源文件，软链接失效，但是硬链接依然可以使用。

**注意点:**

- **创建硬链接使用相对路径和绝对路径都可以**
- **删除源文件，硬链接还可以访问到数据。**
- **创建硬链接，硬链接数会加1，删除源文件或者硬链接，硬链接数会减1。**
- **创建软链接，硬链接数不会加1**
- **不能给目录创建硬链接**



## grep 文本搜索

```sh
grep 'python' a.txt 		#带引号
grep python a.txt				#不带引号
grep ^a a.txt						#正则匹配
cat a.txt | grep python #结合管道命令
```

| 命令选项 | 说明                       |
| :------- | :------------------------- |
| -i       | 忽略大小写                 |
| -n       | 显示匹配行号               |
| -v       | 显示不包含匹配文本的所有行 |

支持正则表达式。

| 正则表达式 | 说明                   |
| :--------- | :--------------------- |
| ^          | 以指定字符串开头       |
| $          | 以指定字符串结尾       |
| .          | 匹配一个非换行符的字符 |



## which 查找命令

查看命令所在的位置

```sh
which docker
```



## find 查找文件

查找命令，在指定目录下查找文件(包括目录)

```sh
find / -name my.cnf
find . -name '1.txt'
find . -name '*.txt'
```

| 选项  | 说明                         |
| :---- | :--------------------------- |
| -name | 根据文件名(包括目录名)字查找 |



find命令结合通配符的使用，是一种特殊语句，主要有星号(*)和问号(?)，用来模糊搜索文件

| 通配符 | 说明                  |
| :----- | :-------------------- |
| *      | 代表0个或多个任意字符 |
| ?      | 代表任意一个字符      |

通配符不仅能结合 **find** 命令使用，还可以结合其它命令使用, 比如: **ls、mv、cp** 等，这里需要注意只有 **find** 命令使用通配符需要加上引号。



find 也可以查询目录下的文件列表

```sh
find /xxx/ 
```





## tar 压缩和解压缩

Linux默认支持的压缩格式:

- .gz
- .bz2
- .zip

**说明:**

- .gz和.bz2的压缩包需要使用tar命令来压缩和解压缩
- .zip的压缩包需要使用zip命令来压缩，使用unzip命令来解压缩



```sh
tar -zcvf t.biz test
tar -xvf t.biz  test
tar -xvf t.biz  test -C aa
```

| 选项 | 说明                               |
| :--- | :--------------------------------- |
| -c   | 创建打包文件                       |
| -v   | 显示打包或者解包的详细信息         |
| -f   | 指定文件名称, 必须放到所有选项后面 |
| -z   | 压缩或解压缩(.gz)                  |
| -j   | 压缩或解压缩(.bz2)                 |
| -x   | 解包                               |
| -C   | 解压缩到指定目录（目录必须存在）   |



## zip/unzip 压缩和解压缩



```sh
zip c test1 			#将test1文件夹压缩生成c.zip
unzip c.zip
unzip c.zip -d BB #解压缩到BB目录下，不存在则自动创建
```



| 命令  | 说明               |
| :---- | :----------------- |
| zip   | 压缩成.zip格式文件 |
| unzip | 解压缩.zip格式文件 |

**unzip命令选项:**

| 选项 | 说明                               |
| :--- | :--------------------------------- |
| -d   | 解压缩到指定目录（目录可以不存在） |

- 压缩文件尽量使用.gz格式，因为占用空间较少
- 使用zip命令压缩的文件占用空间比较多, 当时比较通用，操作更加简单。



## sudo 管理员权限

```sh
sudo -s			#切换管理员身份
sudo mv 		#某一条指令获取管理员权限
whoami 			#查看当前用户权限
exit				#退出登录用户
```



## 用户和用户组 TODO





## OrbStack

```sh
orb #进入默认机器
orb -m [machine_name] #进入指定机器
```





## systemctl

https://segmentfault.com/a/1190000023029058

systemctl命令是Systemd中最重要的一个命令，而Systemd是管理linux系统的程序。

systemctl 可以对服务进行启动，停止等操作，在Systemd中有Unit的概念，每个进程都是一个Unit，总共有十二种Unit类型：

- Service unit，系统服务
- Target unit，多个 Unit 构成的一个组
- Device Unit，硬件设备
- Mount Unit，文件系统的挂载点
- Automount Unit，自动挂载点
- Path Unit，文件或路径
- Scope Unit，不是由 Systemd 启动的外部进程
- Slice Unit，进程组
- Snapshot Unit，Systemd 快照，可以切回某个快照
- Socket Unit，进程间通信的 socket
- Swap Unit，swap 文件
- Timer Unit，定时器

**常用操作**

```sh
# 列出正在运行的Unit
systemctl list-units，可以直接使用systemctl

# 列出所有Unit，包括没有找到配置文件的或者启动失败的
systemctl list-units --all

# 列出所有没有运行的 Unit
systemctl list-units --all --state=inactive

# 列出所有加载失败的 Unit
systemctl list-units --failed

# 列出所有正在运行的、类型为service的Unit
systemctl list-units --type=service

# 显示某个 Unit 是否正在运行
systemctl is-active application.service

# 显示某个 Unit 是否处于启动失败状态
systemctl is-failed application.service

# 显示某个 Unit 服务是否建立了启动链接
systemctl is-enabled application.service

# 立即启动一个服务
sudo systemctl start apache.service

# 立即停止一个服务
sudo systemctl stop apache.service

# 重启一个服务
sudo systemctl restart apache.service

# 重新加载一个服务的配置文件
sudo systemctl reload apache.service

# 重载所有修改过的配置文件
sudo systemctl daemon-reload
```





## iptables和firewalld

centos7以前的版本默认使用iptables服务进行管理防火墙规则。centos7以及其以上版本默认使用firewalld服务管理防火墙。所以在centos8中，就使用其默认的firewalld配置防火墙。

ubuntu系统默认防火墙是UFW（Uncomplicated Firewall），用户管理 iptables 防火墙规则

https://www.cnblogs.com/johnyong/p/13473133.html

[防火墙官方介绍](https://access.redhat.com/documentation/zh-cn/red_hat_enterprise_linux/7/html/security_guide/sec-using_firewalls#sec-Comparison_of_Firewalld_to_system-config-firewall_and_iptables)



## ifconfig

```sh
install net-tools 
```

查看和设置 网卡信息

https://wangchujiang.com/linux-command/c/ifconfig.html

