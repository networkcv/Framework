package com.lwj.repository;


import com.lwj.DO.Employee;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Arrays;
import java.util.List;

/**
 * create by lwj on 2020/4/9
 */
public class EmployeeCrudRepositoryTest {
    ApplicationContext ctx = null;
    EmployeeCrudRepository employeeCrudRepository = null;


    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        employeeCrudRepository = ctx.getBean(EmployeeCrudRepository.class);
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }

    @Test
    public void testSave() {
        System.out.println(employeeCrudRepository);
        // org.springframework.data.jpa.repository.support.SimpleJpaRepository@1ecfcbc9
        List<Employee> employeeList = Arrays.asList(new Employee("test4", 5), new Employee("test4", 5));
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