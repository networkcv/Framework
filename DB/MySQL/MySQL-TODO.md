

## 存储引擎

### MyISAM

5.5版之前MyISAM是MySQL的默认数据库引擎。虽然查询性能极佳，而且提供了大量的特性，包括全文索引、压缩、空间函数等，但MyISAM不支持事务和行级锁，而且最大的缺陷就是不能在表损坏后恢复数据。MyISAM管理的数据文件frm(表格式),MYD(数据),MYI(索引)

### InnoDB

5.5版本后默认的存储引擎为InnoDB，支持事务；锁机制改进，支持行级锁；实现外键，InnoDB存储引擎管理的数据文件分别为frm(表格式),idb(数据+索引)  

可以数据多引擎读取，因为存储引擎建立在表之上，所以可以使用InnoDB类型和MyISAM类型的多表查询
大多数时候我们使用的都是 InnoDB 存储引擎，但是在某些情况下使用 MyISAM 也是合适的比如读密集的情况下。

### InnoDB和MyISAM区别

- **是否支持事务：** InnoDB支持事务，MyISAM不支持事务，对于InnoDB每一条SQL语句都默认封装成事务，自动提交，所以最好把多条SQL语句放在begin transaction 和commit之间组成一个事务
- **是否支持行级锁：** MyISAM只支持表级锁，而InnoDB支持行级锁和表级锁，默认行级锁
- **是否支持外键:** InnoDB支持外键，而MyISAM不支持，对于一个包含外键的InnoDB表转为MyISAM会失败
- **是否支持崩溃后的安全恢复：**  MyISAM 强调的是性能，每次查询具有原子性,其执行速度比InnoDB类型更快，但是不提供崩溃后的安全恢复支持。但是InnoDB 提供事务支持。 具有事务(commit)、回滚(rollback)和崩溃修复能力(crash recovery capabilities)的事务安全(transaction-safe (ACID compliant))型表。
- 索引实现方式的区别： InnnoDB是聚簇索引，数据文件和索引绑在一起，必须要有主键，通过主键索引效率很高，但是辅助索引需要两次查询，先查主键，再通过主键查询到数据，主键建议不要太大，而MyISAM是非聚簇索引，数据和文件分离的，索引保存的是数据文件的地址
- 在统计表行数的区别： InnoDB不保存表的具体行数，执行select count(*) from table时进行全表扫描，而MyISAM通过变量来保持表行数，执行上述语句只需要读取该变量即可

## 索引

MySQL索引使用的数据结构主要有BTree索引和哈希索引。

### 哈希索引

底层的数据结构就是哈希表，因此在绝大多数需求为单条记录查询的时候，可以选择哈希索引，查询性能最快；其余大部分场景，建议选择BTree索引。

### BTree索引

不同的存储引擎的实现方式不同：

- **MyISAM:** B+Tree叶节点的data域存放的是数据记录的地址。在索引检索的时候，首先按照B+Tree搜索算法搜索索引，如果指定的Key存在，则取出其 data 域的值，然后以 data 域的值为地址读取相应的数据记录。这被称为非聚簇索引
- **InnoDB:** 其数据文件本身就是索引文件。相比MyISAM，索引文件和数据文件是分离的，其表数据文件本身就是按B+Tree组织的一个索引结构，树的叶节点data域保存了完整的数据记录。这个索引的key是数据表的主键，因此InnoDB表数据文件本身就是主索引。这被称为“聚簇索引（或聚集索引）”。而其余的索引都作为辅助索引，辅助索引的data域存储相应记录主键的值而不是地址，这也是和MyISAM不同的地方。在根据主索引搜索时，直接找到key所在的节点即可取出数据；在根据辅助索引查找时，则需要先取出主键的值，再走一遍主索引。 因此，在设计表的时候，不建议使用过长的字段作为主键，也不建议使用非单调的字段作为主键，这样会造成主索引频繁分裂

### 适合创建索引的情况

- 搜索的列上,可以加快搜索的速度
- 在作为主键的列上,强制该列的唯一性和组织表中数据的排列结构
- 用在连接的列上,这些列主要是一些外键,可以加快连接的速度
- 根据范围进行搜索的列上创建索引,因为索引已经排序,其指定的范围是连续的
- 排序的列上创建索引,因为索引已经排序,这样查询可以利用索引的排序,加快排序查询时间
- 使用在WHERE子句中的列上面创建索引,加快条件的判断速度。建立索引,一般按照select的where条件来建立,比如: select的条件是where f1 and f2,那么如果我们在字段1或字段2上建立索引是没有用的,只有在字段1和2上同时建立索引才有用等。

### 索引的操作

创建索引  
CREATE INDEX index_name ON table_name(column(length))  
查看索引  
SHOW IDNEX FROM table_name  
删除索引  
DROP INDEX index_name ON table_name  


## 事务

### 事务的四个特性ACID

**原子性**

**一致性**

**隔离性**

**持久性**

### 并发事务带来哪些问题

**脏读**：B事务中读到了A事务中修改了但没提交的数据。

**不可重复读**：B需要在事务中进行两次查询，A事务在B的两次查询中间进行 **更新、插入或删除了某条记录** 并提交事务，因此B在同一事务中会读到两次不同的结果。

**幻读**：指的是当某个事务在读取某个范围内的记录时，另一个事务又在该范围内插入了新的记录，当之前的事务再次读取该范围的记录时，会产生幻行。B需要在事务中进行两次查询，A事务在B的两次查询中间进行 **插入或删除 某条记录**并提交事务，因此B在同一事务中相同的sql 读取到的行数不一致，因此在插入的时候可能会出现主键重复的问题。

不可重复读指的是update操作，而幻读指的是insert或delete操作。

### 事务的四种隔离级别

```sql
set session transaction isolation level Read Committed;
start tansaction;
...
commit;
```

**Read Uncomitted** 未提交读：事务中的修改，即使没有提交，对其他事务也都是可见的。

**Read Committed** 提交读：大多数数据库系统的默认隔离级别都是READ COMMTTED，但MySQL不是，Mysql的默认隔离级别是Repeatable Read。

**Repeatable Read** 可重复读：该隔离级别保证了在同一个事务中多次读取同样记录结果是一致的。解决了不可重复读的问题，但是无法解决幻读的问题。Mysql的RR是由“行排它锁+MVCC”一起实现的。

