package com.lwj.dao;

import com.lwj.DO.Student;
import com.lwj.dao.StudentDao;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.util.List;

/**
 * create by lwj on 2020/4/8
 */
public class SpringJdbcTest {
    ApplicationContext ctx = null;

    @Before
    public void setUp() {
        ctx = new ClassPathXmlApplicationContext("beans.xml");
        System.out.println("setUp");
    }

    @Test
    public void query() {
        System.out.println("query");
        StudentDao studentDao = (StudentDao) ctx.getBean("studentDao");
        List<Student> query = studentDao.query();
        for (Student student :query ) {
            System.out.println(student);
        }
    }


    @Test
    public void save() {
        System.out.println("save");
        StudentDao studentDao = (StudentDao) ctx.getBean("studentDao");
        studentDao.save(new Student("bob", 20));
        query();
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
    }
}
