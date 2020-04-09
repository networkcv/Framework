package com.lwj.uitl;

import com.lwj.repository.EmployeeRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * create by lwj on 2020/4/8
 */
public class EntityManagerFactoryTest {
    ApplicationContext ctx = null;

    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    public void testEntitiyManagerFactory() {
        // 在容器启动时，会扫描配置了@Entity的实体
        // 如果配置了 <prop key="hibernate.hbm2ddl.auto">update</prop>
        // 则会根据实体生成对应的表，前提是数据库中不存在该表
    }

}
