package com.zyt.ytcollege.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyt.ytcollege.dao.DO.TeacherDO;
import com.zyt.ytcollege.dao.mapper.TeacherMapper;
import com.zyt.ytcollege.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lwj on 2020/3/14
 */
@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherDao;
    @Override
    public Page<TeacherDO> list(int pageNum,int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return teacherDao.teacherList();
    }
}