**Serializable** 串行化：最高的隔离级别。它通过强制事务串行执行，避免了前面说的幻读的问题。简单来说，SERIALIZABLE会在读取每一行数据都加锁，所以可能导致大量的超时和锁争用问题。实际应用中也很少用到这个隔离级别，只有在非常需要确保数据的一致性而且可以接受没有并发的情况下，才考虑采用该级别。

## 事务原理

### MVCC

**读锁：**也叫共享锁、S锁，若事务T对数据对象A加上S锁，则事务T可以读A但不能修改A，其他事务只能再对A加S锁，而不能加X锁，直到T释放A上的S 锁。这保证了其他事务可以读A，但在T释放A上的S锁之前不能对A做任何修改。

**写锁：**又称排他锁、X锁。若事务T对数据对象A加上X锁，事务T可以读A也可以修改A，其他事务不能再对A加任何锁，直到T释放A上的锁。这保证了其他事务在T释放A上的锁之前不能再读取和修改A。

**表锁：**操作对象是数据表。Mysql大多数锁策略都支持，是系统开销最低但并发性最低的一个锁策略。事务t对整个表加读锁，则其他事务可读不可写，若加写锁，则其他事务增删改都不行。 

**行级锁：**操作对象是数据表中的一行。是MVCC技术用的比较多的。行级锁对系统开销较大，但处理高并发较好。

MVCC使得大部分支持行锁的事务引擎，不再单纯的使用行锁来进行数据库的并发控制，取而代之的是把数据库的行锁与行的多个版本结合起来，只需要很小的开销,就可以实现非锁定读，从而大大提高数据库系统的并发性能。

## 锁机制与InnoDB锁算法

MyISAM采用表级锁
InnoDB采用行级锁和表级锁，默认行级锁

### 表级锁和行级锁的对比：

- 表级锁：MySQL中锁定粒度最大的一种锁，对当前操作的整张表枷锁，实现简单，加锁快，不会出现死锁，但汽车锁定粒度最大，触发锁冲突的概率最高，并发度最低
- 行级锁：MySQL中锁定粒度最小的一种锁，只针对当前操作行进行枷锁，行级锁大大减少数据库操作的冲突，其加锁粒度最小，并发度高，所以资源消耗也大，加锁慢，会出现死锁

### InnoDB存储引擎的算法有三种

- Record lock ：单个行记录上的锁
- Gap lock ：间隙锁，锁定一个范围，不包括记录本身
- Next-key lock ： record+gap 锁定一个范围，包含记录本身

## 数据库优化

### 读写分离

主库负责写，从库负责读，这种集群方式的本质是把数据库访问的压力从主库转移到从库上，不适合写操作密集的情况。做主从之后可以单独对从库做索引优化，主库可以减少索引提高写效率

#### 主从同步延迟

在主库有数据写入后，同时也写入binlog(二进制日志文件)中，从库是通过binlog文件来同步数据的，期间可能有一定时间的延迟，如果数据量大的话，时间可能更长  

解决办法：关键业务读写都由主库承担，非关键业务读写分离  
类似付钱的业务，读写都到主库，避免延迟的问题，但例如该个头像等业务比较不重要的业务采用读写分离

#### 分配机制

分配机制，怎么制定写操作是去主库写，读操作是去从库读  

1.代码封装  
在代码中抽出一个中间层，让这个中间层来实现读写分离和数据库连接，提供一个provider，封装了常用数据库操作，save操作的dataSource是主库的，select操作的dataSource是从库的  
优点:实现简单，可以根据业务定制变化  
缺点：如果那个数据库宕机，发生主从切换后，需要修改配置重启  

2.数据库中间件  
数据库中间件是个独立的系统，专门实现读写分离和数据库连接管理，业务服务器和数据库中间件之间通过标准的SQL协议交流，在业务服务器看来数据库中间件其实就是个数据库  
优点：有中间件来实现主从切换，业务服务器不需要关心  
缺点：多了一个系统等于多了一个瓶颈，因为所有的数据库操作都要经过它，所以对中间件的性能要求也很高，有开源数据库中间件,如MySQL Proxy，MySQL Route  

####  读写分离小结

读写分离只能分担访问读的压力，无法分担存储的压力，一般先优化慢查询，优化业务逻辑或加入缓存，如果都不行再考虑读写分离，之后才是分库分表

### 分库分表

#### 垂直分区

根据数据库表的相关性进行拆分，如用表中既有用户登录信息又有用户的基本信息，可以将用户表拆分为两个单独的表，或者放到单独的库做分库，通俗来说就是将一张大表拆分为两张小表  
优点：  

- 使得行数据变小，一个数据库(BLock)能存放更多数据，在查询的时候就会减少I/O次数
- 可以最大化利用Cache，具体在垂直拆分的时候可以将不常变的字段放一起，将经常改变的放一起
- 可以简化表结构，易于维护

缺点：  

- 主键会出现冗余，需要管理冗余列
- 并会引起JOIN操作，增加CPU开销，可以通过在业务服务器上进行JOIN来减少数据库压力
- 事务处理更加复杂

#### 水平分区

保持数据表结构不变，通过某种策略存储数据分片，这样每一片数据分散到不同的表或库中，达到分布式的效果，通俗来说就是将一张300万行的表分成3个100万行的表放在不同的数据库存储，避免数据量过大对数据库性能的影响  
优点：  

- 水平分区可以支撑非常大的数据量，但前提是把数据分布到不同的机器上，不然在同一台机器上分表，竞争同一个物理机上的IO、CPU、网络，对提升MySQL的并发没有什么意义
- 应用端的改造很少
- 提升了系统的稳定性和负载能力

缺点：  

- 分片事务一致性难以解决
- 跨节点JOIN性能差，逻辑复杂
- 数据多次扩展难度和维护量极大

#### 数据库分片的两种常见方案

- 客户端代理：分片逻辑在应用端，封装在jar包中，通过修改或封装JDBC层来实现，如当当网的Sharding-JDBC
- 中间件代理：在业务层和数据库中间加了一个代理层，分片逻辑统一维护在中间件服务中，该服务需要独立部署，也可以多台部署，Mycat就是这种架构的实现

#### 水平分区小结

尽量不要对数据进行水平分片，因为拆分提升逻辑、部署、运维的各种复杂度，一般的数据表在优化得当的情况下支撑千万以下的数据量是没有多大问题的，如果，必须要做分片，尽量选择客户端分片架构，这样可以减少一次和中间件的网络IO

### 字段

