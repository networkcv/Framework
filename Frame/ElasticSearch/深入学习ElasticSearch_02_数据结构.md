前面我们了解到ES和MySQL的结构很类似，MySQL中针对不同类型的数据，提供了多种数据类型来满足我们的需求。比如数值类型（int，bigint）来保存数字，字符（char，varchar）类型来保存字符串，还有日期类型（date，datetime）来保存时间。ES的Field同样提供了多种数据类型供我们选择。

## 1、字符类型

> String类型可以和java的string、mysql的varchar等同，但是为何会分为text、keyword呢？这两者又有什么区别？
> ES作为全文检索引擎，它强大的地方就在于**分词**和倒排序索引。而 text 和 keyword 的区别就**在于是否分词**（ps：什么叫分词？举个简单例子，“中国我爱你”这句话，如果使用了分词，那么这句话在底层的储存可能就是“中国”、“我爱你”，**被拆分成了两个关键字**）

**1）text——会分词**

就拿刚才的例子来说，“中国我爱你”这句话，如果使用text类型储存，我们不去特殊定义它的分词器，**那么ES就会使用默认的分词器 standard 。**

![img](https://pic2.zhimg.com/80/v2-7adfac10f01203f7c1bfe41d7015e271_720w.jpg)

由于英文中的句子都是由一个个单词组成，默认的 standard 分词器只需要按词分开即可。同时该分词器也将中文句子中的每个字看做是一个英文单词，所以在分词时会把每个字都拆开，单独存储。这样的话我们就无法根据某个词语来查询对应的文本。

```json
GET _analyze
{
  "text": "中国我爱你",
  "analyzer": "standard"
}

{
  "tokens" : [
    {
      "token" : "中",
      "start_offset" : 0,
      "end_offset" : 1,
      "type" : "<IDEOGRAPHIC>",
      "position" : 0
    },
    {
      "token" : "国",
      "start_offset" : 1,
      "end_offset" : 2,
      "type" : "<IDEOGRAPHIC>",
      "position" : 1
    },
    {
      "token" : "我",
      "start_offset" : 2,
      "end_offset" : 3,
      "type" : "<IDEOGRAPHIC>",
      "position" : 2
    },
    {
      "token" : "爱",
      "start_offset" : 3,
      "end_offset" : 4,
      "type" : "<IDEOGRAPHIC>",
      "position" : 3
    },
    {
      "token" : "你",
      "start_offset" : 4,
      "end_offset" : 5,
      "type" : "<IDEOGRAPHIC>",
      "position" : 4
    }
  ]
}

```

所以我们需要能够对汉语分词的分词器，它能够识别句中的词语，把多个字组成的词语拆分出来，而不是把句子逐字拆分。

IK分词器就可以帮我们完成这个功能。

```json
GET _analyze
{
  "text": "中国我爱你",
  "analyzer": "ik_max_word"
}

{
  "tokens" : [
    {
      "token" : "中国",
      "start_offset" : 0,
      "end_offset" : 2,
      "type" : "CN_WORD",
      "position" : 0
    },
    {
      "token" : "我爱你",
      "start_offset" : 2,
      "end_offset" : 5,
      "type" : "CN_WORD",
      "position" : 1
    },
    {
      "token" : "爱你",
      "start_offset" : 3,
      "end_offset" : 5,
      "type" : "CN_WORD",
      "position" : 2
    }
  ]
}
```

IK分词器不止有 Ik_max_word 模式，还有另外一种分词模式叫 ik_smart 模式。ik_max_word 会做最细粒度的拆分，而 ik_smart 会做最粗粒度的划分。如果查询的关键字可以才词库中查到，那么就不会再去进行分词。

```json
GET _analyze
{
  "text": "中国我爱你",
  "analyzer": "ik_smart"
}

{
  "tokens" : [
    {
      "token" : "中国",
      "start_offset" : 0,
      "end_offset" : 2,
      "type" : "CN_WORD",
      "position" : 0
    },
    {
      "token" : "我爱你",
      "start_offset" : 2,
      "end_offset" : 5,
      "type" : "CN_WORD",
      "position" : 1
    }
  ]
}

```

两种分词器使用的最佳实践是：索引时用ik_max_word，在搜索时用ik_smart。

即：索引时最大化的将文章内容分词，搜索时更精确的搜索到想要的结果。

比如我输入 “苹果手机” 这个词，这时候我希望搜索与苹果手机相关的内容，就可以将 “苹果手机”添加到自定义词库，这样就不会返回和苹果相关以及和其他手机相关的查询内容了。

添加自定义词库方式：

1. 新建词库文件

```shell
/usr/local/etc/elasticsearch/analysis-ik/vi mydic.dic
```

mydic.dic 文件内容：

```txttx t
英雄联盟
```
2. 修改IK分词器配置文件

   IKAnalyzer.cfg.xml：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
   <properties>
   	<comment>IK Analyzer 扩展配置</comment>
   	<!--用户可以在这里配置自己的扩展字典 -->
   	<entry key="ext_dict">mydic.dic</entry>
   	 <!--用户可以在这里配置自己的扩展停止词字典-->
   	<entry key="ext_stopwords"></entry>
   	<!--用户可以在这里配置远程扩展字典 -->
   	<!-- <entry key="remote_ext_dict">words_location</entry> -->
   	<!--用户可以在这里配置远程扩展停止词字典-->
   	<!-- <entry key="remote_ext_stopwords">words_location</entry> -->
   </properties>
   ```

**2）keywor——不会分词**

我们新增一个keyword类型的字段field2，再来看看检索效果：

```text
# 测试分词效果
GET /_analyze
{
  "text": ["中国我爱你"],
  "analyzer": "keyword"
}
# 结果
{
  "tokens": [
    {
      "token": "中国我爱你",
      "start_offset": 0,
      "end_offset": 5,
      "type": "word",
      "position": 0
    }
  ]
}
```

可以发现，类型为keyword，**通过term是可以查询到，说明ES对keyword是没有分词的。**

## 2、时间类型

对于date类型，和mysql的几乎一样，唯一的注意点就是，**储存的格式，ES是可以控制的。并且可规定格式**

```text
PUT my_index
{
  "mappings": {
      "properties": {
        "date": {
          "type": "date" 
      }
    }
  }
}

PUT my_index/_doc/1
{ "date": "2015-01-01" } 

PUT my_index/_doc/2
{ "date": "2015-01-01T12:10:30Z" } 

PUT my_index/_doc/3
{ "date": 1420070400001 } 

GET my_index/_search
{
  "sort": { "date": "asc"} 
}
```

大家可以用kibana试试，**3种格式都可以的。**

同时ES的date类型**允许我们规定格式**，可以使用的格式有：

> yyyy-MM-dd HH:mm:ss
> yyyy-MM-dd
> epoch_millis（毫秒值）

```text
# 规定格式如下： || 表示或者
PUT my_index
{
  "mappings": {
    "_doc": {
      "properties": {
        "date": {
          "type":   "date",
          "format": "yyyy-MM-dd HH:mm:ss||yyyy-MM-dd||epoch_millis"
        }
      }
    }
  }
}
```

**注意：一旦我们规定了格式，如果新增数据不符合这个格式，ES将会报错mapper_parsing_exception。**

## 3、复杂类型

> ES的复杂类型有3个，Array、object、nested。

1）Array：在Elasticsearch中，**数组不需要专用的字段数据类型**。默认情况下，**任何字段都可以包含零个或多个值**，但是，数组中的所有值都**必须具有相同的数据类型。**

> 举个简单例子理解下：比如上一个例子中的field1这个字段，可以只存储一个值“中国我爱你”，同时也可以存储一个数组：["这是","一个","数组"]

```text
# 新增数据
POST /toherotest/_doc/2
{
  "field1":["这是","一个","数组"]
}
```

![img](https://pic4.zhimg.com/80/v2-1d89af53e819b4fc0ba9a56f66b24687_720w.jpg)

2）object我相信大家都能理解；需要注意的是，**object类型的字段，也可以有多个值**，形成List<object>的数据结构。

> 重点：List<object>中的**object不允许彼此独立地索引查询**。这是什么意思呢？

举个简单例子：我们现在有2条数据：数据结构都是一个List<object>

> \# 第一条数据：[ { "name":"tohero1", "age":1 }, { "name":"tohero2", "age":2 } ]
> \# 第二条数据： [ { "name":"tohero1", "age":2 }, { "name":"tohero2", "age":1 } ]

如果此时我们的需求是，只要 **name = “tohero1”and “age”= 1** 的数据，根据我们常规的理解，只有第一条数据才能被检索出来，但是真的是这样么？我们写个例子看看：

```text
# 添加 属性为object的字段 field3
PUT toherotest/_mapping/_doc 
{
  "properties": {
    "field3": {
      "type": "object"
    }
  }
}
# 新增数据
POST /toherotest/_doc/3
{
  "field3":[ { "name":"tohero1", "age":1 }, { "name":"tohero2", "age":2 } ]
}
  
POST /toherotest/_doc/4
{
  "field3": [ { "name":"tohero1", "age":2 }, { "name":"tohero2", "age":1 } ]
}

#执行查询语句 
GET /toherotest/_doc/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "field3.name": "tohero1"
          }
        },
        {
          "term": {
            "field3.age": 1
          }
        }
      ]
    }
  }
}
```

> （ps：现在看不懂查询语句，没关系，**主要是理解几个类型的差异，**可以后面学了查询语句再回头看查询语句）查询语句等价于mysql的 **where** **name = “tohero1”and “age”= 1**

**查询结果如下：**

```text
{
  "took": 2,
  "timed_out": false,
  "_shards": {
    "total": 5,
    "successful": 5,
    "skipped": 0,
    "failed": 0
  },
  "hits": {
    "total": 2,
    "max_score": 1.287682,
    "hits": [
      {
        "_index": "toherotest",
        "_type": "_doc",
        "_id": "4",
        "_score": 1.287682,
        "_source": {
          "field3": [
            {
              "name": "tohero1",
              "age": 2
            },
            {
              "name": "tohero2",
              "age": 1
            }
          ]
        }
      },
      {
        "_index": "toherotest",
        "_type": "_doc",
        "_id": "3",
        "_score": 1.287682,
        "_source": {
          "field3": [
            {
              "name": "tohero1",
              "age": 1
            },
            {
              "name": "tohero2",
              "age": 2
            }
          ]
        }
      }
    ]
  }
}
```

**可以看到两条数据都被我们检索到了。**所以，现在理解什么叫做“**object不允许彼此独立地索引查询”**了吧。

但是，我们在日常的使用过程中，常规的需求就是，**希望object能被独立的索引，**难道es满足不了这个需求么？那是不可能。下面就来看下nested类型。

**3）nested 类型**

> **需要建立对象数组的索引并保持数组中每个对象的独立性，则应使用nested数据类型而不是 object数据类型。在内部，嵌套对象索引阵列作为一个单独的隐藏文档中的每个对象，这意味着每个嵌套的对象可以被独立的查询。**

备注：关于nested类型，TeHero在此就不写实例了，因为对于nested的应用本身属于ES的高级操作**，后面TeHero会单独出一期关于nested的使用教程。**

**对于复杂类型，目前先知道 object和nested类型的区别即可。**

## **4、GEO 地理位置类型**

> 对于 GEO 地理位置类型，分为 地图：Geo-point 和 形状 ：Geo-shape，两种数据类型。
> 对于web开发，一般常用的是 地图类型 Geo-point。
> 要求：先知道如何定义，如何查询即可

```text
PUT my_index
{
  "mappings": {
    "_doc": {
      "properties": {
        "location": {
          "type": "geo_point"
        }
      }
    }
  }
}

PUT my_index/_doc/1
{
  "text": "Geo-point as an object",
  "location": { 
    "lat": 41.12,
    "lon": -71.34
  }
}

PUT my_index/_doc/2
{
  "text": "Geo-point as a string",
  "location": "41.12,-71.34" 
}

PUT my_index/_doc/3
{
  "text": "Geo-point as a geohash",
  "location": "drm3btev3e86" 
}

PUT my_index/_doc/4
{
  "text": "Geo-point as an array",
  "location": [ -71.34, 41.12 ] 
}
```

距离查询：距离某个点方圆200km

```text
GET /my_locations/_search
{
    "query": {
        "bool" : {
            "must" : {
                "match_all" : {}
            },
            "filter" : {
                "geo_distance" : {
                    "distance" : "200km",
                    "pin.location" : {
                        "lat" : 40,
                        "lon" : -70
                    }
                }
            }
        }
    }
}
```

