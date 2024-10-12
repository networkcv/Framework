## 1.Lua

Lua 是一个小巧的脚本语言，由标准C编写而成，几乎在所有操作系统和平台上都可以编译、运行。因此它没有提供强大的库，这是由它的定位决定的。所以Lua不适合作为开发独立应用程序的语言。

在Redis中使用Lua有很多好处，它可以减少网络开销，把多个操作一次性打包执行。Lua脚本天生支持原子性的操作，避免开启第三方事务，提高了性能。代码重用也是一个重要的好处，写好的代码会被加载到Redis内存中，可以供其他客户端使用，减少重复劳动。Lua脚本使用C语言写成的，执行速度很快，并天然具有可移植性，也是代码重用的很好体现。一个完整的Lua解释器，不过200k，在目前所有脚本引擎中，Lua的速度是最快的。这一切都决定了Lua是作为嵌入式脚本的最佳选择。

- **轻量级**: 它用标准C语言编写并以源代码形式开放，编译后仅仅一百余K，可以很方便的嵌入别的程序里。
- **可扩展**: Lua提供了非常易于使用的扩展接口和机制：由宿主语言(通常是C或C++)提供这些功能，Lua可以使用它们，就像是本来就内置的功能一样。
- **其它特性**
  - 支持面向过程(procedure-oriented)编程和函数式编程(functional programming)；
  - 自动内存管理；只提供了一种通用类型的表（table），用它可以实现数组，哈希表，集合，对象；
  - 语言内置模式匹配；闭包(closure)；函数也可以看做一个值；提供多线程（协同进程，并非操作系统所支持的线程）支持；
  - 通过闭包和table可以很方便地支持面向对象编程所需要的一些关键机制，比如数据抽象，虚函数，继承和重载等。

## 2.Lua的安装

**方式一**

mac 上可以直接使用 brewhome 进行安装

```
brew install lua
```

**方式二**

```
curl -R -O http://www.lua.org/ftp/lua-5.3.0.tar.gz
tar zxf lua-5.3.0.tar.gz
cd lua-5.3.0
make macosx test
make install
```



安装好之后，执行 **lua** 命令就可以进入lua的交互命令行模式。

```
networkcavalry@NetworkCavalry-MBP / % lua
Lua 5.2.3  Copyright (C) 1994-2013 Lua.org, PUC-Rio
> 
```

## 3.Lua的基本语法