- 尽量使用TINYINT、SMALLINT、MEDIUM_INT作为整数类型而非INT，如果非负则加上UNSIGNED
- VARCHAR的长度只分配真正需要的空间
- 使用枚举或整数代替字符串类型，如性别字段用0，1代替'男''女'
- 尽量使用TIMESTAMP而非DATETIME，
- 单表不要有太多字段，建议在20以内
- 避免使用NULL字段，很难查询优化且占用额外索引空间
- 用整型来存IP

### 索引

- 索引并不是越多越好，要根据查询有针对性的创建，考虑在WHERE和ORDER BY命令上涉及的列建立索引，可根据- EXPLAIN来查看是否用了索引还是全表扫描
- 应尽量避免在WHERE子句中对字段进行NULL值判断，否则将导致引擎放弃使用索引而进行全表扫描
- 值分布很稀少的字段不适合建索引，例如"性别"这种只有两三个值的字段
- 字符字段只建前缀索引
- 字符字段最好不要做主键
- 不用外键，在逻辑上实现外键
- 尽量不用UNIQUE，由程序保证约束
- 使用多列索引时注意顺序和查询条件保持一致，同时删除不必要的单列索引

### 查询SQL

- 可通过开启慢查询日志来处理较慢的SQL
- 不做列运算：任何对列的操作都将导致表扫描，它包括数据库教程函数、计算表达式等等，如 SELECT id WHERE age + 1 = 10， select * from user where YEAR(date)<2019 可以改为 select * from user where date<'2019-01-01',
- sql语句尽可能简单：一条sql只能在一个cpu运算；大语句拆小语句，减少锁时间；一条大sql可以堵死整个库
- 不用SELECT *
- OR改写成IN：OR的效率是n级别，IN的效率是log(n)级别，in的个数建议控制在200以内
- 不用函数和触发器，在应用程序实现
- 少用JOIN
- 使用同类型进行比较，比如用'123'和'123'比，123和123比
- 尽量避免在WHERE子句中使用!=或<>操作符，否则将引擎放弃使用索引而进行全表扫描
- 对于连续数值，使用BETWEEN不用IN：SELECT id FROM t WHERE num BETWEEN 1 AND 5
- 列表数据不要拿全表，要使用LIMIT来分页，每页数量也不要太大
- 不建议使用like操作,like '%aaa%' 不会使用索引，like 'aaa%'会使用索引



### 缓存

缓存可以发生在这些层次：

- MySQL内部：在系统调优参数介绍了相关设置
- 数据访问层：比如MyBatis针对SQL语句做缓存，而Hibernate可以精确到单个记录，这里缓存的对象主要是持久化对象Persistence Object
- 应用服务层：这里可以通过编程手段对缓存做到更精准的控制和更多的实现策略，这里缓存的对象是数据传输对象Data Transfer Object
- Web层：针对web页面做缓存
- 浏览器客户端：用户端的缓存

可以根据实际情况在一个层次或多个层次结合加入缓存。这里重点介绍下服务层的缓存实现，目前主要有两种方式：

- 直写式（Write Through）：在数据写入数据库后，同时更新缓存，维持数据库与缓存的一致性。这也是当前大多数应用缓存框架如Spring Cache的工作方式。这种实现非常简单，同步好，但效率一般。
- 回写式（Write Back）：当有数据要写入数据库时，只会更新缓存，然后异步批量的将缓存数据同步到数据库上。这种实现比较复杂，需要较多的应用逻辑，同时可能会产生数据库与缓存的不同步，但效率非常高。

## 其他

sql本身优化
    使用关联查询，而不是使用关联子查询
反范式化设计
    反范式是针对范式化设计
    允许存在少量冗余，以空间换时间
物理设计优化
    为表中字段选择合适的数据类型
        1.优先考虑数字类型
        2.其次日期，时间类型
        3.其次是字符串
        4.相同数据类型，优先考虑占用空间小的 比如char代替varchar varchar比char多两个字节
        

​    

三大范式
    第一大范式：字段含义不重复，所有字段都只具有单一属性,不能出现name-age字段
    第二大范式：主键内容不重复
    第三大范式：减少冗余，该分表分表



索引
    索引本质是数据结构

    Innodb  聚集
    testdemo.frm 
    
    范围条件放最后
    索引不能乱加
    执行计划  ex+sql
    possible_keys 可能为索引    key 确实为索引  key_len 索引长度 为

存储引擎
    MyISAM是MySQL的默认数据库引擎（5.5版之前）。虽然性能极佳，而且提供了大量的特性，包括全文索引、压缩、空间函数等，但MyISAM不支持事务和行级锁，而且最大的缺陷就是崩溃后无法安全恢复。不过，5.5版本之后，MySQL引入了InnoDB（事务性数据库引擎），MySQL 5.5版本后默认的存储引擎为InnoDB。

    大多数时候我们使用的都是 InnoDB 存储引擎，但是在某些情况下使用 MyISAM 也是合适的比如读密集的情况下。（如果你不介意 MyISAM 崩溃回复问题的话）。

索引
    MySQL索引使用的数据结构主要有BTree索引 和 哈希索引 。对于哈希索引来说，底层的数据结构就是哈希表，
    因此在绝大多数需求为单条记录查询的时候，可以选择哈希索引，查询性能最快；其余大部分场景，建议选择BTree索引。

    MySQL的BTree索引使用的是B树中的B+Tree，但对于主要的两种存储引擎的实现方式是不同的。
    
        MyISAM: 
            非聚簇索引：5.5版本之前的数据库存储引擎MyISAM中BTree索引在实现时，树节点的data域存放的是数据记录的地址，
            通过key取出data中的地址，然后通过地址读取到相应的数据记录，这样的BTree索引被称为 非聚簇索引
    
            （B+Tree叶节点的data域存放的是数据记录的地址。在索引检索的时候，首先按照B+Tree搜索算法搜索索引，
            如果指定的Key存在，则取出其 data 域的值，然后以 data 域的值为地址读取相应的数据记录。这被称为“非聚簇索引”。）
    
        InnoDB:
            聚簇索引：5.5版本之后的数据库存储引擎InnoDB在实现时，将数据记录直接保存在树节点的data中，
            这个索引的key为记录的主键，InnoDB表数据文件本身就是主索引，相比于MyISAM的非聚簇索引将索引文件和数据文件是分离，
    
            （其数据文件本身就是索引文件。相比MyISAM，索引文件和数据文件是分离的，其表数据文件本身就是按B+Tree组织的一个索引结构，
            树的叶节点data域保存了完整的数据记录。这个索引的key是数据表的主键，因此InnoDB表数据文件本身就是主索引。这被称为“聚簇索引（或聚集索引）”。
            而其余的索引都作为辅助索引，辅助索引的data域存储相应记录主键的值而不是地址，这也是和MyISAM不同的地方。
            在根据主索引搜索时，直接找到key所在的节点即可取出数据；在根据辅助索引查找时，则需要先取出主键的值，再走一遍主索引。 
            因此，在设计表的时候，不建议使用过长的字段作为主键，也不建议使用非单调的字段作为主键，这样会造成主索引频繁分裂。）

