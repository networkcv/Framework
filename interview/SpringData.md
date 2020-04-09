# Spring Data JPA 

## 1. 传统方式访问数据库

### 1.1 JDBC

先写一个获取数据库连接和释放连接的工具类

```java
public class JDBCUtil {
    /**
     * 获取 Connection
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            InputStream inputStream = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            String url = (String) properties.get("jdbc.url");
            String user = (String) properties.get("jdbc.user");
            String password = (String) properties.get("jdbc.password");
            String driverClass = (String) properties.get("jdbc.driverClass");
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }
    // ...
}
```

通过原生的Jdbc进行数据库操作

```java
public class StudentDaoImpl implements StudentDao {
    public List<Student> query() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Student> list = new ArrayList<>();
        try {
            connection = JDBCUtil.getConnection();
            String sql = "select * from student";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Student student = new Student(resultSet.getInt(1), resultSet.getString(2),resultSet.getInt(3));
                list.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement, connection);
        }
        return list;
    }

    @Override
    public void save(Student student) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtil.getConnection();
            String sql = "insert into student(name,age) values(?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, student.getName());
            preparedStatement.setInt(2, student.getAge());
             preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtil.release(resultSet, preparedStatement, connection);
        }
    }
}
```

可以看出有大量的重复代码，两个方法仅是执行的sql不同，我们可以使用模板来完成上述的功能，这样代码会更简单，Spring就提供了这样的模板——Spring JdbcTemplate。

### 1.2 Spring JdbcTemplate

**spring 容器中对应bean的配置，beans.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>

<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="username" value="root"/>
        <property name="password" value="123456"/>
        <property name="url" value="jdbc:mysql://192.168.23.130/springdata?characterEncoding=utf-8&amp;useSSL=false"/>
    </bean>

    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <bean id="studentDao" class="com.lwj.dao.StudentDaoSpringJdbcImpl">
        <property name="jdbcTemplate" ref="jdbcTemplate"/>
    </bean>
</beans>
```

**使用Spring提供的JdbcTemplate实现查询和修改**

```java
public class StudentDaoSpringJdbcImpl implements StudentDao {
    private JdbcTemplate jdbcTemplate;

    @Override
    public List<Student> query() {
        List<Student> list = new ArrayList<>();
        String sql = "select * from student";
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                while (resultSet.next()) {
                    Student student = new Student(resultSet.getInt(1), resultSet.getString(2),
                            resultSet.getInt(3));
                    list.add(student);
                }
            }
        });
        return list;
    }
    
    // ...

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
```

**测试类**

```java
public class SpringJdbcTest {
    ApplicationContext ctx=null;
    @Before
    public void setUp(){
        ctx=new ClassPathXmlApplicationContext("beans.xml");
        System.out.println("setUp");
    }

    @Test
    public void query(){
        System.out.println("query");
        StudentDao studentDao = (StudentDao) ctx.getBean("studentDao");
        List<Student> query = studentDao.query();
        System.out.println(query);
    }

    @After
    public void tearDown(){
        System.out.println("tearDown");
    }
}
```

### 1.3 弊端分析

- 非业务的代码很多，而且很多重复代码
- 需要手动去分页或其他功能

## 2. Spring Data JPA 快速起步

### 2.1 开发环境搭建

传统方式是先建表再 根据 数据表 来创建Bean实体

使用Spring Data JPA 可以根据 Bean实体 来创建 数据表

### 2.2 Spring Data JPA 简单实践

**beans.xml 相关配置**

```xml
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xmlns:jpa="http://www.springframework.org/schema/data/jpa"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
		http://www.springframework.org/schema/data/jpa http://www.springframework.org/schema/data/jpa/spring-jpa-1.3.xsd
		http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-4.0.xsd
		http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-4.0.xsd">

    <!--1 配置数据源-->
    <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
        <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
        <property name="username" value="root"/>
        <property name="password" value="123456"/>
        <property name="url" value="jdbc:mysql://192.168.23.130/springdata?characterEncoding=utf-8&amp;useSSL=false"/>
    </bean>

    <!--2 配置EntityManagerFactory-->
    <bean id="entityManagerFactory" class="org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="jpaVendorAdapter">
            <bean class="org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter"/>
        </property>
        <property name="packagesToScan" value="com.lwj"/>

        <property name="jpaProperties">
            <props>
                <prop key="hibernate.ejb.naming_strategy">org.hibernate.cfg.ImprovedNamingStrategy</prop>
                <prop key="hibernate.dialect">org.hibernate.dialect.MySQL5InnoDBDialect</prop>
                <prop key="hibernate.show_sql">true</prop>
                <prop key="hibernate.format_sql">true</prop>
                <!-- 根据实体生成对应的表，前提是数据库中不存在该表 -->
                <prop key="hibernate.hbm2ddl.auto">update</prop>
            </props>
        </property>
    </bean>

    <!--3 配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.orm.jpa.JpaTransactionManager">
        <property name="entityManagerFactory" ref="entityManagerFactory"/>
    </bean>

    <!--4 配置支持注解的事务-->
    <tx:annotation-driven transaction-manager="transactionManager"/>

    <!--5 配置spring data-->
    <jpa:repositories base-package="com.lwj" entity-manager-factory-ref="entityManagerFactory"/>

    <context:component-scan base-package="com.lwj"/>
