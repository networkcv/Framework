package com.lwj.uitl;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;

/**
 * create by lwj on 2020/4/8
 */
public class DataSourceTest {
    ApplicationContext ctx=null;
    @Before
    public void setUp(){
        ctx=new ClassPathXmlApplicationContext("beans.xml");
        System.out.println("setUp");
    }
    @Test
    public void testDataSource(){
        System.out.println("testDataSource");
        DataSource dataSource = (DataSource) ctx.getBean("dataSource");
        Assert.assertNotNull(dataSource);
        System.out.println(dataSource);
    }

    @After
    public void tearDown(){
        System.out.println("tearDown");
    }
}
