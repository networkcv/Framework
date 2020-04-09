package com.lwj.repository;


import com.lwj.DO.Employee;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * create by lwj on 2020/4/9
 */
public class EmployeeJpaRepositoryTest {
    ApplicationContext ctx = null;
    EmployeeJpaRepository employeeJpaRepository = null;


    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        employeeJpaRepository = ctx.getBean(EmployeeJpaRepository.class);
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    public void testFind() {
        Employee employee = employeeJpaRepository.findOne(1);
        System.out.println(employee);
        boolean exists = employeeJpaRepository.exists(10);
        System.out.println(exists);
    }

}