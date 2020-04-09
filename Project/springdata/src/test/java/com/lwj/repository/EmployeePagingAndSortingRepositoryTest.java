package com.lwj.repository;


import com.lwj.DO.Employee;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

/**
 * create by lwj on 2020/4/9
 */
public class EmployeePagingAndSortingRepositoryTest {
    ApplicationContext ctx = null;
    EmployeePagingAndSortingRepository pagingAndSortingRepository = null;


    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans-new.xml");
        pagingAndSortingRepository = ctx.getBean(EmployeePagingAndSortingRepository.class);
        System.out.println("setUp");
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
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