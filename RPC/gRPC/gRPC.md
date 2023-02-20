## [protobuf为什么那么快？](https://www.jianshu.com/p/72108f0aefca)

1.对传输DTO的key进行排序，压缩和解析的时候是根据序号来进行解析的。这样就可以不用传key。所以需要注意不要随意修改proto文件中字段的顺序。