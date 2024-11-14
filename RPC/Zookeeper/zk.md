# 基础操作

## 创建

create [-s] [-e] path data acl 

-s -e 指的节点特性，分别是顺序节点和临时节点，不指定默认持久节点

acl 是权限控制



## 读取

ls path  查看节点目录

```
ls /
[zookeeper] 
```

第一次部署，默认在根结点下有个一个zookeeper的保留节点。

get  path 查看节点信息

```
localhost:2181	$	get /test
测试数据
cZxid = 0x102
ctime = Thu Nov 14 15:56:24 CST 2024
mZxid = 0x102
mtime = Thu Nov 14 15:56:24 CST 2024
pZxid = 0x102
cversion = 0
dataVersion = 0
aclVersion = 0
ephemeralOwner = 0x0
dataLength = 1
numChildren = 0
```

从上面的输出信息中，我们可以看到，第一行是节点/test 的数据内容，其他几行则是创建该节点的事务 ID （cZxid）、最后一次更新该节点的事务 ID（mZxid）和最后一次更新该节点的时间（mtime）等属性信息。

## 更新

set path data [version]

vesion 可不传，传了的话类似于乐观锁，进行CAS



## 删除

delete path 删除指定节点，如果存在子节点则无法删除

deleteall  path 删除路径下所有节点

```bash
deleteall /zookeeper
```