事物的四大特性(ACID)
    1.原子性（Atomicity）： 事务是最小的执行单位，不允许分割。事务的原子性确保动作要么全部完成，要么完全不起作用；
    2.一致性（Consistency）： 执行事务前后，数据保持一致，多个事务对同一个数据读取的结果是相同的；
    3.隔离性（Isolation）： 并发访问数据库时，一个用户的事务不被其他事务所干扰，各并发事务之间数据库是独立的；
    4.持久性（Durability）： 一个事务被提交之后。它对数据库中数据的改变是持久的，即使数据库发生故障也不应该对其有任何影响。

并发事务带来哪些问题?
    在典型的应用程序中，多个事务并发运行，经常会操作相同的数据来完成各自的任务（多个用户对同一数据进行操作）。并发虽然是必须的，但可能会导致以下的问题。

    脏读（Dirty read）:
        A读到了B修改但未提交的数据
        当一个事务正在访问数据并且对数据进行了修改，而这种修改还没有提交到数据库中，这时另外一个事务也访问了这个数据，然后使用了这个数据。因为这个数据是还没有提交的数据，那么另外一个事务读到的这个数据是“脏数据”，依据“脏数据”所做的操作可能是不正确的。
    丢失修改（Lost to modify）:
        指在一个事务读取一个数据时，另外一个事务也访问了该数据，那么在第一个事务中修改了这个数据后，第二个事务也修改了这个数据。这样第一个事务内的修改结果就被丢失，因此称为丢失修改。 例如：事务1读取某表中的数据A=20，事务2也读取A=20，事务1修改A=A-1，事务2也修改A=A-1，最终结果A=19，事务1的修改被丢失。
    不可重复读（Unrepeatableread）: 
        A在一个事务内两次读取之间B修改了数据
        指在一个事务内多次读同一数据。在这个事务还没有结束时，另一个事务也访问该数据。那么，在第一个事务中的两次读数据之间，由于第二个事务的修改导致第一个事务两次读取的数据可能不太一样。这就发生了在一个事务内两次读到的数据是不一样的情况，因此称为不可重复读。
    幻读（Phantom read）: 
        A在一个事务内两次读取之间B新加了数据
        幻读与不可重复读类似。它发生在一个事务（T1）读取了几行数据，接着另一个并发事务（T2）插入了一些数据时。在随后的查询中，第一个事务（T1）就会发现多了一些原本不存在的记录，就好像发生了幻觉一样，所以称为幻读。
    
    不可重复读和幻读区别：
    
        不可重复读的重点是修改比如多次读取一条记录发现其中某些列的值被修改，幻读的重点在于新增或者删除比如多次读取一条记录发现记录增多或减少了。

事务隔离级别有哪些?MySQL的默认隔离级别是?
    SQL 标准定义了四个隔离级别： 

    READ-UNCOMMITTED(读取未提交)： 最低的隔离级别，允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读。
    READ-COMMITTED(读取已提交)： 允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生。
    REPEATABLE-READ(可重复读)： 对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生。
    SERIALIZABLE(可串行化)： 最高的隔离级别，完全服从ACID的隔离级别。所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。
    
    MySQL InnoDB 存储引擎的默认支持的隔离级别是 REPEATABLE-READ（可重读）
    InnoDB 存储引擎在 分布式事务 的情况下一般会用到 SERIALIZABLE(可串行化) 隔离级别。

大表优化
当MySQL单表记录数过大时，数据库的CRUD性能会明显下降，一些常见的优化措施如下：

1. 限定数据的范围
   务必禁止不带任何限制数据范围条件的查询语句。比如：我们当用户在查询订单历史的时候，我们可以控制在一个月的范围内；

2. 读/写分离
   经典的数据库拆分方案，主库负责写，从库负责读；

3. 垂直分区
   根据数据库里面数据表的相关性进行拆分。 例如，用户表中既有用户的登录信息又有用户的基本信息，可以将用户表拆分成两个单独的表，甚至放到单独的库做分库。

简单来说垂直拆分是指数据表列的拆分，把一张列比较多的表拆分为多张表。 如下图所示，这样来说大家应该就更容易理解了。 数据库垂直分区

垂直拆分的优点： 可以使得列数据变小，在查询时减少读取的Block数，减少I/O次数。此外，垂直分区可以简化表的结构，易于维护。
垂直拆分的缺点： 主键会出现冗余，需要管理冗余列，并会引起Join操作，可以通过在应用层进行Join来解决。此外，垂直分区会让事务变得更加复杂；

4. 水平分区
   保持数据表结构不变，通过某种策略存储数据分片。这样每一片数据分散到不同的表或者库中，达到了分布式的目的。 水平拆分可以支撑非常大的数据量。

水平拆分是指数据表行的拆分，表的行数超过200万行时，就会变慢，这时可以把一张的表的数据拆成多张表来存放。举个例子：我们可以将用户信息表拆分成多个用户信息表，这样就可以避免单一表数据量过大对性能造成影响。

数据库水平拆分

水平拆分可以支持非常大的数据量。需要注意的一点是：分表仅仅是解决了单一表数据过大的问题，但由于表的数据还是在同一台机器上，其实对于提升MySQL并发能力没有什么意义，所以 水平拆分最好分库 。

水平拆分能够 支持非常大的数据量存储，应用端改造也少，但 分片事务难以解决 ，跨节点Join性能较差，逻辑复杂。《Java工程师修炼之道》的作者推荐 尽量不要对数据进行分片，因为拆分会带来逻辑、部署、运维的各种复杂度 ，一般的数据表在优化得当的情况下支撑千万以下的数据量是没有太大问题的。如果实在要分片，尽量选择客户端分片架构，这样可以减少一次和中间件的网络I/O。

下面补充一下数据库分片的两种常见方案：

