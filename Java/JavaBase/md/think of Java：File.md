URI  URL   Path  File 

```java
URL resources = this.getClass().getClassLoader().getResource("classpath下的路径");
Path path=Path.of(resource.getFile()); //JDK11
//使用nio包下的Files工具类遍历path下的文件，需要传入一个观察者对象
Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        return super.visitFile(file, attrs);
                    }
                })
```



如何在异步任务中处理异步任务



## getResourceAsStream

在java项目中会经常用到getResourceAsStream这个函数获取一些配置文件，但是怎样正确使用这个函数呢？

.
 |── auth.server.properties
 |── com
│   |── winwill
│   |   └── test
│   |        └── TestGetResourceAsStream.class
 |── config
│   └── config2.properties
 |── config.properties

  config.properties 这个文件的的根目录路径是/config.properties，config2.properties的是/config/config2.properties。

下边这两个是相对于TestGetResourceAsStream.class这个class文件来获取资源。

**通过绝对路径获取文件**

```java
public class TestGetResourceAsStream {
    @Test
    public void getResourceClassAndFileInSamePackage() throws IOException {
        // 获取config2.properties
        InputStream config2 = this.getClass().getResourceAsStream("/config/config2.properties");
 
        // 获取config.properties
        InputStream config = this.getClass().getResourceAsStream("/config.properties");
    }
```

**通过相对路径获取文件**

```java
public class TestGetResourceAsStream {
    @Test
    public void getResourceClassAndFileInSamePackage() throws IOException {
        // 获取config2.properties
        InputStream config2 = this.getClass().getResourceAsStream("../../../config/config2.properties");
 
        // 获取config.properties
        InputStream config = this.getClass().getResourceAsStream("../../../config.properties");
        System.out.println(config + " " + config2);
    }
}
```

**如果用类加载器获取资源那么会有所不同**。

类加载器默认使用的就是class文件根目录，**所以再获取对应文件的路上前不能加 `/`**。

```java
   @Test
    public void getResourceClassAndFileInSamePackage() throws IOException {
        // 获取config2.properties
        InputStream config2 = this.getClass().getClassLoader().getResourceAsStream("config/config2.properties");
 
        // 获取config.properties
        InputStream config = this.getClass().getClassLoader().getResourceAsStream("config.properties");
    }

```



