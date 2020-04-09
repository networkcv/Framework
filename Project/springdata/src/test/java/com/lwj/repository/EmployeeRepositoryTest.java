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
    public void testFindByNameEndingWithAndAgeGreaterThan() {
        List<Employee> res = employeeRepository.findByNameEndingWithAndAgeGreaterThan("3", 22);
        res.forEach(System.out::println);
    }

    @Test
    public void testFindByNameIn() {
        System.out.println(employeeRepository);
        List<Employee> res = employeeRepository.findByNameIn(Arrays.asList("test1", "test2"));
        res.forEach(System.out::println);
    }

    @Test
    public void testGetEmployeeByMaxId() {
        Employee employee = employeeRepository.getEmployeeByMaxId();
        System.out.println(employee);
    }

    @Test
    public void testGetEmployeeByNameAndAge() {
        Employee employee = employeeRepository.myQueryEmployeeByNameAndAge(20, "jack");
        System.out.println(employee);
    }

    @Test
    public void testGetEmployeeByNameAndAge2() {
        Employee employee = employeeRepository.myQueryEmployeeByNameAndAge2("jack", 20);
        System.out.println(employee);
    }

    @Test
    public void testmMQueryNative(){
        List<Employee> employees = employeeRepository.myQueryNative(20);
        employees.forEach(System.out::println);
    }

    @Test
    public void testUpdateById(){
        employeeRepository.updateById(1,100);
        System.out.println(employeeRepository.findByName("jack"));;
    }
}
