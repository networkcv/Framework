## JPA
    由Sun公司提供了一对对于持久层操作的标准(接口+文档)
## Hibernate
    是Gavin King开发的一套对于持久层操作的自动的ORM框架
## HibernateJPA
    是在Hibernate3.2版本中提供了对于JPA的标准的实现，提供了JPA标准来实现持久层开发的API

## Hibernate
    hql
        hibernate query language
        就是将原来的sql语句中的表与字段名称换成对象与属性的名称就可以了
        public List<User> selectUserByUser(String username){
            Session session=template.getSessionFactory().getCurrentSession();
            Query query= session.createQuery("from User where username = :abc");
            query=query.setString("abc",username);
            return query.list();
        }
    sql
        public List<User> selectUserByUser(String username){
            Session session=template.getSessionFactory().getCurrentSession();
            Query query= session.createSQLQuery("select * from t_user where user_name = ?");
            query.addEntity(User.class).setString(0,username);
            return query.list;
        }
    qbc
        query by criteria
        将对数据库的操作转为对对象的方法调用,以面向对象的形式操作数据库
        public List<User> selectUserByUser(String username){
            Session session=template.getSessionFactory().getCurrentSession();
            Criteria c=session.createCriteria(User.class);
            c.add(Restrictions.eq("username",username));
        }

## Hibernate 与 Spring 整合
    将SessionFactory交由Spring容器来管理，SessionFactory中包含dataSource属性
        <bean id="sessionFactory" class="org.springframework.orm.hibernate5.LocalSessionFactoryBean">
            <property name="dataSource" ref="dataSource"/>
            <!-- hibernateProperties属性：配置与hibernate相关的内容，如显示sql语句，开启正向工程 -->
            <property name="hibernateProperties">
                <props>
                    <!-- 显示当前执行的sql语句 -->
                    <prop key="hibernate.show_sql">true</prop>
                    <!-- 开启正向工程 -->
                    <prop key="hibernate.hbm2ddl.auto">update</prop>
                </props>
            </property>
            <!-- 扫描实体所在的包 -->
            <property name="packagesToScan">
                <list>
                    <value>com.lwj.pojo</value>
                </list>
            </property>
        </bean>

    所以还需要配置dataSource数据源
        <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
            <property name="jdbcUrl" value="${jdbc.url}"/>
            <property name="driverClass" value="${jdbc.driver.class}"/>
            <property name="user" value="${jdbc.username}"/>
            <property name="password" value="${jdbc.password}"/>
        </bean>

    对数据库的操作涉及事务，所以还需注册事务管理器transactionManager
        <bean id="transactionManager" class="org.springframework.orm.hibernate5.HibernateTransactionManager">
            <property name="sessionFactory" ref="sessionFactory"/>
        </bean>

    Hibernate提供了数据库操作的模板HibernateTemplate
        <!--HibernateTemplate-->
        <bean id="hibernateTemplate" class="org.springframework.orm.hibernate5.HibernateTemplate" >
            <property name="sessionFactory" ref="sessionFactory"></property>
        </bean>    

    <!-- 配置开启注解事务处理 -->
	    <tx:annotation-driven transaction-manager="transactionManager"/>
	
	<!-- 配置springIOC的注解扫描 -->
	    <context:component-scan base-package="com.lwj"/>


