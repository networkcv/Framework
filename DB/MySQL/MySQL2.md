# MySQL

## 锁

### 粒度

- 全局锁

	- 定义

		- 对整个数据库加锁，让整个数据库处于只读状态

	- 命令

		- flush tables with read lock

	- 作用

		- 让整个数据库处于只读状态，数据更新、表字段定义等语句会被阻塞

	- 使用场景

		- 做全局逻辑备份

- 表级锁

	- 表锁

		- 定义

			- 对整张表加锁，除了会限制其它线程的读写，也会限制本线程接下来的操作

		- 命令

			- lock tables ... read/write
			- unlock tables

		- 作用

			- 多用于并发处理

		- 使用场景

			- 在行锁出现之前用来处理并发问题

	- MDL锁

		- 定义

			- MetaData Lock元数据锁

		- 作用

			- 保证读写的正确性，当对一个表做增删改查操作的时候，加MDL读锁；当要对表表结构变更的时候，加MDL写锁
			- 读锁与读锁之间不互斥，因此可以有多个线程同时对一张表增删改查；读写锁、写写锁之间是互斥的，用来保证表结构变更操作的安全性。因此，如果有两个线程同时给一张表做结构变更，其中一个要等另一个执行完才能开始执行。

		- 使用场景

			- MySQL5.5版本引入MDL锁，不需要显示访问，在访问一个表时会被自动加上

- 行锁

	- 定义

		- 针对数据表中行记录的锁，由各个存储引擎自行实现；行锁在事务提交或者回滚后才会释放

	- 作用

		- 当有多条线程更新同一行记录时，未持有锁的事务将会被阻塞，直至持有锁才能继续执行

	- 使用场景

		- InnoDB引擎支持，用于处理并发场景

	- 分类

		- 乐观锁

			- 定义

				- 乐观锁认为自己在使用数据的时候不会有其它线程来修改数据，因此不会添加锁，只在更新时去判断之前是否有其它线程更新了这个数据。

			- 实现方式

				- 版本号机制
				- CAS算法

					- 定义

						- 全称Compare And Swap(比较并替换),是一种无锁算法，可以在不使用锁的情况下实现多线程之间的变量同步

					- 特点

						- 在不使用锁的情况下实现多线程之间的变量同步

					- 使用场景

						- java.util.concurrent包中的原子类就是通过CAS实现的

			- 作用
			- 使用场景

				- 适合读操作多的应用场景，不加锁的特定能够使读操作的性能大幅提升

		- 悲观锁

			- 定义

				- 悲观所认为自己在使用数据的时候会有别的线程来修改数据，所以在获取数据时会显示加上锁，确保数据不会被其它线程修改

			- 作用

				- 并发场景下确保数据的准确性

			- 使用场景

				- 适合写操作比较多的场景，加锁的特点能够保证数据的准确性

### 类型

- 共享锁

	- 定义

		- 同一时刻可以被多个线程同时占有锁资源

	- 作用
	- 使用场景

- 排他锁（独占锁）

	- 定义

		- 同一时刻，只能有一个线程占有锁资源
		- 如果线程t1对数据A加上独占锁后，其它线程将不能再对A加任何类型的锁，获得排他锁的线程既能读数据也能写数据

	- 作用
	- 使用场景

		- Synchronized
		- Lock

## 数据结构

### B树

### B+树

- 每一个索引在InnoDB里对应一颗B+树

### 哈希表

## 日志文件

### undo log(回滚日志)

- 定义

	- undo log是逻辑日志，用来记录数据库表的行信息
	- 默认保存在共享空间表中，位于mysql目录的data子目录的ibdata1文件

- 作用

	- 用于保证事务的原子性和事务并发控制
	- 当事务发生异常或显示回滚时，MySQL使用undo log回滚数据

- 使用场景

	- 当delete一条记录时，undo log里会记录一条insert日志；update时也会记录一条相反的update日志

- 写入与释放

	- undo log在事务刚开始时就会产生，保存事务开始前的数据
	- undo log也会产生redo log
	- mysql5.7 之后有独立的undo 表空间
	- 当事务提交后，undo log并不能被马上删除，而是由purge线程判断是否还有事务在使用undo log记录的信息，以此决定是否释放日志空间。

### redo log(重做日志)

- 定义
- 作用
- 使用场景

### binlog(归档日志)

- 定义
- 作用

	- 用于记录数据库执行的写入操作（不包括查询）信息，以二进制保存在磁盘中；binlog通过追加的方式进行写入，可以通过max_binlog_size参数设置每个binlog文件的大小，当文件大小达到给定值之后，会生成新的文件来保存日志

- 执行流程
- 使用场景

	- 主从复制
	- 数据恢复
	- ES监听

- 日志格式

### general log(通用日志)

### relay log(中继日志)

## 存储引擎

### InnoDB

