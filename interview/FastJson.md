# FastJson 

## fastjson  简单介绍
- #### JSON  
    fastJson的解析器，用于JSON格式字符串与JSON对象及javaBean之间的转换 
- #### JSONObject  
    fastJson提供用来封装Bean的JSON对象
    ```java
    JSONObject extends JSON implements Map<String, Object>, Cloneable, Serializable, InvocationHandler 
    ```
- #### JSONArray：  
    fastJson提供json数组对象
    ```java
    JSONArray extends JSON implements List<Object>, Cloneable, RandomAccess, Serializable
    ```
## fastjson  常用API 
> JSON这个类是fastjson API的入口，主要的功能都通过这个类提供。

#### 序列化API

```java
package com.alibaba.fastjson;

public abstract class JSON {
    // 将Java对象序列化为JSON字符串，支持各种各种Java基本类型和JavaBean
    public static String toJSONString(Object object, SerializerFeature... features);

    // 将Java对象序列化为JSON字符串，返回JSON字符串的utf-8 bytes
    public static byte[] toJSONBytes(Object object, SerializerFeature... features);

    // 将Java对象序列化为JSON字符串，写入到Writer中
    public static void writeJSONString(Writer writer, 
                                       Object object, 
                                       SerializerFeature... features);

    // 将Java对象序列化为JSON字符串，按UTF-8编码写入到OutputStream中
    public static final int writeJSONString(OutputStream os, // 
                                            Object object, // 
                                            SerializerFeature... features);
}
```

#### 反序列化API

```java
package com.alibaba.fastjson;

public abstract class JSON {
    // 将JSON字符串反序列化为JavaBean
    public static <T> T parseObject(String jsonStr, 
                                    Class<T> clazz, 
                                    Feature... features);

    // 将JSON字符串反序列化为JavaBean
    public static <T> T parseObject(byte[] jsonBytes,  // UTF-8格式的JSON字符串
                                    Class<T> clazz, 
                                    Feature... features);

    // 将JSON字符串反序列化为泛型类型的JavaBean
    public static <T> T parseObject(String text, 
                                    TypeReference<T> type, 
                                    Feature... features);

    // 将JSON字符串反序列为JSONObject
    public static JSONObject parseObject(String text);
}
```


## fastjson 扩展指定属性的序列化/反序列化类
DefaultJSONParser的parseObject中，通过ParserConfig去获得相应的反序列化类，并通过反序列化类去反序列化出我们需要解析的对象。

https://www.w3cschool.cn/fastjson/fastjson-parseprocess.html

test.class
JSON.parseObject(json3, User.class)

JSON.class
public static <T> T parseObject(String text, Class<T> clazz) {

JSON.class
parseObject(String json, Class<T> clazz, Feature... features)
    return parseObject(json, clazz, ParserConfig.global, (ParseProcess)null, DEFAULT_PARSER_FEATURE, features);

JSON.class
parseObject(String input, Type clazz, ParserConfig config, ParseProcess processor, int featureValues, Feature... features)
    DefaultJSONParser parser = new DefaultJSONParser(input, config, featureValues);
    parser.parseObject(clazz, (Object)null);

DefaultJSONParser.class
parseObject(Type type, Object fieldName)
    int token = this.lexer.token();
    ObjectDeserializer derializer = this.config.getDeserializer(type);
    return derializer.getClass() == JavaBeanDeserializer.class ? ((JavaBeanDeserializer)derializer).deserialze(this, type, fieldName, 0) : derializer.deserialze(this, type, fieldName);

JavaBeanDeserializer.class
public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, int features) 
    return this.deserialze(parser, type, fieldName, (Object)null, features, (int[])null);

JavaBeanDeserializer.class
protected <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName, Object object, int features, int[] setFlags) {
    JSONLexerBase lexer = (JSONLexerBase)parser.lexer;      JSON的基本词法分析器
    ParserConfig config = parser.getConfig();
    int token = lexer.token();