## Spring 整合HibernateJPA
    <!--使用JPA规范 Spring整合JPA 配置EntityManagerFactory-->
	<bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
		<property name="dataSource" ref="dataSource"/>
		<property name="jpaVendorAdapter" >
			<bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter">
				<property name="database" value="MYSQL"/>
				<!--正向工程 自动创建表-->
				<property name="generateDdl" value="true"/>
				<property name="showSql" value="true"/>
			</bean>
		</property>
		<property name="packagesToScan" value="com.lwj.pojo"/>
		<!--<property name="packagesToScan">-->
			<!--<list>-->
				<!--<value>com.lwj.pojo</value>-->
			<!--</list>-->
		<!--</property>-->
	</bean>

    <!--使用JpaHibernate的事务管理器-->
	<bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
		<property name="entityManagerFactory" ref="entityManagerFactory"/>
	</bean>

    public class JPAUserDaoImpl implements UserDao {
        //类似于HibernateTemplate
        @PersistenceContext(name = "entityManagerFactory")
        private EntityManager entityManager;
    
        public void inesrtUser(User user) {
            entityManager.persist(user);
        }

        public void updateUser(User user) {
            entityManager.merge(user);
        }

        public void deleteUser(User user) {
            entityManager.remove(user);
        }

        public User selectUserById(Integer userid) {
            return entityManager.find(User.class,userid);
        }

        public List<User> selectUserByName(String username){
            //hql
            return entityManager.createQuery("from User where username = :abc").setParameter("abc",username).getResultList();
        }

        public List<User> selectUertByNameWithJPASQL(String username){
            //sql
            //Hibernate JPA中和JDBC中一样，参数绑定的索引从1开始，HIbernate是从0开始
            return entityManager.createNativeQuery("select * from t_user where username= ?", User.class).setParameter(1, username).getResultList();
        }
    }

## Spring Data JPA 
- Spring Data JPA 是Spring Data 项目下的一个模块。提供了一套基于JPA标准操作数据库的简化方案。
底层默认是依赖Hibernate JPA 来实现的。

- 依赖关系：Spring Data JPA -> Hibernate JPA  -> Hibernate 

- Spring Data JPA 的技术特点，我们只需要定义接口并集成Spring Data JPA 中所以提供的接口就可以了，
需要写接口实现类。


## Spring Data Redis
Spring Data Redis 是属于Spring Data 下的的一个模块，作用是简化对于Redis的操作  
### 修改pom.xml
```xml
<!--    springboot 整合redis -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
 <!--   protostuff的自定义序列化     -->
        <dependency>
            <groupId>com.dyuproject.protostuff</groupId>
            <artifactId>protostuff-core</artifactId>
            <version>1.1.3</version>
        </dependency>
        <dependency>
            <groupId>com.dyuproject.protostuff</groupId>
            <artifactId>protostuff-runtime</artifactId>
            <version>1.1.3</version>
        </dependency>
```
### 编写配置类
```java
@Configuration
public class RedisConfig {
    //1.创建JedisPollConfig对象，在该对象中完成连接池的配置
    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大空闲数
        config.setMaxIdle(10);
        //最小空闲数
        config.setMinIdle(5);
        //最大连接数
        config.setMaxTotal(20);
        return config;
    }

    //2.创建JedisConnectionFactory 配置redis连接信息
    @Bean
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig config) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        //配置连接池信息
        factory.setPoolConfig(config);
        factory.setHostName("192.168.3.193");
        factory.setPort(6379);
        return factory;
    }

    //3.创建RedisTemplate，用于执行redis操作的方法，value的序列化器使用protostuff序列化器
    @Bean
    public RedisTemplate<String,Object> redisTemplate(JedisConnectionFactory factory){
        RedisTemplate<String,Object> template=new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setEnableTransactionSupport(true);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        //value的序列化器使用protostuff序列化器
        ProtostuffSerializer valueSerializer = new ProtostuffSerializer();
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(valueSerializer);
        return template;
    }
}
```
```java
//自定义的序列化器
public class ProtostuffSerializer implements RedisSerializer<Object> {

    private boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

    private final Schema<ProtoWrapper> schema;

    private final ProtoWrapper wrapper;

    private final LinkedBuffer buffer;

    public ProtostuffSerializer() {
        this.wrapper = new ProtoWrapper();
        this.schema = RuntimeSchema.getSchema(ProtoWrapper.class);
        this.buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    }

    @Override
    public byte[] serialize(Object t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        wrapper.data = t;
        try {
            return ProtostuffIOUtil.toByteArray(wrapper, schema, buffer);
        } finally {
            buffer.clear();
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (isEmpty(bytes)) {
            return null;
        }

        ProtoWrapper newMessage = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(bytes, newMessage, schema);
        return newMessage.data;
    }

    private static class ProtoWrapper {

        public Object data;

    }
}
```