[lua基本语法学习链接](https://www.runoob.com/lua/lua-basic-syntax.html)

[redis在使用lua脚本以及实现redis分布式锁](https://blog.csdn.net/zjcjava/article/details/84842115?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromBaidu-1.control)

[lua book](https://github.com/52fhy/lua-book)



## 4.cjson的安装

前面有提到 lua 本身不提供类库，在编写脚本时，我们常常会需要对 Json 进行转换，这个时候就需要一个工具包来为我们提供该功能，**cjson** 就可以很好的实现这一点，它可以将字符串转换为Lua的类型。

1. 将 **lua-cjson-2.1.0.tar.gz** 下载后进行解压 

   ```
   tar zxvf lua-cjson-2.1.0.tar.gz [-C /usr/local]
   ```

2. 进入解压目录修改 Makefile 文件

   ```
   ##### Build defaults #####
   LUA_VERSION =       5.4.2     修改为lua的版本，lua -v 查看版本
   TARGET =            cjson.so
   PREFIX =            /usr/local	
   #CFLAGS =            -g -Wall -pedantic -fno-inline
   CFLAGS =            -O3 -Wall -pedantic -DNDEBUG
   CJSON_CFLAGS =      -fpic
   #CJSON_LDFLAGS =     -shared
   CJSON_LDFLAGS =     -bundle -undefined dynamic_lookup
   LUA_INCLUDE_DIR =   $(PREFIX)/include
   LUA_CMODULE_DIR =   $(PREFIX)/lib/lua/$(LUA_VERSION)
   LUA_MODULE_DIR =    $(PREFIX)/share/lua/$(LUA_VERSION)
   LUA_BIN_DIR =       $(PREFIX)/bin
   ```

3. 在该目录下 进行 **编译** 和 **安装**

   ```
   networkcavalry@NetworkCavalry-MBP lua-cjson-2.1.0 % make
   networkcavalry@NetworkCavalry-MBP lua-cjson-2.1.0 % make install
   ```

   操作完成后进行检查， **/usr/local/lib/lua/5.2** 目录下存在 **cjson.so** 这个文件，如果没有的话，去cJson的解压目录进行手动复制。

这样就可以在本地使用cjson 进行 encode 和 decode了

```lua
local cjson = require "cjson"
local retTable = {};    --最终产生json的表

--顺序数值
local intDatas = {};
intDatas[1] = 100;
intDatas[2] = "100";

--数组
local aryDatas = {};
aryDatas[1] = {};
aryDatas[1]["键11"] = "值11";
aryDatas[1]["键12"] = "值12";
aryDatas[2] = {};
aryDatas[2]["键21"] = "值21";
aryDatas[2]["键22"] = "值22";

--对Table赋值
retTable["键1"] = "值1";
retTable[2] = 123;
retTable["int_datas"] = intDatas;
retTable["aryDatas"] = aryDatas;

--将表数据编码成json字符串
local jsonStr = cjson.encode(retTable);
print(jsonStr);

return retTable;

```



## 5.ZeroBrane Studio 安装

ZeroBrane Studio 是一个轻量级的开源的 Lua IDE，支持 语法突出显示，代码分析器，实时编码 和 断点调试。

进行安装目录，执行命令打开IDE

```
networkcavalry@NetworkCavalry-MBP ZeroBraneStudio-1.90 % ./zbstudio.sh 
```

**注意事项：**

执行前需要进行配置

```
path.lua="/usr/local/bin/lua"
```

## 6.Lua应用场景

### 1.Lua实现限流

### 2.Lua实现分布式锁

### 3.Lua脚本结合Redis使用

## 7.Lua Redis [debug]()

```
redis-cli -h r-uf6y01md4e3nrelhaq.redis.rds.aliyuncs.com -p 6379 -a ETyqgTgsK85TGK0h --ldb --eval ~/a.lua  price_pick_up_skuId_metas:{1001009} price_pick_up_skuId_metas:{1001009}:100001 , 1612144900001
```

https://cloud.tencent.com/developer/article/1683207

# Lua脚本规范与常见报错

**重要**

本文中含有需要您注意的重要提示信息，忽略该信息可能对您的业务造成影响，请务必仔细阅读。

云数据库Redis实例支持Lua相关命令，通过Lua脚本可高效地处理CAS（compare-and-set）命令，进一步提升Redis的性能，同时可以轻松实现以前较难实现或者不能高效实现的模式。本文介绍通过Redis使用Lua脚本的基本语法与使用规范。

# 注意事项

[数据管理服务DMS](https://www.alibabacloud.com/help/zh/dms/product-overview/what-is-dms)控制台目前暂不支持使用Lua脚本等相关命令，请通过客户端或Redis-cli连接Redis实例使用Lua脚本。

# 基本语法

以下为部分命令的示例，本文在执行以下命令前执行了`SET foo value_test`。

## EVAL

```
EVAL script numkeys [key [key ...]] [arg [arg ...]]
```

执行给定的脚本和参数，并返回结果。参数说明：**script**：Lua脚本。**numkeys**：指定KEYS[]参数的数量，非负整数。**KEYS[]**：传入的Redis键参数。**ARGV[]**：传入的脚本参数。KEYS[]与ARGV[]的索引均从1开始。**说明**与SCRIPT LOAD命令一样，EVAL命令也会将Lua脚本缓存至Redis。混用或滥用**KEYS[]**与**ARGV[]**可能会导致Redis产生不符合预期的行为，尤其在集群模式下，详情请参见[集群中Lua脚本的限制](https://www.alibabacloud.com/help/zh/redis/support/usage-of-lua-scripts#section-8f7-qgv-dlv)。推荐使用**KEYS[]**与**ARGV[]**的方式传递参数。不推荐将参数编码进脚本中，过多类似行为会导致LUA虚拟机内存使用量上升，且无法及时回收，极端情况下会导致实例主库与备库内存溢出（Out of Memory），造成数据丢失。

EVAL命令示例：

```sql
EVAL "return redis.call('GET', KEYS[1])" 1 foo
```

返回示例：

```sql
"value_test"
```

## EVALSHA

```
EVALSHA sha1 numkeys key [key ...] arg [arg ...]
```

给定脚本的SHA1校验和，Redis将再次执行脚本。使用EVALSHA命令时，若sha1值对应的脚本未缓存至Redis中，Redis会返回NOSCRIPT错误，请通过EVAL或SCRIPT LOAD命令将目标脚本缓存至Redis中后进行重试，详情请参见[处理NOSCRIPT错误](https://www.alibabacloud.com/help/zh/redis/support/usage-of-lua-scripts#section-82q-id4-ycg)。

EVALSHA命令示例：

```sql
EVALSHA 620cd258c2c9c88c9d10db67812ccf663d96bdc6 1 foo
```

返回示例：

```sql
"value_test"
```



## SCRIPT LOAD

```
SCRIPT LOAD script
```

将给定的script脚本缓存在Redis中，并返回该脚本的SHA1校验和。

SCRIPT LOAD命令示例：

```sql
SCRIPT LOAD "return redis.call('GET', KEYS[1])"
```

返回示例：

```sql
"620cd258c2c9c88c9d10db67812ccf663d96bdc6"
```



## SCRIPT EXISTS

```
SCRIPT EXISTS script [script ...]
```

给定一个（或多个）脚本的SHA1，返回每个SHA1对应的脚本是否已缓存在当前Redis服务。脚本已存在则返回1，不存在则返回0。

SCRIPT EXISTS命令示例：

```sql
SCRIPT EXISTS 620cd258c2c9c88c9d10db67812ccf663d96bdc6 ffffffffffffffffffffffffffffffffffffffff
```

返回示例：

```sql
1) (integer) 1
2) (integer) 0
```



## SCRIPT KILL

```
SCRIPT KILL
```

停止正在运行的Lua脚本。

## SCRIPT FLUSH

```
SCRIPT FLUSH
```

清空当前Redis服务器中的所有Lua脚本缓存。

SCRIPT FLUSH命令示例：

**警告** 该命令会清空实例中的所有Lua脚本缓存，请提前备份Lua脚本。

```sql
SCRIPT FLUSH
```

返回示例：

```sql
OK
```

# 优化内存、网络开销

现象：

在Redis中缓存了大量功能重复的脚本，占用大量内存空间甚至引发内存溢出（Out of Memory），错误示例如下。

```plaintext
EVAL "return redis.call('set', 'k1', 'v1')" 0
EVAL "return redis.call('set', 'k2', 'v2')" 0
```

解决方案：

- 请避免将参数作为常量写在Lua脚本中，以减少内存空间的浪费。

  ```plaintext
  # 与错误示例实现相同功能但仅需缓存一次脚本。
  EVAL "return redis.call('set', KEYS[1], ARGV[1])" 1 k1 v1
  EVAL "return redis.call('set', KEYS[1], ARGV[1])" 1 k2 v2
  ```

- 更加建议采用如下写法，在减少内存的同时，降低网络开销。

  ```plaintext
  SCRIPT LOAD "return redis.call('set', KEYS[1], ARGV[1])"    # 执行后，Redis将返回"55b22c0d0cedf3866879ce7c854970626dcef0c3"
  EVALSHA 55b22c0d0cedf3866879ce7c854970626dcef0c3 1 k1 v1
  EVALSHA 55b22c0d0cedf3866879ce7c854970626dcef0c3 1 k2 v2
  ```

# 清理Lua脚本的内存占用

现象：

由于Lua脚本缓存将计入Redis的内存使用量中，并会导致used_memory升高，当Redis的内存使用量接近甚至超过maxmemory时，可能引发内存溢出（Out Of Memory），报错示例如下。

```plaintext
-OOM command not allowed when used memory > 'maxmemory'.
```

解决方案：

通过客户端执行SCRIPT FLUSH命令清除Lua脚本缓存，但与FLUSHALL不同，SCRIPT FLUSH命令为同步操作。若Redis缓存的Lua脚本过多，SCRIPT FLUSH命令会阻塞Redis较长时间，可能导致实例不可用，请谨慎处理，建议在业务低峰期执行该操作。

**说明**

在控制台上单击**清除数据**只能清除数据，无法清除Lua脚本缓存。

同时，请避免编写过大的Lua脚本，防止占用过多的内存；避免在Lua脚本中大批量写入数据，否则会导致内存使用急剧升高，甚至造成实例OOM。在业务允许的情况下，建议开启[数据逐出](https://www.alibabacloud.com/help/zh/redis/user-guide/how-does-apsaradb-for-redis-evict-data-by-default)（云数据库Redis默认开启，模式为volatile-lru）节省内存空间。但无论是否开启数据逐出，Redis均不会逐出Lua脚本缓存。

# 处理NOSCRIPT错误

现象：

使用EVALSHA命令时，若sha1值对应的脚本未缓存至Redis中，Redis会返回NOSCRIPT错误，报错示例如下。

```plaintext
(error) NOSCRIPT No matching script. Please use EVAL.
```

解决方案：

请通过EVAL命令或SCRIPT LOAD命令将目标脚本缓存至Redis中后进行重试。但由于Redis不保证Lua脚本的持久化、复制能力，Redis在部分场景下仍会清除Lua脚本缓存（例如实例迁移、变配等），这要求您的客户端需具备处理该错误的能力，详情请参见[脚本缓存、持久化与复制](https://www.alibabacloud.com/help/zh/redis/support/usage-of-lua-scripts#section-ot9-rbm-ofj)。

以下为一种处理NOSCRIPT错误的Python Demo示例，该demo利用Lua脚本实现了字符串prepend操作。

**说明**

您可以考虑通过Python的redis-py解决该类错误，redis-py提供了封装Redis Lua的一些底层逻辑判断（例如NOSCRIPT错误的catch）的Script类。

```plaintext
import redis
import hashlib

# strin是一个Lua脚本的字符串，函数以字符串的格式返回strin的sha1值。
def calcSha1(strin):
    sha1_obj = hashlib.sha1()
    sha1_obj.update(strin.encode('utf-8'))
    sha1_val = sha1_obj.hexdigest()
    return sha1_val

class MyRedis(redis.Redis):

    def __init__(self, host="localhost", port=6379, password=None, decode_responses=False):
        redis.Redis.__init__(self, host=host, port=port, password=password, decode_responses=decode_responses)

    def prepend_inLua(self, key, value):
        script_content = """\
        local suffix = redis.call("get", KEYS[1])
        local prefix = ARGV[1]
        local new_value = prefix..suffix
        return redis.call("set", KEYS[1], new_value)
        """
        script_sha1 = calcSha1(script_content)
        if self.script_exists(script_sha1)[0] == True:      # 检查Redis是否已缓存该脚本。
            return self.evalsha(script_sha1, 1, key, value) # 如果已缓存，则用EVALSHA执行脚本
        else:
            return self.eval(script_content, 1, key, value) # 否则用EVAL执行脚本，注意EVAL有将脚本缓存到Redis的作用。这里也可以考虑采用SCRIPT LOAD与EVALSHA的方式。

r = MyRedis(host="r-******.redis.rds.aliyuncs.com", password="***:***", port=6379, decode_responses=True)

print(r.prepend_inLua("k", "v"))
print(r.get("k"))
            
```

# 处理Lua脚本超时

- 现象：

  由于Lua脚本在Tair中是原子执行的，Lua慢请求可能会导致Tair阻塞。单个Lua脚本阻塞Tair最多5秒，5秒后Tair会给所有其他命令返回如下BUSY error报错，直到脚本执行结束。

  ```sql
  BUSY Redis is busy running a script. You can only call SCRIPT KILL or SHUTDOWN NOSAVE.
  ```

  解决方案：

  您可以通过SCRIPT KILL命令终止Lua脚本或等待Lua脚本执行结束。

  **说明**

  - SCRIPT KILL命令在执行慢Lua脚本的前5秒不会生效（Tair阻塞中）。
  - 建议您编写Lua脚本时预估脚本的执行时间，同时检查死循环等问题，避免过长时间阻塞Tair导致服务不可用，必要时请拆分Lua脚本。

- 现象：

  若当前Lua脚本已执行写命令，则SCRIPT KILL命令将无法生效，报错示例如下。

  ```sql
  (error) UNKILLABLE Sorry the script already executed write commands against the dataset. You can either wait the script termination or kill the server in a hard way using the SHUTDOWN NOSAVE command.
  ```

  解决方案：

  请在控制台的**实例列表**中单击对应实例**重启**。

# 脚本缓存、持久化与复制

现象：

在不重启、不调用SCRIPT FLUSH命令的情况下，Redis会一直缓存执行过的Lua脚本。但在部分情况下（例如实例迁移、变配、版本升级、切换等等），Redis无法保证Lua脚本的持久化，也无法保证Lua脚本能够被同步至其他节点。

解决方案：

由于Redis不保证Lua脚本的持久化、复制能力，请您在本地存储所有Lua脚本，在必要时通过EVAL或SCRIPT LOAD命令将Lua脚本重新缓存至Redis中，避免实例重启、HA切换等操作时Redis中Lua脚本被清空而带来的NOSCRIPT错误。

# 集群中Lua脚本的限制

Redis Cluster对使用Lua脚本增加了一些限制，在此基础上，Redis集群版对使用Lua脚本存在如下额外限制：

- 小版本限制，若无法执行EVAL的相关命令，并报错`ERR command eval not support for normal user`时，请升级小版本后重试，具体操作请参见[升级小版本](https://www.alibabacloud.com/help/zh/redis/user-guide/update-the-minor-version#concept-itn-f44-tdb)。

- 所有Key必须在一个slot上，否则报错`-ERR eval/evalsha command keys must be in same slot\r\n`。

  您可以通过CLUSTER KEYSLOT命令获取目标Key的哈希槽（Hash Slot）进行确认。

- 对单个节点执行SCRIPT LOAD命令时，不保证将该Lua脚本存入至其他节点中。

- 不支持发布订阅命令，包括**PSUBSCRIBE**、**PUBSUB**、**PUBLISH**、**PUNSUBSCRIBE**、**SUBSCRIBE**和**UNSUBSCRIBE**。

- 不支持UNPACK函数。

**说明**

若您能够在代码中确保所有操作都在相同slot（如果不能保障这一点，执行会出错），且希望打破Redis集群的Lua限制，可以在控制台将**script_check_enable**修改为0，则后端不会对脚本进行校验，但仍需要使用KEYS数组至少传递一个key，供代理节点执行路由转发。具体操作，请参见[设置实例参数](https://www.alibabacloud.com/help/zh/redis/user-guide/modify-the-values-of-parameters-for-an-instance#concept-q1w-kxn-tdb)。

# 代理模式（Proxy）对Lua的额外检测项

您也可以通过**script_check_enable**参数关闭以下检查项（不推荐）。

- 所有key都应该由KEYS数组来传递，**redis.call/pcall**中调用的Redis命令，key的位置必须是KEYS array，且不能使用Lua变量替换KEYS，否则直接返回错误信息：`-ERR bad lua script for redis cluster, all the keys that the script uses should be passed using the KEYS array\r\n`。

  **说明**

  仅Redis 5.0版本（小版本5.0.8以下）、4.0及以下版本实例存在该限制。

  正确与错误命令示例如下：

  ```sql
  # 本示例的准备工作需执行如下命令。
  SET foo foo_value
  SET {foo}bar bar_value
  
  # 正确示例：
  EVAL "return redis.call('mget', KEYS[1], KEYS[2])" 2 foo {foo}bar
  
  # 错误示例：
  EVAL "return redis.call('mget', KEYS[1], '{foo}bar')" 1 foo                      # '{foo}bar'作为Key，应该使用KEYS数组进行传递。
  EVAL "local i = 2 return redis.call('mget', KEYS[1], KEYS[i])" 2 foo {foo}bar    # 在代理模式（Proxy）不允许执行此脚本，因为KEYS数据的索引是变量，但在直连模式中无此限制。
  EVAL "return redis.call('mget', KEYS[1], ARGV[1])" 1 foo {foo}bar                # 不应该使用ARGV[1]数据元素作为Key。
  ```

- 调用必须要带有key，否则直接返回错误信息：`-ERR for redis cluster, eval/evalsha number of keys can't be negative or zero\r\n`。

  **说明**

  仅Redis 5.0版本（小版本5.0.8以下）、4.0及以下版本实例存在该限制。

  正确与错误命令示例如下：

  ```sql
  # 正确示例
  EVAL "return redis.call('get', KEYS[1])" 1 foo
  
  # 错误示例
  EVAL "return redis.call('get', 'foo')" 0
  ```

- 不支持在MULTI、EXEC事务中使用EVAL、EVALSHA、SCRIPT系列命令。

- 不支持在Lua中执行跨Redis节点的命令，例如KEYS、SCAN等。

  为了保证Lua执行的原子性，Proxy会根据KEYS参数将Lua发送到一个Redis节点执行并获取结果，从而导致该结果与全局结果不一致。

**说明**

若您需要使用代理模式下受限的部分功能，您可以尝试开通使用云数据库Redis集群版的直连模式。但是由于云数据库Redis集群版在迁移、变配时都会通过proxy代理迁移数据，直连模式下不符合代理模式的Lua脚本会迁移、变配失败。

建议您在直连模式下使用Lua脚本时应尽可能符合代理模式下的限制规范，避免后续Lua脚本迁移、变配失败。