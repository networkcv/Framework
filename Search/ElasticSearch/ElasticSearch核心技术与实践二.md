##  基于Trem的查询

- Term是表达语意的最小单位，搜索和利用统计语言模型进行自然语言处理都需要处理Term

- Term Level Query: Term Query/ Range Query/ Exists Query/ Prefix Query/ Wildard Query
- 在ES中，Term查询，对输入**不做分词**，会将输入作为一个整体，在倒排索引中查找准确的词项，并且使用相关度算分公式为每个包含该词项的文档进行 **相关度算分**，例如“Apple-Store”
- 可以通过 Constant Source 将查询转换成一个 Filtering，避免算分，并利用缓存，提高性能

###  多字段Mapping和Term查询 

 ![image-20210102070715634](img/ElasticSearch核心技术与实践二/image-20210102070715634.png)

复合查询 — Constant Score 转为 Filter

- 将Query 装成 Filter，忽略 TF-IDF计算，避免相关性算分的开销
- Filter 可以有效利用缓存

![image-20210102070927691](img/ElasticSearch核心技术与实践二/image-20210102070927691.png)

## 基于全文的查询

- 基于全文本的查找: Match Query / Match Phrase Query / Query String Query
- 索引和搜索时都会进行分词，查询字符串先传递到一个合适的分词器，然后生成一个供查询的词项列表
- 查询时，先对输入**的查询进行分词**，然后每个词项逐个进行底层的查询，最终将结果进行合并，并未每个文档生成一个算分。例如查“Matrix reloaded”，会查到包括Matrix 或者 reload 的所有结果

Match Query 查询过程

![image-20210102071617532](img/ElasticSearch核心技术与实践二/image-20210102071617532.png)

##  结构化数据

![image-20210102071906135](img/ElasticSearch核心技术与实践二/image-20210102071906135.png)

### ES中的结构化搜索

![image-20210102072036360](img/ElasticSearch核心技术与实践二/image-20210102072036360.png)

**小结**

![image-20210102072837493](img/ElasticSearch核心技术与实践二/image-20210102072837493.png)

## 相关性和相关性算分

根据词项的重要程度和在一篇文档中出现的频率来进行相关性的算分

![image-20210102073237374](img/ElasticSearch核心技术与实践二/image-20210102073237374.png)

#### 词频TF

![image-20210102073358828](img/ElasticSearch核心技术与实践二/image-20210102073358828.png)

### 逆文档频率 IDF 

![image-20210102073556925](img/ElasticSearch核心技术与实践二/image-20210102073556925.png)

# Query Context & Filter Context

query Context 会计算相关性算分

filter Context 不会计算相关性算分，所以性能更好一些 

### Bool 查询

![image-20210106223158715](img/ElasticSearch核心技术与实践二/image-20210106223158715.png)

![image-20210106223351131](img/ElasticSearch核心技术与实践二/image-20210106223351131.png)

 ![image-20210106223944256](img/ElasticSearch核心技术与实践二/image-20210106223944256.png)

![image-20210106224027945](img/ElasticSearch核心技术与实践二/image-20210106224027945.png)

### Bool 嵌套

![image-20210106224115012](img/ElasticSearch核心技术与实践二/image-20210106224115012.png)

### Bool 嵌套的结构，会对相关度算分产生影响

![image-20210106224206561](img/ElasticSearch核心技术与实践二/image-20210106224206561.png)

### 控制字段的Boosting 来影响算分

![image-20210106224321196](img/ElasticSearch核心技术与实践二/image-20210106224321196.png)

### boosting Query 查询

控制查询的精准度，调小某个词的算分权重

![image-20210106224700369](img/ElasticSearch核心技术与实践二/image-20210106224700369.png)

# 单字符串多字段查询

## Dis Max Query

Bool Query在用should进行时，只是将各个字段上的评分做一个加和，当处理一些竞争字段的时候，返回的算分并不满足我们的期望，所以产生了Dis Max Query 

### 示例

![image-20210106225012399](img/ElasticSearch核心技术与实践二/image-20210106225012399.png)

上面的查询结果是 doc1排在doc2的前面，原因在于doc1命中了两条should语句

### 算分过程

