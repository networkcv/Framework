## 基于Trem的查询

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

32



 