package com.zyt.ytcollege.service.impl;

import com.zyt.ytcollege.dao.DO.StudentDO;
import com.zyt.ytcollege.dao.mapper.StudentMapper;
import com.zyt.ytcollege.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lwj on 2020/3/15
 */
@Service
public class StudentServiceImpl  implements StudentService {
    @Autowired
    private StudentMapper studentDao;

    @Override
    public StudentDO findStudent(StudentDO studentQuery) {
        return studentDao.selectStudent(studentQuery);
    }

    @Override
    public int saveStudent(StudentDO student) {
        return studentDao.insertStudent(student);
    }
}