- 查询should语句中的两个查询
- 加和两个查询的评分
- 乘以匹配语句的总数
- 除以所有语句的总数

### Disjunction Max Query

![image-20210106225542332](img/ElasticSearch核心技术与实践二/image-20210106225542332.png)

![image-20210106225628907](img/ElasticSearch核心技术与实践二/image-20210106225628907.png)

这个时候doc2就会排在doc1的前面

## 三种场景

![image-20210106230441504](img/ElasticSearch核心技术与实践二/image-20210106230441504.png)

## Multi Match Query

### best_fields

![image-20210106230734294](img/ElasticSearch核心技术与实践二/image-20210106230734294.png)

### most_fields

![image-20210106231151020](img/ElasticSearch核心技术与实践二/image-20210106231151020.png)

使用多数字段匹配解决

![image-20210106231230271](img/ElasticSearch核心技术与实践二/image-20210106231230271.png)

### cross_fields

# Search Template和Index Alias

Search Template 解耦程序&搜索DSL 

Index Alias 实现零停机运维

![image-20210107232225097](img/ElasticSearch核心技术与实践二/image-20210107232225097.png)



# Function-Score-Query优化算分 

## 算分与排序

- Elasticsearch 默认会以文档的相关度算分进行排序
- 可以通过指定一个或者多个字段进行排序
- 使用相关度算分（score）排序，不能满足某些特定条件，无法针对相关度，对排序实现更多的控制

## Function-Score-Query

![image-20210107232750514](img/ElasticSearch核心技术与实践二/image-20210107232750514.png)

![image-20210107233044138](img/ElasticSearch核心技术与实践二/image-20210107233044138.png)![image-20210107233108968](img/ElasticSearch核心技术与实践二/image-20210107233108968.png)

可以看到点赞数对 _source的影响很大，但这并不是我们想要的，我们可以使用Modifier来控制它的影响度

### 使用 Modifier 平滑曲线

![image-20210107233302338](img/ElasticSearch核心技术与实践二/image-20210107233302338.png)

### 还可以引入 Factor

![image-20210107233450816](img/ElasticSearch核心技术与实践二/image-20210107233450816.png)

Boost Mode 和 Max Boost

![image-20210107233605359](img/ElasticSearch核心技术与实践二/image-20210107233605359.png)

![image-20210107233707368](img/ElasticSearch核心技术与实践二/image-20210107233707368.png)

### 一致性随机函数

![image-20210107233843098](img/ElasticSearch核心技术与实践二/image-20210107233843098.png)

# Term&Phrase-Suggester

## 什么是搜索建议

![image-20210107234023124](img/ElasticSearch核心技术与实践二/image-20210107234023124.png)

## Elasticsearch Suggester API

- 搜索引擎中类似的功能，在 Elasticsearch 中是通过 Suggester API 实现的
- 原理：将输入的文本分解为 Token，然后在索引的字典里查找相似的 Term 并返回
- 根据不同的使用场景，ElasticSearch 设计了4种类别的 Suggesters
  - Term & Phrase Suggester
  - Complete & Context Suggester

### Term Suggester - Missing Mode

![image-20210107234912136](img/ElasticSearch核心技术与实践二/image-20210107234912136.png)

![image-20210107234925615](img/ElasticSearch核心技术与实践二/image-20210107234925615.png)

### Phrase Suggester

![image-20210107235108889](img/ElasticSearch核心技术与实践二/image-20210107235108889.png)

### Complete Suggester

![image-20210107235616185](img/ElasticSearch核心技术与实践二/image-20210107235616185.png)

**使用Complete SUggester 的步骤**

![image-20210107235817662](img/ElasticSearch核心技术与实践二/image-20210107235817662.png)

![image-20210108000914502](img/ElasticSearch核心技术与实践二/image-20210108000914502.png) 

### Context Suggester

![image-20210108083701429](img/ElasticSearch核心技术与实践二/image-20210108083701429.png)

![image-20210108083857077](img/ElasticSearch核心技术与实践二/image-20210108083857077.png) 

## 精准度和召回率

### 精准度

Copmletion > Phrase > Term

### 召回率

Term > Phrase > Completion

### 性能

Completion > Phrase > Term