- 介绍

	- InnoDB是将表数据存储到磁盘上的存储引擎，所以即使服务器重启后数据还是存在的

- 数据处理过程

	- 数据处理的过程发生在内存中，所以需要把磁盘中的数据加载到内存中，如果是处理写入或者修改请求的话，还需要把内存中的内容刷新到磁盘上。但是读写磁盘的速度非常慢，一条一条把记录从磁盘读出来会非常慢，InnoDB采用的方式是：将数据划分为若干个页，以页作为磁盘和内存间交互的基本单位，页的大小一般为16kb，也就是说，一般情况下，一次最少从磁盘中读取16kb的内容到内存中，一次最少把内存中16kb内容刷新到磁盘中。

- InnoDB 行格式

	- 介绍

		- 数据库是以记录为单位来向表中插入数据的，这些记录在磁盘上的存放方式也被称为行格式或者记录格式。设计InnoDB存储引擎的大叔们到现在为止设计了4种不同类型的行格式，分别是Compact、Redundant、Dynamic和Compressed行格式

	- 种类

		- COMPACT行格式

			- 图例

				- 

			- 介绍

				- 一条完整的记录分为 记录的额外信息 和 记录的真是数据 两部分。

			- 组成

				- 记录的额外信息

					- 变长字段长度列表：
					- NULL值列表
					- 记录头信息

				- 记录的真实数据

					- 子主题 1
					- 子主题 2

		- 子主题 2
		- 子主题 3
		- 子主题 4

	- 指定行格式语法

- 数据页结构

	- InnoDB记录结构
	- InnoDB页结构

		- 介绍

			- 数据页是InnoDB管理存储空间的基本单位，一个页的大小一般是16kb。

		- 示意图

			- 

		- 组成

			- File header

				- 文件头，存放数据页的一些通用信息

			- Page Header

				- 页面头部，数据页专有的一些信息

			- Infimum+Supremum

				- 最小记录和最大记录，是两个虚拟的行记录

			- User Records

				- 用户记录，实际存储的行记录内容

			- Free Space

				- 页中尚未使用的空间

			- Page Directory

				- 页中某些记录的相对位置

			- File Trailer

				- 校验页是否完整

		- 记录插入过程

			- 介绍

				- 存储的记录会按照我们指定的行格式存储到User Records部分。但是在一开始生成页的时候，其实并没有User Records这个部分，每当我们插入一条记录，都会从Free Space部分，也就是尚未使用的存储空间中申请一个记录大小的空间划分到User Records部分，当Free Space部分的空间全部被User Records部分替代掉之后，也就意味着这个页使用完了，如果还有新的记录插入的话，就需要去申请新的页了

			- 示意图

				- 

### MyISAM

### MEMORY

## 数据库连接池

### 定义

- 数据库连接池负责分配、管理和释放数据库连接，它允许引用程序重复使用一个现有的数据库连接，而不是重新建立一个。

### 作用

- 在应用启动时建立足够的数据库连接，并将这些连接组成一个连接池，由程序动态的对池中的连接进行申请、使用和释放，提升系统性能
- 减少了每次连接数据库时的tcp连接，减少网络IO消耗

### 流程

- 不使用数据库连接池流程

	- 图例

		- 

	- 步骤

		- 1、TCP建立连接（三次握手）
		- 2、MySQL权限认证
		- 3、SQL执行
		- 4、TCP关闭连接（四次挥手）

- 使用数据库连接池流程

	- 图例

		- 

	- 步骤

		- 1、第一次访问时建立TCP连接
		- 2、SQL执行使用的都是连接池中的连接
		- 3、服务停止时释放连接池

### 工作原理

- 连接池建立

	- 系统初始化时，连接池会根据系统配置建立，并在池中创建几个连接对象，连接池中的连接不能随意创建和关闭，避免随意连接和关闭造成的系统开销

- 连接池管理

	- 首先查看连接池中是否有空闲连接，如果存在空闲连接，则将连接分配给客户使用；如果没有空闲连接，则查看当前所开的连接数是否已经达到最大连接数，如果没达到就重新创建一个连接给请求的客户；如果达到就按设定的最大等待时间进行等待，如果超出最大等待时间，则抛出异常；

- 连接池关闭

	- 当客户端释放数据库连接时，先判断该连接的引用次数是否超过了规定值，如果超过就从连接池中删除该连接，否则保留

### 常用参数（HikariCP）

- 1、autoCommit=true 

	- 连接是否自动提交事务，默认true

- 2、connectionTimeout

	- 连接超时时间，如果超时，将抛出SQLException

- 3、idleTimeout

	- 连接在连接池中闲置多久被抛弃，默认600000 ms

- 4、minimumIdle

	- 最小空闲连接数

- 5、maximumPoolSize

	- 最大连接数

- 6、poolName

	- 连接池名称

