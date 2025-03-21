# JDK21 JVM推荐配置及说明

根据应用场景选择合适的 GC：延迟敏感选 ZGC，吞吐优先选 G1



## ZGC推荐配置【延迟敏感】

-XX:+UseContainerSupport -XX:InitialCodeCacheSize=120MB -XX:InitialRAMPercentage=70.0 -XX:MinRAMPercentage=70 -XX:MaxRAMPercentage=70.0 -XX:+AlwaysPreTouch -XX:+UseZGC -XX:+ZGenerational -Xlog:gc*:file=/opt/zcy/modules/gc-%t.log:time,uptime,level,tags::filecount=5,filesize=10m



## G1推荐配置【吞吐优先】

-XX:+UseContainerSupport -XX:InitialCodeCacheSize=120MB -XX:InitialRAMPercentage=70.0 -XX:MinRAMPercentage=70 -XX:MaxRAMPercentage=70.0 -XX:+AlwaysPreTouch -Xlog:gc*:file=/opt/zcy/modules/gc-%t.log:time,uptime,level,tags::filecount=5,filesize=10m



## 配置说明

| 配置项                                                       | 说明                                                         | 默认值 | 推荐值 |
| :----------------------------------------------------------- | :----------------------------------------------------------- | :----- | :----- |
| -XX:+UseContainerSupport                                     | 启用容器化支持                                               |        |        |
| -XX:InitialRAMPercentage                                     | 初始化堆内存百分比                                           | 1.56   | 70~75  |
| -XX:MinRAMPercentage                                         | 最小堆内存百分比                                             | 50     | 70~75  |
| -XX:MaxRAMPercentage                                         | 最大堆内存百分比                                             | 25     | 70~75  |
| -Xlog:gc*:file=/opt/zcy/modules/gc-%t.log:time,uptime,level,tags::filecount=5,filesize=10m | -Xlog:gc*：启用所有与 GC 相关的日志记录。 file=gc.log：指定日志文件名为 gc.log。 time,uptime,level,tags：在日志中包含时间戳、JVM 启动时间、日志级别和标签。 filecount=5,filesize=10M：设置日志文件的最大数量为 5 个，每个文件的最大大小为 10MB。 |        |        |
| -XX:InitialCodeCacheSize                                     | 指定 JVM 启动时初始分配的代码缓存大小                        | 2.5MB  | 120MB  |
| -XX:ReservedCodeCacheSize                                    | 指定代码缓存的最大大小。当代码缓存达到该值时，JVM 将停止编译新的代码，可能会导致性能下降 | 240MB  | 240MB  |