</beans>
```

**实体类 相关配置**

```java
/* 
 * JPA 会扫描这个注解，将其加入EntityManagerFactory
 * 如果配置了 <prop key="hibernate.hbm2ddl.auto">update</prop>
 * 则会根据实体生成对应的表，前提是数据库中不存在该表
 */
@Entity	
@Data
public class Employee {
    @Id
    @GeneratedValue
    private Integer employeeId;
    @Column(length = 30)
    private String name;
    @Column(nullable = false)
    private Integer age;
}
```

**Repository 仓库的配置**

```java
// 这里也可以选择不继承 Repository 而是使用 @RepositoryDefinition
public interface EmployeeRepository extends Repository<Employee,Integer> {
    // 方法名需要按照特定规则来编写
    public Employee findByName(String name);
    
    //...
}
```

**编写测试类**

```java
public class EmployeeRepositoryTest {
    ApplicationContext ctx = null;
    EmployeeRepository employeeRepository = null;

    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        employeeRepository = ctx.getBean(EmployeeRepository.class);
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    public void testFindByName(){
        Employee jack = employeeRepository.findByName("jack");
        System.out.println(jack);
    }
}
// Output
// Employee(employeeId=1, name=jack, age=20)
```

## 3. Spring Data JPA 进阶

### 3.1 Repository 接口讲解

- Repository 接口是 Spring Data 的核心接口，不提供任何方法
- public interface Repository <T, ID extends Serializable> {}  标记接口
- @RepositoryDefinition 注解的使用

### 3.2 Repository 的子接口

- **CrudRepository** ：继承 Repository，实现了CRUD相关的方法。
- **PagingAndSortingRepository** ：继承自 CrudRepository，实现分页排序相关的方法。
- **JpaRepository** ：继承自 PagingAndSortingRepositroy，实现JPA规范相关的方法。
- **SimpleJpaRepository**：实现了 JpaRepository，也就是上面这些接口的一个简单实现类。

### 3.3  查询方法 定义规则和使用

继承 Repository 中我们的定义的接口方法可以不用自己实现

**参考规则：**

![image-20200409104700869](img/image-20200409104700869.png)

![image-20200409104759258](img/image-20200409104759258.png)

**Repository 配置**

```java
@RepositoryDefinition(domainClass = Employee.class,idClass = Integer.class)
public interface EmployeeRepository{
    Employee findByName(String name);

    // where name like ?% and age <= ?
    List<Employee> findByNameStartingWithAndAgeLessThanEqual(String name, Integer integer);

    // where name like %? and age > ?
    List<Employee> findByNameEndingWithAndAgeGreaterThan(String name, Integer integer);

    // where name in (?,?...)
    List<Employee> findByNameIn(List<String> names);
}
```

**测试类**

```java
public class EmployeeRepositoryTest {
    ApplicationContext ctx = null;
    EmployeeRepository employeeRepository = null;


    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        employeeRepository = ctx.getBean(EmployeeRepository.class);
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    public void testFindByName() {
        Employee jack = employeeRepository.findByName("jack");
        System.out.println(jack);
    }

    @Test
    public void testFindByNameStartingWithAndAgeLessThan() {
        List<Employee> res = employeeRepository.findByNameStartingWithAndAgeLessThanEqual("test", 23);
        res.forEach(System.out::println);
    }

