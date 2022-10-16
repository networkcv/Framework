### mkdir 

mkdir -p /a/b 可以创建多级文件夹



在linux和centos下，我们就直接可以用telnet命令来测试端口是否畅通。具体用法：telnet 指定的IP或者 域名 端口号.

```bash
比如：telnet 192.168.1.102 3306



内网 telnet 127.0.0.1 8090
```

在Mac os 下

```bash
nc -vz -w 2 192.168.1.104 3306
```



目录操作:
	cd		切换目录
	clear		清楚屏幕
	pwd		显示当前目录所在位置
	ls(list)	显示当前目录下的所有目录 -a(r代表all)(显示隐藏目录)/-l
			(显示目录详细信息，ls -l 可以缩写为ll)
	mkdir(make dir)	创建目录
	find		搜索目录 find /root -name '*test*'
	mv(move)	移动改名 mv 旧名称 新名称 mv 目标名 目标新位置
	cp(copy)	复制目标 cp -r(r代表递归)(复制目录) 目标名 要复制到的位置
	rm(remove)	删除目标 rm -r(删除目录) -f(r代表force强制)(跳过询问直接删除)
文件操作:
	touch		创建文件 touch 文件名
	echo 2 >myid	将2输出到myid中，也可以简单创建文件
	cat		显示最后一屏内容	cat	 文件名	 cat 是由第一行到最后一行连续显示在萤幕上，而 tac 则是由最后一行到第一行反向在萤幕上显示出来
	tail	查看文件的后几行	tail -n  -10 文件名	Ctrl+C强制退出
	head	与tail相反 	查看文件的前几行  head -n 10  test.log   查询日志文件中的头10行日志;
	vim		修改文件内容		vim	 文件名
			vim有3种模式，开始是命令 按i进入编辑模式 按Esc返回命令底行模式 保存退出:wq
			保存退出：Shift+zz wq	不保存退出：q!	强制退出：  !	正常退出：  q	Shift+G 快速到文件末尾
	chmod		修改文件权限 chmod u+x 文件名	在当前用户下可以执行   当前用户/当前用户所在组/其他用户
压缩和解压：
	tar		压缩文件	tar -zc(压缩)vf 打包压缩后的文件名 要打包压缩的文件
			解压文件	tar -z(格式为gz)x(解压缩)v(显示过程)f(文件名) xxx.tar.gz -C /usr------C代表指定解压的位置
其他命令：
	grep		grep to(要查的信息) sudo.conf(要查的文件) --color(颜色高亮显示)
	ps		查询进程	ps -ef     
	|		管道符	前面的输出作为后面的输入	例： ps -ef | grep tomcat
	kill		结束进程	kill -9(强制关闭) 进程的pid
	ifconfig	查看当前系统的网卡信息：ifconfig	用户后边的第一列数字是 PID
	ping		查看与某台机器的连接情况：ping
	netstat		查看当前系统的端口使用：netstat -an(windows中是 netstat -aov) 本机地址最后一位是端口号
	chmod		修改权限方法  chmod u=rwx,g=rw,o=r aaa.txt	chmod 764 aaa.txt
	rpm -qa | grep mysql	查询
	rpm -e --nodeps 名称	卸载
	rpm -ivh 软件名		安装

.sh的启动、关闭和查看状态	./zkServer.sh  start/stop/status
关闭防火墙		service iptables stop
开机不启动防火墙	chkconfig iptables off
查看控制台		tail -f tomcat-sina/logs/catalina.out	查看tomcat启动情况	
查看带nginx进程 	ps aux|grep nginx/(pid)	
杀死带nginx进程 	ps aux|grep nginx/(pid)  | xargs kill	
根据进程pid查端口	netstat -nap | grep pid
根据端口port查进程	netstat -nap | grep port
退出telnet		telnet 192.168.60.82 9736  ctrl+] 输入命令  quit
查看启动的控制台	./apache-activemq-5.15.6/bin/activemq console  
查找文件		whereis my.cnf
yum install gcc/ruby	联网下载资源

根据名称查询进程 ps -ef|grep svn
根据端口查进程	netstat -lntup|grep 3690
根据端口查进程	lsof -i :3690
展示目录树 tree /application/svndata/



tail -f error.log	查看文件末尾，可以看到实时输出的内容。