[从java8到java17](https://cf.cai-inc.com/pages/viewpage.action?pageId=118106240)

# JDK 21


  字符串模板（预览）
    增强 Java 编程语言，使用字符串模板。字符串模板通过将文本与嵌入的表达式和模板处理器相结合，补充了 Java 现有的字符串字面量和文本块，以产生特殊的结果。这是一个预览语言特性和 API。
  Sequenced 有序集合
    介绍用于表示具有定义的遭遇顺序的集合的新接口。每个这样的集合都有一个明确的第一元素、第二元素，依此类推，直到最后一个元素。它还提供了统一的 API 来访问其第一和最后一个元素，以及以相反顺序处理其元素。
   **Generational ZGC**
    通过扩展 Z 垃圾回收器（ZGC）以维护年轻和旧对象的独立代，提高应用程序性能。这将允许 ZGC 更频繁地收集年轻对象——这些对象往往寿命短暂。
    -XX:+UseZGC -XX:+ZGenerational
  **record 记录模式**
  **switch 模式匹配**
  **虚拟线程**
  准备禁止动态加载代理
  外部函数与内存 API（第三预览）
  未命名模式和变量（预览）
    增强 Java 语言，使用未命名的模式匹配记录组件，无需声明组件的名称或类型，以及未命名的变量，可以初始化但不能使用。这两个都由下划线字符表示， _ 。
  未命名的类和实例主方法（预览）
    Java 语言进化，使学生能够在无需理解为大程序设计的语言特性下编写他们的第一个程序。远非使用 Java 的独立方言，学生可以为单类程序编写精简的声明，然后随着技能的增长无缝扩展他们的程序以使用更高级的特性。
  Scoped Values (Preview)
  向量 API（第六个孵化器）
  弃用 Windows 32 位 x86 端口以移除
  准备禁止动态加载JVM代理
    当代理动态加载到正在运行的 JVM 时发出警告。这些警告旨在为用户准备未来版本，该版本默认不允许动态加载代理，以提高默认的完整性。在启动时加载代理的服务性工具不会在任何版本中引发警告。
    as支持21
  密钥封装机制 API
    介绍一种用于密钥封装机制（KEMs）的 API，这是一种使用公钥密码学来保护对称密钥的加密技术。
  结构化并发（预览）

# JDK 20


  作用域值Scoped Values（孵化器）
    介绍作用域值，它允许在线程内部和跨线程之间共享不可变数据。它们比线程局部变量更受欢迎，尤其是在使用大量虚拟线程时。这是一个孵化 API。
  记录模式（第二次预览）
  switch 的模式匹配（第四次预览）
  外部函数与内存 API（第二次预览）
  虚拟线程（第二次预览）
  结构化并发（第二次孵化）
  向量 API（第五孵化器）

# JDK 19


  record记录模式（预览版）
    使用记录模式增强 Java 编程语言，以解构记录值。记录模式和类型模式可以嵌套，以实现强大的声明式和可组合的数据导航和处理形式。这是一项预览语言功能。
  Linux/RISC-V Port
    将 JDK 移植到 Linux/RISC-V。
    RISC-V 是一种免费的开源 RISC 指令集架构 （ISA），最初由加州大学伯克利分校设计，现在在 RISC-V International 的赞助下合作开发。它已经被广泛的语言工具链支持。随着 RISC-V 硬件可用性的提高，JDK 的端口将很有价值。
  外部函数和内存 API（预览版）
  虚拟线程（预览版）
    将虚拟线程引入 Java 平台。虚拟线程是轻量级线程，可显著减少编写、维护和观察高吞吐量并发应用程序的工作量。
  Vector API（第四个培养箱）
  switch的模式匹配（第三次预览）
  结构化并发 （孵化器）
    通过引入结构化并发 API 来简化多线程编程。结构化并发将在不同线程中运行的多个任务视为单个工作单元，从而简化错误处理和取消、提高可靠性并增强可观测性。

# JDK 18


  默认为 UTF-8
    指定 UTF-8 作为标准 Java API 的默认字符集。通过此更改，依赖于默认字符集的 API 将在所有实现、作系统、区域设置和配置中保持一致的行为。
  简单 Web 服务器
    提供命令行工具以启动仅提供静态文件的最小 Web 服务器。没有可用的 CGI 或类似 servlet 的功能。该工具将用于原型设计、临时编码和测试目的，尤其是在教育环境中。
  Java API 文档中的代码片段
    为 JavaDoc 的标准 Doclet 引入 @snippet 标记，以简化在 API 文档中包含示例源代码的过程。
    用于jshell的代码片段
  使用方法句柄重新实现核心反射
    在 java.lang.invoke 方法句柄之上重新实现 java.lang.reflect.Method、Constructor 和 Field。将方法处理用于反射的底层机制将降低 java.lang.reflect 和 java.lang.invoke API 的维护和开发成本。
     -Djdk.reflect.useDirectMethodHandle=false 启用旧实现
  矢量 API（第三个培养箱）
    引入一个 API 来表达向量计算，这些计算在运行时可靠地编译为受支持的 CPU 架构上的最佳向量指令，从而实现优于等效标量计算的性能。
  Internet 地址解析 SPI
    为主机名和地址解析定义服务提供商接口 （SPI），以便 [java.net](http://java.net/).InetAddress 可以使用平台内置解析程序以外的解析程序。
  外部函数和内存 API（第二个孵化器）
    引入一个 API，通过该 API，Java 程序可以与 Java 运行时之外的代码和数据进行互作。通过高效调用外部函数（即 JVM 外部的代码）和安全访问外部内存（即不受 JVM 管理的内存），API 使 Java 程序能够调用本机库并处理本机数据，而不会出现 JNI 的脆弱性和危险性。
  switch 的模式匹配（第二次预览）
    通过对 switch 表达式和语句进行模式匹配，以及对模式语言的扩展，增强 Java 编程语言。将模式匹配扩展到 switch，允许针对多个模式测试表达式，每个模式都有一个特定的作，以便可以简洁安全地表达面向数据的复杂查询。这是 JDK 18 中的预览语言功能。
  弃用 Finalization for Removal