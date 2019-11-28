默认读取静态文件位置：
    1.resource下的static目录
    2.webapp目录

使用模板引擎的页面存放位置：
    resouces下templates
    该目录是安全的，意味着该目录下的内容是不允许外界直接访问的

freemakeer:
    文件格式为  .ftl
    <#list users as user>
        ${user.userId}
        ${user.userName}
    </#list>

thymeleaf：詹姆拉夫
    Thymeleaf是通过特定语法对html的标记做渲染

    th:text  在页面中输出值，不能含有空格
        th:text="hello"
        th:text="${msg}"

    th:value   可以将一个值放入到input标签的value中
    th:field   ${user.name} 用来做数据的回显
    th:errors  ${users.name} 读取返回的错误信息

    ${#strings.isEmpty(key)} 判断字符串是否为空
    ${#strings.contains(msg,'T')} 判断字符串是否包含指定子串
    ${#strings.startsWith(msg,'a')} 判断字符串是否以字符串开头
    ${#strings.endsWith(msg,'a')} 判断字符串是否以字符串结尾
    ${#strings.length(msg)} 判断字符串长度
    ${#strings.indexOf(msg，'a')} 查找字串的位置，并返回下标，没有返回-1
    ${#strings.substring(msg，1)} 截取字符串，从1到末尾
    ${#strings.substring(msg，1,5)} 截取字符串，从索引1到5
    ${#strings.toUpperCase(msg)} 字符串转大写
    ${#strings.toLowerCase(msg)} 字符串转小写

    ${#dates.format(key)}   格式化日期，以浏览器默认格式语言为标准
    ${#dates.format(key,'yyyy/mm/dd')}  以指定格式格式化日期
    ${#dates.year(key)}   取年
    ${#dates.month(key)}  取月  
    ${#dates.day(key)}    取日

    th:if 条件判断
        th:if="${sex}=='1'"
    th:switch
        <div th:switch="${id}">
            <span th:case="1">1</span>
            <span th:case="2">1</span>

    th:each   迭代遍历
        <tr th:each ="u : ${list}">
            <td> th:text ="${u.userId}"</td>
        </tr>
        <tr th:each ="u.var : ${list}">
            <td> th:text ="${var.index}"</td>
            <td> th:text ="${var.last}"</td>
        </tr>
        index:当前迭代器的索引 从0开始
        count:当前对象的计数  从1开始
        size: 被迭代对象的长度
        even/odd:布尔值，当前循环是否是偶数/奇数 从0开始
        first:布尔值，当前循环是否是第一条
        last:布尔值，当前循环是否是最后一条
        <tr th each = "maps : ${map}">
            <td th:each = "entry:${maps}" th:text="${entry.value.userId}"></td>
            <td th:each = "entry:${maps}" th:text="${entry.value.userName}"></td>
        </tr>

    域对象操作
        <span th:text="${httpServletRequest.getAttribute('req')}"/>
        <span th:text="${session.sess}"/>
        <span th:text="${appliction.app}"/>
    
    URL表达式
        th:href
        th:src
        @{}
        绝对路径
            <a th:href = @{"http://www.baidu.com"}>     <a href = "http://www.baidu.com">
        相对路径
            <a th:href=@{/show}>  相对于项目的根
            <a th:href=@{~/project2/resource/show}>  相对于服务器的根路径
        路径传参
            <a th:href="@{/show(id=1,name=lwj)}">

    使用Thymeleaf的内置对象strings
    调用内置对象一定要有#
    大部分内置的对象的末尾都为s，如strings、numbers、dates
    
    异常解决：
        元素类型"meta",必须匹配结束标记"</meta>"
        1:在html中严格按照语法来写
        2:更换thymeleaf的版本（3.0以上）

MyBatis
    appliction.properties
        spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
        mybatis.type-aliases-package=com.lwj.pojo

服务端表单数据校验
    springBoot对表单做数据校验
        使用hibernate-validator校验框架
            1.需要在实体类中添加校验规则
                @NotBlank(message="用户名不能为空")
                private String password
            2.在Controller的方法中开启校验
                public　String add(@Valid User user){}
            3.在参数中添加用来接收校验结果的变量
                add(@Valid User user,BindingResult result)
                    if(result.hasErrors()){
                        return "add";
                    }
    解决数据校验时代异常问题
        thymeleaf中如果取不到变量则会报错
            1.可以在跳转页面的方法中注入一个Users对象
                springmvc会将对象放入到Model中传递，key的名称会使用对象的驼峰命名规则
            2.如果想为传递的对象更改名称，可以使用@ModelAttribute("u")
                表示当前传递的对象的key为u，在页面中获取该对象的key也需要修改为u
    其他校验规则
        @NotBlank() 判断字符串是否为空或空串，去掉首尾空格
        @NotEmpty(message="") 判断字符串是否为空或空串
        @Length(min=2,max=8,message="")   限制字符串的最大和最小
        @Min/@max   判断数值的最小值和最大值
        @Email(regex="")  判断邮箱的合法性

异常处理
    SpringBoot默认的处理异常的机制: 
        SpringBoot默认提供了一套处理异常的机制。,一旦程序中出现了异常SpringBoot会向/error的url发送请求。
        在springBoot中提供了一个BasicExceptionController来处理/error请求,然后跳转到默认显示异常的页面来展示异常信息。
    SpringBoot对于异常处理提供五种处理方式
        1.自定义错误页面
            缺点：对异常的统一处理，不能针对特定异常进行特殊处理
            在template新建error.html,使用${exception}可以取去异常类型，
            发生错误后可以同一跳转到自定义的错误页面，error.html
        2.@ExceptionHandler 注解处理异常
            缺点：代码冗余，而且只能解决当前Controller的异常处理，不能跨Controller
            @ExceptionHandler(value={java.lang.NullpointerException.class})
            pubilc ModelAndView NullExceptionHandler(Exception e){
                ModelAndView mv = new ModelAndView();
                mv.addObject("error",e.toString);
                mv.setViewName("error2");
                return mv;
            }
        3.@ControllerAdvice+@ExceptionHandler 注解处理异常
            使用AOP的方式，建立通知类，使用@ControllerAdvice标注，
            该类中使用@ExceptionHandler对需要统一处理的异常进行定义
        4.配置SimpleMappingExceptionResolver 处理异常
            对第三种方式的简化，对异常信息和视图跳转注册，无法传递异常信息
            @Configuration
            public class GlobalException{
                @Bean
                public SimpleMappingExceptionResolver getSimpleMappingEceptionResolver{
                    SimpleMappingExceptionResolver resovler=new SimpleMappingExceptionResolver();
                    Properties mappings = new Properties();
                    properties.put("java.lang.NullpointerException","error1");
                    properties.put("java.lang.ArithmeticException","error2");
                    resovler.setExceptionMapping(mappings);
                    return resovler;
                }
            }
        5.通过实现HandlerExceptionResolver接口做处理异常，对第四种方式的扩展
        @Configuration
        public class  GlobalException implements HandlerExceptionResolver{
            public ModelAndView resolverException(httpServletRequest request,HttpServletResponse response,Exception e){
                ModelAndView mv =new ModelAndView();
                if(e instanceof NullpointerException){
                    mv.setViewName("error1");
                }
                if(e instanceof ArithmeticException){
                    mv.setViewName("error2");
                }
                mv.addObject("error",e.toString())
                return mv;
            }
            
        }

SpringBoot整合Junit单元测试
    @RunWith(SpringJUnit4ClassRunner.class)
    @SpingBootTest(classes={Application.class})
    public class UserServiceTest{
    }

    @RunWith:启动器
    SpringJUnit4ClassRunner.class 让junit与spring环境进行整合

    @SpringBootTest(classes={Application.class})
        当前类为springBoot的测试类
        加载SpringBoot启动类，启动Springboot
    
    @ContextConfiguration("classpath:applicationContext.xml")
        spring整合junit4加载配置文件

SpringBoot热启动
    SpringLoader
        针对java代码的热部署，不对页面热部署
        方式一：以maven插件方式使用SpringLoader
            在maven中配置SpringLoader插件
            使用maven命令开启，spring-boot:run
            这种方式缺点是在系统后台以进程的形式运行的，需要手动关闭进程
        方式二：在项目中直接使用jar包的方式
            使用带参数启动项目
                -javaagent:.\lib\springloaded-1.2.5 RELEASE.jar -noverify

    devtools
        DevTools在部署项目时使用的是重新部署的方式
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-devtools</artifactId>
            <optional>true</optional>
	    </dependency>

SpringBoot 缓存技术
    SpringBoot整合Ehcache
        1.修改pom文件
        2.创建Ehcache配置文件，ehcache.xml   位置src/main/resource
        3.修改application.xml   
            spring.cache.ehcache.config=ehcache.xml
        4.启动类上加注解 @EnableCaching
        5.在Service中方法上加注解@Cacheable/@Cacheable(value="user") 使用默认/自定义缓存策略 
            Cacheable(key="#user")
            给存储的值进行标记，查询时比对存储中是否包含该key，如果有则直接返回，没有则查询
          在对数据库有增删改的时候，在方法上加入@Cacheevict(value="user"，allentry=true)  
            删除指定缓存策略的缓存，下次查询时就会重新查询数据库，然后对缓存进行同步，


SpringBoot整合Spring Data Redis
    1.pom.xml
       <!-- Spring Data Redis的启动器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <!-- Test的启动器 -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
        </dependency>
    2.配置类
        @Configuration
        public class RedisConfiguration {
            //1.创建JedisPoolConfig对象，在该对象中完成一些连接池的配置
            @Bean
            public JedisPoolConfig jedisPoolConfig() {
                JedisPoolConfig config = new JedisPoolConfig();
                config.setMaxIdle(10);  //最大空闲数
                config.setMinIdle(5);   //最小空闲数
                config.setMaxTotal(20); //最大连接数
                return config;
            }

            //2.创建JedisCOnnectionFactory.配置redis连接信息
            @Bean("redisFactory")
            public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig config) {
                JedisConnectionFactory factory = new JedisConnectionFactory();
                factory.setPoolConfig(config);  //关联连接池的配置对象
                //配置连接redis的信息
                factory.setHostName("192.168.128.128");
                factory.setPort(6379);
                return factory;
            }

            //3.创建RedisTemplate，用于执行redis操作的方法
            @Bean
            public RedisTemplate<String, Object> redisTemplate(@Qualifier("redisFactory") JedisConnectionFactory factory) {
                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(factory);
                //为key设置序列化器
                template.setKeySerializer(new StringRedisSerializer());
                //为value设置序列化器
                template.setValueSerializer(new StringRedisSerializer());
                return template;
            }
        }
    3.测试
        @RunWith(SpringJUnit4ClassRunner.class)
        @SpringBootTest(classes = SpringbootStudyApplication.class)
        public class SpringbootStudyApplicationTests {
            @Autowired
            private RedisTemplate<String,Object> redisTemplate ;
            @Test
            public void redisTest(){
                redisTemplate.opsForValue().set("jack","rose");
                System.out.println(redisTemplate.opsForValue().get("jack"));
            }
        }

        opsForValue.set()/get() 操作字符串
        opsForHash  操作哈希表
        opsForList  操作列表
        opsForSet   操作无序set
        opsForZSet  操作有序set
    
    4.使用配置文件
        @ConfigurationProperties(prefix="aaa.bbb")
        public JedisPoolConfig jedisPoolConfig(){
            return new JedisPoolConfig();
        }
        在该方法返回JedisPoolConfig后，springboot进行赋值


        applicationContext.properties
            aaa.bbb.max-idle=10
            aaa.bbb.min-idle=5
            aaa.bbb.max-total=20
            
            aaa.bbb.hostName=192.168.128.12
            aaa.bbb.port=4379
    
    5.存储对象
        //使用jdk序列化，将对象序列化后转为字符串，其中由于字节和字符编码不同，在redis-cli中会显示乱码，不过可以正常取出
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.opsForValue().set("user",user);
        redisTemplate.opsForValue().get("user");

        //使用Json格式存储到redis
         redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer());
        redisTemplate.opsForValue().set("user",user);
        redisTemplate.opsForValue().get("user");

Springboot 定时任务
    Scheduled   /ˈskɛdʒʊələ/ 
        1.pom.xml添加坐标
            <dependency>
                <groupId>org.springframework</groupId>
                <artifactId>spring-context-support</artifactId>
            </dependency>

        2.写配置类
        @Component
        public class ScheduedTest {
            @Scheduled(cron = "0/2 * * * *  *")
            public void test(){
                System.out.println("time:" +new Date());
            }
        }

        3.在启动类开启Scheduling
        @SpringBootApplication
        @EnableScheduling
        public class SpringbootStudyApplication {}

        cron表达式：用于定时任务
            Seconds Minutes Hours Day Month Week
            0-59    0-59    0-23  1-31 1-12 1-7(1指周日)
            * 指每一个  ? 表占位   - 表范围  , 表列表
            0/2 表示从0秒开始，2秒为增量步长值
    Quartz
        jar运行
            1.pom.xml添加坐标
                <!--Quartz 定时任务-->
                <dependency>
                    <groupId>org.quartz-scheduler</groupId>
                    <artifactId>quartz</artifactId>
                    <version>2.2.2</version>
                </dependency>

            2.写任务类
                public class QuartzDemo implements Job {
                    /**
                    *  任务执行时触发的方法
                    */
                    @Override
                    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                        System.out.println("execute   "+new Date());
                    }
                }

            3.写启动类
                public class QuartzMain {
                public static void main(String[] args) throws SchedulerException {
                    //1.创建Job对象，做什么事
                    JobDetail job = JobBuilder.newJob(QuartzDemo.class).build();
                    //2.创建Trigger对象，在什么时候做
                    //  1）简单的Trigger触发时间：通过Quartz提供的方法完成简单的重复调用
                //        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(SimpleScheduleBuilder.repeatSecondlyForever()).build();
                    //  2）cron Trigger：通过cron表达式来指定触发时间
                        Trigger trigger = TriggerBuilder.newTrigger().withSchedule(CronScheduleBuilder.cronSchedule("0/2 * * * * ?")).build();

                        //3.创建Scheduler对象，在什么时间做什么事
                        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
                        scheduler.scheduleJob(job, trigger);
                        scheduler.start();
                    }
                }
                
        Springboot整合
            1.pom.xml添加坐标
                <!--Quartz 定时任务-->
                <dependency>
                    <groupId>org.quartz-scheduler</groupId>
                    <artifactId>quartz</artifactId>
                    <version>2.2.2</version>
                </dependency>
                <!--scheduled 定时任务-->
                <dependency>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-context-support</artifactId>
                </dependency>
                <!--事务-->
                <dependency>
                    <groupId>org.springframework</groupId>
                    <artifactId>spring-tx</artifactId>
                </dependency>

            2.写任务类
                public class QuartzDemo implements Job {
                    /**
                    *  任务执行时触发的方法
                    */
                    @Override
                    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
                        System.out.println("execute   "+new Date());
                    }
                }

            3.写配置类
                @Configuration
                public class QuartzConfig {
                    //1.创建job对象
                    @Bean
                    public JobDetailFactoryBean getJobDetailFactoryBean(){
                        JobDetailFactoryBean factory = new JobDetailFactoryBean();
                        factory.setJobClass(QuartzDemo.class);
                        return factory;

                    }
                    //2.1创建简单Trigger对象
                //    @Bean
                //    public SimpleTriggerFactoryBean getSimpleTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean){
                //        SimpleTriggerFactoryBean factory = new SimpleTriggerFactoryBean();
                //        factory.setJobDetail(jobDetailFactoryBean.getObject());
                //        factory.setRepeatInterval(2000);    //间隔时间
                //        factory.setRepeatCount(5);  //次数
                //        return factory;
                //    }
                    //2.2创建CronTrigger对象
                    @Bean
                    public CronTriggerFactoryBean getCronTriggerFactoryBean(JobDetailFactoryBean jobDetailFactoryBean){
                        CronTriggerFactoryBean factory = new CronTriggerFactoryBean();
                        factory.setJobDetail(jobDetailFactoryBean.getObject());
                        factory.setCronExpression("0/2 * * * * ?");
                        return factory;
                    }
                    //3.创建Scheduler对象
                    @Bean
                    public SchedulerFactoryBean getSchedulerFactoryBean(CronTriggerFactoryBean simpleTriggerFactoryBean){
                        SchedulerFactoryBean factory = new SchedulerFactoryBean();
                        factory.setTriggers(simpleTriggerFactoryBean.getObject());
                        return factory;
                    }
                }          

            4.在启动类开启Scheduling
                @SpringBootApplication
                @EnableScheduling	//启动Scheduling定时任务或Quartz定时任务都用这个
                public class SpringbootStudyApplication {}
            5.解决任务类中无法注入的问题
                原因：Quartz产生的对象是反射产生直接返回的，没有由Spring容器管理，Spring需要管理注入和被注入的对象才能注入
                解决方法：继承反射产生对象的BeanFactory，将返回的Bean交由Spring管理后再返回
                
                //控制Factory返回
                @Component("myAdaptableJobFactory")
                public class MyAdaptableJobFactory extends AdaptableJobFactory {
                    @Autowired
                    private AutowireCapableBeanFactory autowireCapableBeanFactory;
                    //该方法将实例化的对象手动添加到SpringIoc容器中并完成对象的注入
                    @Override
                    protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
                        Object obj = super.createJobInstance(bundle);
                        autowireCapableBeanFactory.autowireBean(obj);
                        return obj;

                    }
                }
                
                //修改Quartz对Job任务的创建过程
                @Bean
                public SchedulerFactoryBean getSchedulerFactoryBean(CronTriggerFactoryBean simpleTriggerFactoryBean, MyAdaptableJobFactory myAdaptableJobFactory){
                    SchedulerFactoryBean factory = new SchedulerFactoryBean();
                    factory.setTriggers(simpleTriggerFactoryBean.getObject());
                    factory.setJobFactory(myAdaptableJobFactory);   //使用自己的Factory
                    return factory;
                }               

## SpringBoot整合webSocket

> 参考 https://blog.csdn.net/wangmx1993328/article/details/84582904

- ### pom.xml
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
<!-- Spring boot WebSocket-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-websocket</artifactId>
</dependency>
```

- ### 创建WebSocket服务端
创建后台 webSocket 服务端类，标识 @ServerEndpoint( javax.websocket.server.ServerEndpoint)注解，表示当前类是 webSocket 服务终端，同时在里面实现客户端连接建立、发送消息、接收消息等通信业务。

这与传统导包式开发 Tomcat WebSocket 服务端是一样的，区别就是：传统方式 @ServerEndpoint 类上不需要加 @Component 交由 Spring 管理，而现在需要加上 @Component 将此组件交由 spring 管理。
```java 
/**
 * Created by Administrator on 2018/11/28 0028.
 * @ServerEndpoint ：标识此类为 Tomcat 的 websocket 服务终端，/websocket/yy.action 是客户端连接请求的路径
 * @Component ：将本类交由 spring IOC 容器管理
 */
@ServerEndpoint(value = "/websocket/test")
@Component
public class ServerEnpoint {
 
    private static Logger logger = LoggerFactory.getLogger(ServerEnpoint.class);
 
    /**
     * 用 Set 来 存储 客户端 连接
     */
    private static Set<Session> sessionSet = new HashSet<>();
 
    /**
     * 连接成功后自动触发
     *
     * @param session
     */
    @OnOpen
    public void afterConnectionEstablished(Session session) {
        /**
         * session 表示一个连接会话，整个连接会话过程中它都是固定的，每个不同的连接 session 不同
         * String queryString = session.getQueryString();//获取请求地址中的查询字符串
         * Map<String, List<String>> parameterMap = session.getRequestParameterMap();//获取请求地址中参数
         * Map<String, String> stringMap = session.getPathParameters();
         * URI uri = session.getRequestURI();
         */
        sessionSet.add(session);
        logger.info("新客户端加入，session id=" + session.getId() + ",当前客户端格个数为：" + sessionSet.size());
 
        /**
         * session.getBasicRemote().sendText(textMessage);同步发送
         * session.getAsyncRemote().sendText(textMessage);异步发送
         */
        session.getAsyncRemote().sendText("我是服务器，你连接成功!");
    }
 
    /**
     * 连接断开后自动触发，连接断开后，应该清楚掉 session 集合中的值
     *
     * @param session
     */
    @OnClose
    public void afterConnectionClosed(Session session) {
        sessionSet.remove(session);
        logger.info("客户端断开，session id=" + session.getId() + ",当前客户端格个数为：" + sessionSet.size());
    }
 
    /**
     * 收到客户端消息后自动触发
     *
     * @param session
     * @param textMessage ：客户端传来的文本消息
     */
    @OnMessage
    public void handleMessage(Session session, String textMessage) {
        try {
            logger.info("接收到客户端信息，session id=" + session.getId() + ":" + textMessage);
            /**
             * 原样回复文本消息
             * getBasicRemote：同步发送
             * session.getAsyncRemote().sendText(textMessage);异步发送
             * */
            session.getBasicRemote().sendText(textMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 消息传输错误后
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void handleTransportError(Session session, Throwable throwable) {
        System.out.println("shake client And server handleTransportError,session.getId()=" + session.getId() + " -- " + throwable.getMessage());
        logger.error("与客户端 session id=" + session.getId() + " 通信错误...");
    }
}
```

- ### 使用配置类注册webSocket
注入 org.springframework.web.socket.server.standard.ServerEndpointExporter，这个 bean 会自动注册使用了@ServerEndpoint 注解声明的 Websocket endpoint 。

如果使用独立的 servlet 容器，而不是使用 springboot 的内置容器，就不要注入ServerEndpointExporter，因为它将由 Tomcat 容器自己提供和管理。

因为传统导包式 Tomcat websocket 开发时，是需要实现 javax.websocket.server.ServerApplicationConfig 接口的，然后由它去扫描整个应用中的 @ServerEndpoint，而现在这一步就由 springboot 的 ServerEndpointExporter 取代了。
```java
/**
 * Created by Administrator on 2018/11/28 0028.
 */
@Configuration
public class WebSocketConfig {
    /**
     * 创建 ServerEndpointExporter 组件，交由 spring IOC 容器管理，
     * 它会自动扫描注册应用中所有的 @ServerEndpoint
     *
     * @return
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```