客户端代理： 分片逻辑在应用端，封装在jar包中，通过修改或者封装JDBC层来实现。 当当网的 Sharding-JDBC 、阿里的TDDL是两种比较常用的实现。
中间件代理： 在应用和数据中间加了一个代理层。分片逻辑统一维护在中间件服务中。 我们现在谈的 Mycat 、360的Atlas、网易的DDB等等都是这种架构的实现。

SQL explain
    https://www.cnblogs.com/xuanzhi201111/p/4175635.html

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
  -  语句

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





## 关联查询

初始化数据

```sql
CREATE TABLE `age` (
  `id` int DEFAULT NULL,
  `age` int DEFAULT NULL,
  KEY `i_age` (`age`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

INSERT INTO `test`.`age` (`id`, `age`) VALUES (1, 11);
INSERT INTO `test`.`age` (`id`, `age`) VALUES (2, 12);
INSERT INTO `test`.`age` (`id`, `age`) VALUES (3, 13);
INSERT INTO `test`.`age` (`id`, `age`) VALUES (5, 15);

CREATE TABLE `users` (
  `id` int DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  KEY `i_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

INSERT INTO `test`.`users` (`id`, `name`) VALUES (1, 'lwj');
INSERT INTO `test`.`users` (`id`, `name`) VALUES (2, 'lws');
INSERT INTO `test`.`users` (`id`, `name`) VALUES (3, 'wb');
INSERT INTO `test`.`users` (`id`, `name`) VALUES (4, 'bob');
```



```sql
SELECT * from age;
SELECT * from users;



SELECT * from users u INNER JOIN age a on u.id=a.id;
SELECT * from users u ,age a where u.id=a.id;

SELECT * from users u LEFT JOIN age a on u.id=a.id;
SELECT * from users u LEFT OUTER JOIN age a on u.id=a.id;



-- error mysql 不支持
SELECT * from users u FULL JOIN age a on u.id=a.id; 

SELECT * from users u LEFT JOIN age a on u.id=a.id
UNION 
SELECT * from users u RIGHT JOIN age a on u.id=a.id;
```



**inner join(内连接）、left join(左连接)、right join（右连接）、full join（全连接)区别**

```sql
SELECT ... FROM tableA ??? JOIN tableB ON tableA.column1 = tableB.column2;
```

我们把tableA看作左表，把tableB看成右表。

那么INNER JOIN是选出两张表都存在的记录：

![inner-join](img/MySQL-TODO/l-20240618101748561.png)

LEFT OUTER JOIN是选出左表存在的记录：

![left-outer-join](img/MySQL-TODO/l-20240618101748544.png)

RIGHT OUTER JOIN是选出右表存在的记录：

![right-outer-join](img/MySQL-TODO/l-20240618101858339.png)

FULL OUTER JOIN则是选出左右表都存在的记录：

顾名思义，把两张表的字段都查出来，没有对应的值就显示null，但是注意：mysql是没有全外连接的(mysql中没有full outer join关键字)，想要达到全外连接的效果，可以使用union关键字连接左外连接和右外连接

![full-outer-join](img/MySQL-TODO/l-20240618101902486.png)





**在 inner join 的情况下，结果是一样的。**

**在 left join / right join 情况下 ，on 的过滤只对右边表有效 ，where 则会对 join 之后的所有记录再进行过滤。**

join过程可以这样理解：首先两个表做一个笛卡尔积，on后面的条件是对这个笛卡尔积做一个过滤形成一张临时表，如果没有where就直接返回结果，如果有where就对上一步的临时表再进行过滤。下面看实验：

拿 left join 来说，由于left join 的特性，一定会将 left join 左边表的所有记录显示出来，而left join 右边表的记录可以通过  on 来过滤（左边表的记录使用on过滤没有效果）。

下面结合sql和执行结果看一下：

- 没有使用 on 对右边表进行过滤， 注意看sql中【】的内容。

```mysql
select u.name ,u.uid ,r.rtid ,r.sleep_avg_heartrate heartrate ,r.sleep_avg_breatherate breatherate,r.oxygen,r.szy,r.ssy
from  user u left join report r on 【u.uid =r.uid 】group by u.uid;
```

![](../../../../../.Trash/img\1583202054(1).jpg)

- 使用 on 对右边表进行过滤，可以看到相对应的字段的值都变成了null。

```mysql
select u.name ,u.uid ,r.rtid ,r.sleep_avg_heartrate heartrate ,r.sleep_avg_breatherate breatherate,r.oxygen,r.szy,r.ssy
from  user u left join report r on 【u.uid =r.uid and r.uid!=4 】group by u.uid;
```

![](../../../../../.Trash/\img\1583201828(1).jpg)

- 在使用 on 对右边表进行过滤的基础上，同时也对左边表进行过滤，发现并没有什么效果。

```sql
select u.name ,u.uid ,r.rtid ,r.sleep_avg_heartrate heartrate ,r.sleep_avg_breatherate breatherate,r.oxygen,r.szy,r.ssy
    from  user u left join report r on 【u.uid =r.uid and r.uid!=4 and u.uid!=4 】group by u.uid;
```

![](../../../../../.Trash/\img\1583201828(1).jpg)

- 在前边使用left join 的基础上，再加上 where 进行过滤。

```sql
select u.name ,u.uid ,r.rtid ,r.sleep_avg_heartrate heartrate ,r.sleep_avg_breatherate breatherate,r.oxygen,r.szy,r.ssy
    from  user u left join report r on 【u.uid =r.uid  and r.uid!=4 where u.uid=4 】group by u.uid;
```

![](../../../../../.Trash/\img\1583202935(1).jpg)



### Left join  多个表

在 SQL 中，可以通过 `LEFT JOIN` 来连接多个表。假设有三个表 A、B、C，它们之间的关系如下：

```
复制代码A (a_id, a_name)
B (b_id, b_name, a_id)
C (c_id, c_name, b_id)
```

其中 B 表和 C 表都有一个外键与 A 表关联，现在要查询 A 表中的所有记录以及与其关联的 B 表和 C 表的数据，可以使用如下语句：

```
sql复制代码SELECT A.*, B.*, C.*
FROM A
LEFT JOIN B ON A.a_id = B.a_id
LEFT JOIN C ON B.b_id = C.b_id;
```

这条 SQL 语句使用了两次 `LEFT JOIN`，首先将 A 表和 B 表连接起来，然后再将 B 表和 C 表连接起来。这样就可以得到 A 表、B 表和 C 表的所有记录，其中 A 表的记录会在 B 表和 C 表之前显示出来。

需要注意的是，如果在 `LEFT JOIN` 中涉及到了多个表，那么连接的顺序会影响查询结果。在上述例子中，先将 A 表和 B 表连接再将 B 表和 C 表连接会得到不同于先将 B 表和 C 表连接再将 A 表和 B 表连接的结果。因此，在编写含有多个表的 SQL 查询语句时，需要仔细考虑连接的顺序。

在 SQL 中，`LEFT JOIN`（左连接）、`RIGHT JOIN`（右连接）、`INNER JOIN`（内连接）、`FULL JOIN`（全连接）和 `CROSS JOIN`（交叉连接）是不同类型的连接操作，用于将两个或多个表中的行组合起来生成结果集。下面我将解释每种连接的区别，并提供一个具体的数据例子。

## 五种关联查询

### INNER JOIN（内连接）

返回两个表中有匹配的行。

**例子**：

```sql
Table1:
+----+------+
| id | name |
+----+------+
|  1 | A    |
|  2 | B    |
|  3 | C    |
+----+------ +

