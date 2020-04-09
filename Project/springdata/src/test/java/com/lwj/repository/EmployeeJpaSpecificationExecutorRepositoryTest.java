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
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;

/**
 * create by lwj on 2020/4/9
 */
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