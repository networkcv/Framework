package com.zyt.ytcollege.service;

import com.zyt.ytcollege.dao.DO.StudentDO;

/**
 * create by lwj on 2020/3/15
 */
public interface StudentService {

    StudentDO findStudent(StudentDO studentQuery);

    int saveStudent(StudentDO student);
}