Table2:
+----+------+
| id | value |
+----+------+
|  1 | X    |
|  2 | Y    |
|  4 | Z    |
+----+------ +

SELECT Table1.id, Table1.name, Table2.value
FROM Table1 INNER JOIN Table2 ON Table1.id = Table2.id;
+----+------+-------+
| id | name | value |
+----+------+-------+
|  1 | A    | X     |
|  2 | B    | Y     |
+----+------+-------+
```

### LEFT JOIN（左连接）

返回左表（Table1）的所有行，即使右表（Table2）中没有匹配的行。

**例子**：

```sql
SELECT Table1.id, Table1.name, Table2.value
FROM Table1 LEFT JOIN Table2 ON Table1.id = Table2.id;
+----+------+-------+
| id | name | value |
+----+------+-------+
|  1 | A    | X     |
|  2 | B    | Y     |
|  3 | C    | NULL  |
+----+------+-------+
```

### RIGHT JOIN（右连接）

返回右表（Table2）的所有行，即使左表（Table1）中没有匹配的行。在大多数 SQL 数据库中，`RIGHT JOIN` 不太常用，因为可以通过将表的位置互换来模拟。

**例子**：

```sql
SELECT Table1.id, Table1.name, Table2.value
FROM Table1 RIGHT JOIN Table2 ON Table1.id = Table2.id;
+----+------+------+
| id | name | value|
+----+------+------+
|  1 | A    | X   |
|  2 | B    | Y   |
|  4 | NULL | Z   |
+----+------+------+
```

### FULL JOIN（全连接）

mysql 是不支持的

返回两个表中所有行的组合，如果某一侧没有匹配，则结果中该侧的列会包含 NULL。

**例子**：

```sql
SELECT Table1.id, Table1.name, Table2.value
FROM Table1 FULL JOIN Table2 ON Table1.id = Table2.id;
+----+------+------+
| id | name | value|
+----+------+------+
|  1 | A    | X   |
|  2 | B    | Y   |
|  3 | C    | NULL|
|  4 | NULL | Z   |
+----+------+------+
```

### CROSS JOIN（交叉连接）

返回两个表所有可能的行组合。

**例子**：

```sql
SELECT Table1.id, Table1.name, Table2.value
FROM Table1 CROSS JOIN Table2;
+----+------+------+
| id | name | value|
+----+------+------+
|  1 | A    | X   |
|  1 | A    | Y   |
|  1 | A    | Z   |
|  2 | B    | X   |
|  2 | B    | Y   |
|  2 | B    | Z   |
|  3 | C    | X   |
|  3 | C    | Y   |
|  3 | C    | Z   |
+----+------+------+
```

在实际使用中，`INNER JOIN` 是最常用的，因为它只返回匹配的行。`LEFT JOIN` 和 `RIGHT JOIN` 用于获取一个表的所有行，即使另一个表中没有匹配。`FULL JOIN` 用于获取两个表中所有行的完整集合，包括匹配和不匹配的行。`CROSS JOIN` 用于生成笛卡尔积，通常用于特殊情况。

## 从主表复制数据到副表

```mysql
insert into 
stock_change_record_copy1(order_id,sale_item_sku_id,quantity,ext,gmt_create,gmt_modified)
select 
order_id,sale_item_sku_id,quantity,ext,gmt_create,gmt_modified 
from stock_change_record
```





## SQL编写优化思路

### 0、SQL执行顺序

![图片](../../../../../.Trash/img/SQL相关/640)

**sql语句的执行顺序：**

```
FROM
<left_table>

ON
<join_condition>

<join_type>
 JOIN
<right_table>

WHERE
<where_condition>

GROUP BY
<group_by_list>

HAVING
<having_condition>

SELECT

DISTINCT
<select_list>

ORDER BY
<order_by_condition>

LIMIT
<limit_number>
```

### 1、SearchAfter模式的LIMIT

分页查询是最常用的场景之一，但也通常也是最容易出问题的地方。比如对于下面简单的语句，一般 DBA 想到的办法是在 type, name, create_time 字段上加组合索引。这样条件排序都能有效的利用到索引，性能迅速提升。

```
SELECT *
FROM   operation
WHERE  type = 'SQLStats'
       AND name = 'SlowLog'
ORDER  BY create_time
LIMIT  1000, 10;
```

好吧，可能90%以上的 DBA 解决该问题就到此为止。但当 LIMIT 子句变成 “LIMIT 1000000,10” 时，程序员仍然会抱怨：我只取10条记录为什么还是慢？

要知道数据库也并不知道第1000000条记录从什么地方开始，即使有索引也需要从头计算一次。出现这种性能问题，多数情形下是程序员偷懒了。

在前端数据浏览翻页，或者大数据分批导出等场景下，是可以将上一页的最大值当成参数作为查询条件的。SQL 重新设计如下：

```
SELECT   *
FROM     operation
WHERE    type = 'SQLStats'
AND      name = 'SlowLog'
AND      create_time > '2017-03-16 14:00:00'
ORDER BY create_time limit 10;
```

在新设计下查询时间基本固定，不会随着数据量的增长而发生变化。



### 2、查询隐式转换导致索引失效

SQL语句中查询变量和字段定义类型不匹配，如字符串类型的字段，接受了一个数值类型的值。会导致隐式的函数转换，从而使索引失效。

```sql
# name 字段是字符串
EXPLAIN SELECT * from name where name ='4';
# 1	SIMPLE	name		ref	i_name	i_name	1023	const	1	100.00	 走索引

