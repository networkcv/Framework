## Log相关

之前使用Commons Logging日志框架和Log4j来实现日志打印。

但由于Log4j中打印日志需要字符串拼接。

```java
log.info("Set score " + score + " for Person " + p.getName() + " ok.");
```

所以现在流行的日志框架SLF4J，搭配Logback来做日志实现。

打印日志就变成了：

```java
logger.info("Set score {} for Person {} ok.", score, p.getName());
```

以这着占位符的方式，使用起来更加便捷。

日志文件的配置可以参考：https://www.liaoxuefeng.com/wiki/1252599548343744/1264739155914176