# 跨集群搜索

## 水平扩展的痛点

![image-20210108084205959](img/ElasticSearch核心技术与实践二/image-20210108084205959.png)

## 跨集群搜索 Cross Cluster Search

![image-20210108084629951](img/ElasticSearch核心技术与实践二/image-20210108084629951.png)

### 配置及查询

![image-20210108084657544](img/ElasticSearch核心技术与实践二/image-20210108084657544.png)

# 集群分布式模型及选主与脑裂问题

## 分布式特性

![image-20210108084826951](img/ElasticSearch核心技术与实践二/image-20210108084826951.png)

## 节点

![image-20210108084839278](img/ElasticSearch核心技术与实践二/image-20210108084839278.png)

## Coordinating Node

![image-20210108084906738](img/ElasticSearch核心技术与实践二/image-20210108084906738.png)

## Data Node

![image-20210108085244284](img/ElasticSearch核心技术与实践二/image-20210108085244284.png)

## Master Node

![image-20210108085439509](img/ElasticSearch核心技术与实践二/image-20210108085439509.png)

## Master Eligible Node & 选主流程

![image-20210108085537133](img/ElasticSearch核心技术与实践二/image-20210108085537133.png)

![image-20210108085627447](img/ElasticSearch核心技术与实践二/image-20210108085627447.png)

### 脑裂问题

![image-20210108085642894](img/ElasticSearch核心技术与实践二/image-20210108085642894.png)

## 如何避免脑裂问题

![image-20210108085741601](img/ElasticSearch核心技术与实践二/image-20210108085741601.png)

## 集群状态

![image-20210108085603854](img/ElasticSearch核心技术与实践二/image-20210108085603854.png)

## 配置节点类型

![image-20210108085849389](img/ElasticSearch核心技术与实践二/image-20210108085849389.png)

# 分片和集群的故障转移

## Primary Shard 提升系统存储容量

![image-20210108090031600](img/ElasticSearch核心技术与实践二/image-20210108090031600.png) 

## Replica Shard 提高数据可用性

![image-20210108090116054](img/ElasticSearch核心技术与实践二/image-20210108090116054.png)

## 分片数的设定

![image-20210108090130200](img/ElasticSearch核心技术与实践二/image-20210108090130200.png)

## 单节点集群

![image-20210108090239399](img/ElasticSearch核心技术与实践二/image-20210108090239399.png)

## 再增加一个数据节点

![image-20210108090257963](img/ElasticSearch核心技术与实践二/image-20210108090257963.png)

## 故障转移

## ![image-20210108090315460](img/ElasticSearch核心技术与实践二/image-20210108090315460.png) 

## 集群健康状态

![image-20210108090552675](img/ElasticSearch核心技术与实践二/image-20210108090552675.png)

# 文档分布式存储

![image-20210108090732376](img/ElasticSearch核心技术与实践二/image-20210108090732376.png)

##  文档到分片的路由算法

![image-20210108090822702](img/ElasticSearch核心技术与实践二/image-20210108090822702.png)

## 更新一个文档

![image-20210108090958973](img/ElasticSearch核心技术与实践二/image-20210108090958973.png)

## 删除一个文档

![image-20210108091014971](img/ElasticSearch核心技术与实践二/image-20210108091014971.png)

# 分片的生命周期

## 分片的内部原理

![image-20210108091249925](img/ElasticSearch核心技术与实践二/image-20210108091249925.png)

## 倒排索引不可变性

![image-20210108091355792](img/ElasticSearch核心技术与实践二/image-20210108091355792.png)

## Lucene Index

![image-20210108091454806](img/ElasticSearch核心技术与实践二/image-20210108091454806.png)

## 什么是 Refresh

![image-20210108091628226](img/ElasticSearch核心技术与实践二/image-20210108091628226.png)

## 什么是 Transaction Log

![image-20210108091801972](img/ElasticSearch核心技术与实践二/image-20210108091801972.png)

## 什么是 Flush

![image-20210108092030517](img/ElasticSearch核心技术与实践二/image-20210108092030517.png)

## Merge

![image-20210108092112508](img/ElasticSearch核心技术与实践二/image-20210108092112508.png)

