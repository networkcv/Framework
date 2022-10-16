## 1.读取配置⽂件 
读取完成以后以流的形式存在，我们不能将读取到的配置信息以流的形式存放在内存中，不好操作，可 以创建javaBean来存储 
1. Configuration : 存放数据库基本信息、Map<唯⼀标识，Mapper> 唯⼀标识：namespace + "." + id 
2. MappedStatement：sql语句、statement类型、输⼊参数java类型、输出参数java类型

## 2.解析配置⽂件 
创建sqlSessionFactoryBuilder类： ⽅法：sqlSessionFactory build()：
1. 使⽤dom4j解析配置⽂件，将解析出来的内容封装到Configuration和MappedStatement中
2. 创建SqlSessionFactory的实现类DefaultSqlSession

## 3.创建SqlSessionFactory
- ⽅法：openSession() : 获取sqlSession接⼝的实现类实例对象

## 4.创建sqlSession接⼝及实现类
主要封装crud⽅法 
⽅法：
- selectList(String statementId,Object param)：查询所有 
- selectOne(String statementId,Object param)：查询单个 具体实现：封装JDBC完成对数据库表的查询操作