    @Test
    public void testFindByNameEndingWithAndAgeGreaterThan(){
        List<Employee> res = employeeRepository.findByNameEndingWithAndAgeGreaterThan("3",22);
        res.forEach(System.out::println);
    }

    @Test
    public void testFindByNameIn(){
        System.out.println(employeeRepository);
        List<Employee> res = employeeRepository.findByNameIn(Arrays.asList("test1", "test2"));
        res.forEach(System.out::println);
    }
}
```

**弊端：**

1. 方法名过长，约定大于配置
2. 不适用于复杂Sql

### 3.4 Query 注解的使用

- 在 Repository 方法中使用，不需要遵循查询方法命名规则
- 只需要 @Query 定义在 Repository 中的方法之上即可
- 命名参数及索引参数的使用
- 支持原生的SQL查询

**Repository 配置**

```java
@RepositoryDefinition(domainClass = Employee.class, idClass = Integer.class)
public interface EmployeeRepository {
    // 注意这里的 Employee 是类名，而不是表名
    @Query("select o from Employee o where o.employeeId =(select max(o.employeeId) from Employee)")
    Employee getEmployeeByMaxId();

    // 和JDBC原生的PreparedStatement一样，参数下标从1开始
    @Query(value = "select o from Employee  o where o.age=?1 and o.name=?2 ")
    Employee myQueryEmployeeByNameAndAge(Integer age, String name);
	
    // 类比PreparedStatement的按名称传参
    @Query(value = "select o from Employee  o where o.name=:name and o.age=:age")
    Employee myQueryEmployeeByNameAndAge2(@Param("name") String name, @Param("age") Integer age);

    // nativeQuery 设为 true，开启原生的SQL查询
    @Query(nativeQuery = true, value = "select * from employee where age > :age ")
    List<Employee> myQueryNative();
}
```

**测试类**

```java
public class EmployeeRepositoryTest {
    ApplicationContext ctx = null;
    EmployeeRepository employeeRepository = null;


    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        employeeRepository = ctx.getBean(EmployeeRepository.class);
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    public void testGetEmployeeByMaxId() {
        Employee employee = employeeRepository.getEmployeeByMaxId();
        System.out.println(employee);
    }

    @Test
    public void testMyQueryEmployeeByNameAndAge() {
        Employee employee = employeeRepository.myQueryEmployeeByNameAndAge(20, "jack");
        System.out.println(employee);
    }

    @Test
    public void testMyQueryEmployeeByNameAndAge2() {
        Employee employee = employeeRepository.myQueryEmployeeByNameAndAge2("jack", 20);
        System.out.println(employee);
    }

