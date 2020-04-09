package com.lwj.dao;

import com.lwj.DO.Student;

import java.util.List;

/**
 * create by lwj on 2020/4/8
 */
public interface StudentDao {
    List<Student> query();

    void save(Student student);
}