EXPLAIN SELECT * from name where name =4;
# 1	SIMPLE	name		ALL	i_name				4	25.00	Using where			未走索引

EXPLAIN SELECT * from name where name =CAST(4 as CHAR);
# 1	SIMPLE	name		ref	i_name	i_name	1023	const	1	100.00	 走索引
```



### 4、混合排序

MySQL 不能利用索引进行混合排序。但在某些场景，还是有机会使用特殊方法提升性能的。

```
SELECT *
FROM   my_order o
       INNER JOIN my_appraise a ON a.orderid = o.id
ORDER  BY a.is_reply ASC,
          a.appraise_time DESC
LIMIT  0, 20
```

执行计划显示为全表扫描：

```
+----+-------------+-------+--------+-------------+---------+---------+---------------+---------+-+
| id | select_type | table | type   | possible_keys     | key     | key_len | ref      | rows    | Extra
+----+-------------+-------+--------+-------------+---------+---------+---------------+---------+-+
|  1 | SIMPLE      | a     | ALL    | idx_orderid | NULL    | NULL    | NULL    | 1967647 | Using filesort |
|  1 | SIMPLE      | o     | eq_ref | PRIMARY     | PRIMARY | 122     | a.orderid |       1 | NULL           |
+----+-------------+-------+--------+---------+---------+---------+-----------------+---------+-+
```

由于 is_reply 只有0和1两种状态，我们按照下面的方法重写后，执行时间从1.58秒降低到2毫秒。

```
SELECT *
FROM   ((SELECT *
         FROM   my_order o
                INNER JOIN my_appraise a
                        ON a.orderid = o.id
                           AND is_reply = 0
         ORDER  BY appraise_time DESC
         LIMIT  0, 20)
        UNION ALL
        (SELECT *
         FROM   my_order o
                INNER JOIN my_appraise a
                        ON a.orderid = o.id
                           AND is_reply = 1
         ORDER  BY appraise_time DESC
         LIMIT  0, 20)) t
ORDER  BY  is_reply ASC,
          appraisetime DESC
LIMIT  20;
```

### 5、EXISTS语句

MySQL 对待 EXISTS 子句时，仍然采用嵌套子查询的执行方式。如下面的 SQL 语句：

```
SELECT *
FROM   my_neighbor n
       LEFT JOIN my_neighbor_apply sra
              ON n.id = sra.neighbor_id
                 AND sra.user_id = 'xxx'
WHERE  n.topic_status < 4
       AND EXISTS(SELECT 1
                  FROM   message_info m
                  WHERE  n.id = m.neighbor_id
                         AND m.inuser = 'xxx')
       AND n.topic_type <> 5
```

执行计划为：

```
+----+--------------------+-------+------+-----+------------------------------------------+---------+-------+---------+ -----+
| id | select_type        | table | type | possible_keys     | key   | key_len | ref   | rows    | Extra   |
+----+--------------------+-------+------+ -----+------------------------------------------+---------+-------+---------+ -----+
|  1 | PRIMARY            | n     | ALL  |  | NULL     | NULL    | NULL  | 1086041 | Using where                   |
|  1 | PRIMARY            | sra   | ref  |  | idx_user_id | 123     | const |       1 | Using where          |
|  2 | DEPENDENT SUBQUERY | m     | ref  |  | idx_message_info   | 122     | const |       1 | Using index condition; Using where |
+----+--------------------+-------+------+ -----+------------------------------------------+---------+-------+---------+ -----+
```

去掉 exists 更改为 join，能够避免嵌套子查询，将执行时间从1.93秒降低为1毫秒。

```
SELECT *
FROM   my_neighbor n
       INNER JOIN message_info m
               ON n.id = m.neighbor_id
                  AND m.inuser = 'xxx'
       LEFT JOIN my_neighbor_apply sra
              ON n.id = sra.neighbor_id
                 AND sra.user_id = 'xxx'
WHERE  n.topic_status < 4
       AND n.topic_type <> 5
```

新的执行计划：

```
+----+-------------+-------+--------+ -----+------------------------------------------+---------+ -----+------+ -----+
| id | select_type | table | type   | possible_keys     | key       | key_len | ref   | rows | Extra                 |
+----+-------------+-------+--------+ -----+------------------------------------------+---------+ -----+------+ -----+
|  1 | SIMPLE      | m     | ref    | | idx_message_info   | 122     | const    |    1 | Using index condition |
|  1 | SIMPLE      | n     | eq_ref | | PRIMARY   | 122     | ighbor_id |    1 | Using where      |
|  1 | SIMPLE      | sra   | ref    | | idx_user_id | 123     | const     |    1 | Using where           |
+----+-------------+-------+--------+ -----+------------------------------------------+---------+ -----+------+ -----+
```

### 6、条件下推

外部查询条件不能够下推到复杂的视图或子查询的情况有：

1、聚合子查询； 2、含有 LIMIT 的子查询； 3、UNION 或 UNION ALL 子查询； 4、输出字段中的子查询；

如下面的语句，从执行计划可以看出其条件作用于聚合子查询之后：

```
SELECT *
FROM   (SELECT target,
               Count(*)
        FROM   operation
        GROUP  BY target) t
WHERE  target = 'rm-xxxx'
+----+-------------+------------+-------+---------------+-------------+---------+-------+------+-------------+
| id | select_type | table      | type  | possible_keys | key         | key_len | ref   | rows | Extra       |
+----+-------------+------------+-------+---------------+-------------+---------+-------+------+-------------+
|  1 | PRIMARY     | <derived2> | ref   | <auto_key0>   | <auto_key0> | 514     | const |    2 | Using where |
|  2 | DERIVED     | operation  | index | idx_4         | idx_4       | 519     | NULL  |   20 | Using index |
+----+-------------+------------+-------+---------------+-------------+---------+-------+------+-------------+
```

确定从语义上查询条件可以直接下推后，重写如下：

```
SELECT target,
       Count(*)
FROM   operation
WHERE  target = 'rm-xxxx'
GROUP  BY target
```

执行计划变为：

```
+----+-------------+-----------+------+---------------+-------+---------+-------+------+--------------------+
| id | select_type | table | type | possible_keys | key | key_len | ref | rows | Extra |
+----+-------------+-----------+------+---------------+-------+---------+-------+------+--------------------+
| 1 | SIMPLE | operation | ref | idx_4 | idx_4 | 514 | const | 1 | Using where; Using index |
+----+-------------+-----------+------+---------------+-------+---------+-------+------+--------------------+
```

关于 MySQL 外部条件不能下推的详细解释说明请参考以前文章：MySQL · 性能优化 · 条件下推到物化表 http://mysql.taobao.org/monthly/2016/07/08

### 7、提前缩小范围

先上初始 SQL 语句：

```
SELECT *
FROM   my_order o
       LEFT JOIN my_userinfo u
              ON o.uid = u.uid
       LEFT JOIN my_productinfo p
              ON o.pid = p.pid