    @Test
    public void testMyQueryNative(){
        List<Employee> employees = employeeRepository.myQueryNative(20);
        employees.forEach(System.out::println);
    }
}
```

### 3.5 更新及删除操作 整合事务使用

- @Modifying 注解的使用
- @Modifying 结合 @Query 注解执行更新操作
- @Transactional 在 Spring Data 中的使用

```java
// @Modifying 需搭配 @Transactional 使用
@Modifying
// @Transactional 是开启事务的注解，一般需要用在Service层，这里为了方便演示，所以加在Dao接口上
@Transactional
@Query("update Employee set age=:age where employeeId =:id")
void updateById(@Param("id") Integer id, @Param("age") Integer age);
```

```java
@Test
public void testUpdateById(){
    employeeRepository.updateById(1,100);
    System.out.println(employeeRepository.findByName("jack"));
}
```



## 4. Spring Data JPA 高级

### 4.1 CrudRepository 接口使用详解

CrudRepository 接口中额外定义了我们平时Crud的一些方法，并在 SimpleRepository 中做了实现，因此 只是定义了 EmployeeCrudRepository 空接口，就可以进行对数据库的简单操作。

```java
public interface EmployeeCrudRepository extends CrudRepository<Employee, Integer> {}
```

```java
public class EmployeeCrudRepositoryTest {
    ApplicationContext ctx = null;
    EmployeeCrudRepository employeeCrudRepository = null;
    
    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        employeeCrudRepository = ctx.getBean(EmployeeCrudRepository.class);
        System.out.println("setUp");
    }

    @Test
    public void testSave() {
        System.out.println(employeeCrudRepository);
        // org.springframework.data.jpa.repository.support.SimpleJpaRepository@1ecfcbc9
        List<Employee> employeeList = Arrays.asList(new Employee("test4", 5), new Employee("test4", 5));
        // SimpleJpaRepository 类中实现了 CrudRepository接口 相关方法
        employeeCrudRepository.save(employeeList);
        Iterable<Employee> iterable = employeeCrudRepository.findAll();
        System.out.println("foreach 遍历:");
        for (Employee employee : iterable) {
            System.out.println(employee);
        }
        System.out.println("forEach 遍历:");
        iterable.forEach(System.out::println);
    }

}
```

### 4.2 PagingAndSortingRespository 接口使用详解

- 该接口 包含 分页和排序 功能
- 带 排序 的查询， findAll（Sort sort）
- 带 排序的分页查询，findAll（Pageable pageable）

```java
public interface EmployeePagingAndSortingRepository extends PagingAndSortingRepository {}
```

PagingAndSortingRepository 接口 默认的实现类依旧是 SimpleJpaRepository。

```java
public class EmployeePagingAndSortingRepositoryTest {
    ApplicationContext ctx = null;
    EmployeePagingAndSortingRepository pagingAndSortingRepository = null;

    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        pagingAndSortingRepository = ctx.getBean(EmployeePagingAndSortingRepository.class);
        System.out.println("setUp");
    }

    @Test
    public void testPage() {
        // page:index 是从0开始,而不是从1开始
        Pageable pageable = new PageRequest(1,2);
        Page<Employee> page = pagingAndSortingRepository.findAll(pageable);
        List<Employee> list = page.getContent();
        System.out.println(list);
    }

    @Test
    public void testPageAndSort(){
        Sort orders = new Sort(new Sort.Order(Sort.Direction.DESC,"employeeId"));
        Pageable pageable = new PageRequest(1,2,orders);
        // 结果为先排序再分页
        Page<Employee> page = pagingAndSortingRepository.findAll(pageable);
        List<Employee> list = page.getContent();
        System.out.println(list);
    }
}
```



### 4.3 JpaRepository 接口使用详解

- findAll / findAll(Sort sort)
- save(entities)
- flush
- deleteBatch(entities)

```java
public interface EmployeeJpaRepository extends JpaRepository<Employee, Integer> {
}
```



```java
public class EmployeeJpaRepositoryTest {
    ApplicationContext ctx = null;
    EmployeeJpaRepository employeeJpaRepository = null;

    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        employeeJpaRepository = ctx.getBean(EmployeeJpaRepository.class);
        System.out.println("setUp");
    }

    @Test
    public void testFind() {
        Employee employee = employeeJpaRepository.findOne(1);
        System.out.println(employee);
        boolean exists = employeeJpaRepository.exists(10);
        System.out.println(exists);
    }

}
```

### 4.4 JpaSpecificationExecutor 接口使用

```java
public interface EmployeeJpaSpecificationExecutorRepository extends JpaRepository<Employee,Integer> , JpaSpecificationExecutor<Employee> {
}
```

```java
public class EmployeeJpaSpecificationExecutorRepositoryTest {
    ApplicationContext ctx = null;
    EmployeeJpaSpecificationExecutorRepository employeeJpaSpecificationExecutorRepository = null;

    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        employeeJpaSpecificationExecutorRepository = ctx.getBean(EmployeeJpaSpecificationExecutorRepository.class);
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    /**
     * 1.分页排序
     * 2.查询条件 age>30
     */
    @Test
    public void testQuery() {
        Sort orders = new Sort(new Sort.Order(Sort.Direction.DESC,"employeeId"));
        Pageable pageable = new PageRequest(1,2,orders);
        // 构建查询条件
        Specification<Employee> specification = new Specification<Employee>() {

            @Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                /**
                 * root:就是我们要查询的类型
                 * query:添加查询条件
                 * cb:构建Predicate
                 */
                Path<Integer> payh = root.get("age");
                return cb.gt(payh,30);
            }
        };
        Page<Employee> page = employeeJpaSpecificationExecutorRepository.findAll(specification,pageable);
        List<Employee> list = page.getContent();
        System.out.println(list);
	}
}
```





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
springboot 2.0.0.RELEASE 之前，data-redis 底层客户端为 jedis,所以只需要引入 <artifactId>spring-boot-starter-data-redis</artifactId>即可，  
如果使用yml自动配置的话，务必写对属性前缀，这样Springboot的默认配置类才能读取到。    
从springboot 2.0.0.RELEASE 开始底层区分两个不同的实现，jedis及lettuce，默认采用 lettuce,yml配置文件中的也会标注出使用得是jedis还是lettuce    
公共配置 spring.redis.timeout 的参数改为 Duration 类型，需要增加时间单位参数 如毫秒ms 秒s  


springboot整合redis原理: 获取redisPoolConfig，通过config创建出JedisConnectionFactory，通过factory构建出一个方便使用的template，我们还可以在template上再进一步的封装。


### Springboot 2.0之前 整合Redis  
- 修改application.properties
```yml
spring.redis.database=0
spring.redis.host=192.168.99.100
spring.redis.port=6379
# redis服务器的登录密码 没有可以不配置
#spring.redis.password= 
spring.redis.pool.max-active=8
spring.redis.pool.max-idle=8
spring.redis.pool.max-wait=-1
spring.redis.pool.min-idle=0
# redis 服务名称
#spring.redis.sentinel.master=
# 主机列表，格式为 host:port， 多个用逗号分隔  
#spring.redis.sentinel.nodes= 
spring.redis.timeout=10
```
- 修改pom.xml
```xml
<!--    springboot 2.0之前 整合redis -->
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
- 编写配置类
```java
@Configuration
public class RedisConfig {
    //1.创建JedisPollConfig对象，在该对象中完成连接池的配置
    @Bean
    //读取properties配置文件中配置的redis基本属性
    @ConfigurationProperties(prefix = "spring.redis")
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大空闲数
        // config.setMaxIdle(10);
        //最小空闲数
        // config.setMinIdle(5);
        //最大连接数
        // config.setMaxTotal(20);
        return config;
    }

    //2.创建JedisConnectionFactory 配置redis连接信息
    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig config) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        //配置连接池信息
        factory.setPoolConfig(config);
        // factory.setHostName("192.168.3.193");
        // factory.setPort(6379);
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
### Springboot 2.0之后 整合Redis 
- pom.xml
```xml
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>2.2.1.RELEASE</version>
            <relativePath/> <!-- lookup parent from repository -->
        </parent>
        <!--redis-->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-pool2</artifactId>
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
- application.yml
```yml
spring: 
  #redis配置
  redis:
    database: 0
    host: 192.168.3.193
    port: 6379
    password:
    jedis:
      pool:
        max-active: 100 #连接池最大连接数（负值表示没有限制）
        max-wait: -1ms #连接池最大阻塞等待时间（负值表示没有限制）
        max-idle: 100 #连接池最大空闭连接数
        min-idle: 50 #连接汉最小空闲连接数
    timeout: 100ms #连接超时时间（毫秒）
```
- 配置类
```java
@Configuration
public class NewRedisConfig {
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setEnableTransactionSupport(true);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);

        //value的序列化器使用protostuff序列化器
        //自定义的序列化类，在前面可以找到
        ProtostuffSerializer valueSerializer = new ProtostuffSerializer();
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(valueSerializer);
        return template;
    }
}
```
- 对redisTemplate再进行封装
```java

@Repository
@Slf4j
public class RedisDaoImpl<T> implements RedisDao<T> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean set(String key, Object value) {
        try {
            ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
            opsForValue.set(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return false;
    }

    @Override
    public boolean set(String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            // 创建对简单值(Redis术语中的string类型)执行操作的对象
            ValueOperations<String, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public boolean existsKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public <T> T getValue(String key, Class<T> type) {
        Object result = null;
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        try {
            result = operations.get(key);
            if (result == null) {
                return null;
            }
            // 将 Object 类型强转成 type 对应的类型
            return type.cast(result);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void removeKey(String key) {
        // 检查 key 是否存在
        if (existsKey(key)) {
            redisTemplate.delete(key);
        }
    }

    @Override
    public void remove(String... keys) {
        for (String key : keys) {
            removeKey(key);
        }
    }

    @Override
    public void removePattern(String pattern) {
        // 获取所有匹配的键
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }

    }

    @Override
    public <T> T getValue(String key) {
        T result = null;
        try {
            ValueOperations<String, Object> opsForValue = redisTemplate.opsForValue();
            result = (T) opsForValue.get(key);
            return result;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return result;
    }
}
```