### 分类

- HikariCP
- Druid

## 分库分表

### sharding-jdbc

- 定义
- SQL执行流程

	- 图例

		- 

	- 步骤

		- SQL解析

			- 词法解析
			- 语法解析

		- SQL路由

			- sharding-jdbc根据上下文匹配用户对这句SQL所涉及的库和表配置的分片策略（用户配置的分片策略），并根据分片策略生成路由后的SQL。路由后的SQL有一条或多条，每一条都对应着各自真实的物理分片

		- SQL改写

			- 将SQL改写成在真实数据库中可以执行的语句（例如把逻辑表名改为带编号的物理表）

		- SQL执行

			- 通过多线程执行器异步执行改写后得到的 SQL 语句

		- 结果归并

			- 将多个执行结果集归并以便于通过统一的 JDBC 接口输出

- 核心概念

	- 逻辑表和物理表
	- 分片键
	- 路由
	- 分片策略

		- 标准分片策略
		- 复合分片策略
		- Hint分片策略
		- Inline分片策略
		- 不分片策略

	- 分片算法
	- 绑定表

- 使用方式

	- 引入依赖包
	- 水平分片规格配置
	- 创建数据源
	- 执行SQL

## 索引

### 定义

- 索引是用来快速查找特定记录的数据结构，能够快速的确定要查找数据所在数据文件的具体位置而不用扫描所有数据，这比按顺序读取每一行数据快很多

### 模型

- 哈希表

	- 定义

		- 是一种以key-value形式存储数据的结构

	- 原理

		- 底层基于数组+链表实现

	- 适用场景

		- 适用于等值查询的场景，比如一些NoSQL引擎

- 有序数组

	- 定义
	- 原理

		- 底层基于数组实现

	- 适用场景

		- 适用于等值查询和范围查询；适用于静态存储引擎，存的是不会再修改的数据

- 搜索树

	- 二叉搜索树

		- 定义
		- 特点

			- 每个节点的左儿子小于父节点，父节点又小于右儿子

		- 适用场景

	- N叉树

		- 定义
		- 特点
		- 适用场景

### 类型

- 主键索引

	- 是一种特殊的唯一索引，一个表只能有一个主键，不允许空值

- 唯一索引

	-  定义
	- 语句

- 普通索引
- 组合索引

	- 多个字段上创建索引

- 全文索引
- 覆盖索引

## 事务

### 原子性(Atomicity)

- 定义

	- 一个事务是一个不可分割的工作单元，要么全部成功，要么全部失败

- 原理

	- 基于undo log日志实现，当事务对数据库进行修改时，InnoDB会生成对应的undo log；如果事务执行失败回滚时，可以利用undo log中的信息将数据回滚到修改前的状态

### 一致性(Consistency)

- 定义

	- 事务执行结束后，执行前后都是合法的数据状态

- 原理

	- 数据库通过原子性、隔离性、持久性来保证一致性，一致性是目的，AID是手段

### 隔离性(Isolation)

- 定义

	- 事务内部的操作与其它事务是隔离的，并发执行的各个事务之间不能互相干扰

- 隔离级别

	- 读未提交

		- 事务在执行过程中可以看到其它事务未提交的更新的记录，会造成脏读

	- 读已提交

		- 事务在执行过程中可以看到其它事务已提交的更新的记录，会造成不可重复读

	- 可重复读（InnoDB默认）

		- 一个事务在执行过程中可以看到其它事务已经提交的新插入的记录，但是不能看到其它事务对已有记录的更新，会造成幻读
		- 幻读case

			- 事务A开启事务，事务B开启事务，事务A查询数据data不存在，事务B插入数据data，事务B 提交事务，事务A查询数据data不存在，事务A插入数据data失败

	- 串行化

		- 事务在执行过程中完全看不到其它事务对数据库做的更新，并发度低

- 原理
- 作用
- 间隙锁

	- 定义

		- 

	- 作用

		- 在可重复读级别下为了解决幻读问题引入的锁机制

	- 使用场景

- MVCC

### 持久性(Durability)

- 定义

	- 事务一旦提交，对数据库的改变是永久性的

- 原理

	- 基于redo log实现，当事务对数据库进行修改时，会在redo log中记录这次操作，当事务提交时，会将日志写入磁盘中

- 作用

	- 当数据库服务器发生宕机时，会将redo log中的内容恢复到数据库中，以保证数据的持久性

## 基础架构

### 图例

- 

### 连接器

- 管理连接，权限验证

### 分析器

- 词法分析，语法分析

### 优化器

- 生成执行计划，索引选择

	- 索引选择

		- 索引的行数
		- 是否回表操作
		- 是否使用临时表
		- 是否使用排序或者文件排序

### 执行器

- 操作存储引擎，返回结果

### 存储引擎

- 存储数据，提供读写接口