WHERE  ( o.display = 0 )
       AND ( o.ostaus = 1 )
ORDER  BY o.selltime DESC
LIMIT  0, 15
```

该SQL语句原意是：先做一系列的左连接，然后排序取前15条记录。从执行计划也可以看出，最后一步估算排序记录数为90万，时间消耗为12秒。

```
+----+-------------+-------+--------+---------------+---------+---------+-----------------+--------+----------------------------------------------------+
| id | select_type | table | type   | possible_keys | key     | key_len | ref             | rows   | Extra                                              |
+----+-------------+-------+--------+---------------+---------+---------+-----------------+--------+----------------------------------------------------+
|  1 | SIMPLE      | o     | ALL    | NULL          | NULL    | NULL    | NULL            | 909119 | Using where; Using temporary; Using filesort       |
|  1 | SIMPLE      | u     | eq_ref | PRIMARY       | PRIMARY | 4       | o.uid |      1 | NULL                                               |
|  1 | SIMPLE      | p     | ALL    | PRIMARY       | NULL    | NULL    | NULL            |      6 | Using where; Using join buffer (Block Nested Loop) |
+----+-------------+-------+--------+---------------+---------+---------+-----------------+--------+----------------------------------------------------+
```

由于最后 WHERE 条件以及排序均针对最左主表，因此可以先对 my_order 排序提前缩小数据量再做左连接。SQL 重写后如下，执行时间缩小为1毫秒左右。

```
SELECT *
FROM (
SELECT *
FROM   my_order o
WHERE  ( o.display = 0 )
       AND ( o.ostaus = 1 )
ORDER  BY o.selltime DESC
LIMIT  0, 15
) o
     LEFT JOIN my_userinfo u
              ON o.uid = u.uid
     LEFT JOIN my_productinfo p
              ON o.pid = p.pid
ORDER BY  o.selltime DESC
limit 0, 15
```

再检查执行计划：子查询物化后（select_type=DERIVED)参与 JOIN。虽然估算行扫描仍然为90万，但是利用了索引以及 LIMIT 子句后，实际执行时间变得很小。

```
+----+-------------+------------+--------+---------------+---------+---------+-------+--------+----------------------------------------------------+
| id | select_type | table      | type   | possible_keys | key     | key_len | ref   | rows   | Extra                                              |
+----+-------------+------------+--------+---------------+---------+---------+-------+--------+----------------------------------------------------+
|  1 | PRIMARY     | <derived2> | ALL    | NULL          | NULL    | NULL    | NULL  |     15 | Using temporary; Using filesort                    |
|  1 | PRIMARY     | u          | eq_ref | PRIMARY       | PRIMARY | 4       | o.uid |      1 | NULL                                               |
|  1 | PRIMARY     | p          | ALL    | PRIMARY       | NULL    | NULL    | NULL  |      6 | Using where; Using join buffer (Block Nested Loop) |
|  2 | DERIVED     | o          | index  | NULL          | idx_1   | 5       | NULL  | 909112 | Using where                                        |
+----+-------------+------------+--------+---------------+---------+---------+-------+--------+----------------------------------------------------+
```

### 8、中间结果集下推

再来看下面这个已经初步优化过的例子(左连接中的主表优先作用查询条件)：

```
SELECT    a.*,
          c.allocated
FROM      (
              SELECT   resourceid
              FROM     my_distribute d
                   WHERE    isdelete = 0
                   AND      cusmanagercode = '1234567'
                   ORDER BY salecode limit 20) a
LEFT JOIN
          (
              SELECT   resourcesid， sum(ifnull(allocation, 0) * 12345) allocated
              FROM     my_resources
                   GROUP BY resourcesid) c
ON        a.resourceid = c.resourcesid
```

那么该语句还存在其它问题吗？不难看出子查询 c 是全表聚合查询，在表数量特别大的情况下会导致整个语句的性能下降。

其实对于子查询 c，左连接最后结果集只关心能和主表 resourceid 能匹配的数据。因此我们可以重写语句如下，执行时间从原来的2秒下降到2毫秒。

```
SELECT    a.*,
          c.allocated
FROM      (
                   SELECT   resourceid
                   FROM     my_distribute d
                   WHERE    isdelete = 0
                   AND      cusmanagercode = '1234567'
                   ORDER BY salecode limit 20) a
LEFT JOIN
          (
                   SELECT   resourcesid， sum(ifnull(allocation, 0) * 12345) allocated
                   FROM     my_resources r,
                            (
                                     SELECT   resourceid
                                     FROM     my_distribute d
                                     WHERE    isdelete = 0
                                     AND      cusmanagercode = '1234567'
                                     ORDER BY salecode limit 20) a
                   WHERE    r.resourcesid = a.resourcesid
                   GROUP BY resourcesid) c
ON        a.resourceid = c.resourcesid
```

但是子查询 a 在我们的SQL语句中出现了多次。这种写法不仅存在额外的开销，还使得整个语句显的繁杂。使用 WITH 语句再次重写：

```
WITH a AS
(
         SELECT   resourceid
         FROM     my_distribute d
         WHERE    isdelete = 0
         AND      cusmanagercode = '1234567'
         ORDER BY salecode limit 20)
SELECT    a.*,
          c.allocated
FROM      a
LEFT JOIN
          (
                   SELECT   resourcesid， sum(ifnull(allocation, 0) * 12345) allocated
                   FROM     my_resources r,
                            a
                   WHERE    r.resourcesid = a.resourcesid
                   GROUP BY resourcesid) c
ON        a.resourceid = c.resourcesid
```

### 小结

数据库编译器产生执行计划，决定着SQL的实际执行方式。但是编译器只是尽力服务，所有数据库的编译器都不是尽善尽美的。

上述提到的多数场景，在其它数据库中也存在性能问题。了解数据库编译器的特性，才能避规其短处，写出高性能的SQL语句。

程序员在设计数据模型以及编写SQL语句时，要把算法的思想或意识带进来。

编写复杂SQL语句要养成使用 WITH 语句的习惯。简洁且思路清晰的SQL语句也能减小数据库的负担 。