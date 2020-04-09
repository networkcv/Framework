package com.lwj.dao;

import com.lwj.DO.Student;
import org.junit.Test;

import java.util.List;

/**
 * create by lwj on 2020/4/8
 */
public class StudentDaoImplTest {
    @Test
    public void testQuery() {
        StudentDao studentDao = new StudentDaoImpl();
        List<Student> query = studentDao.query();
        for (Student student : query) {
            System.out.println(student);
        }
    }

    @Test
    public void testSave() {
        StudentDao studentDao = new StudentDaoImpl();
        studentDao.save(new Student("bob", 19));
        testQuery();
    }
}
