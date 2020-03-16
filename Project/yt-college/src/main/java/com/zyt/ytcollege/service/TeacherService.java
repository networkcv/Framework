package com.zyt.ytcollege.service;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.TeacherDO;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;

/**
 * create by lwj on 2020/3/14
 */
public interface TeacherService {
    JsonMsg saveTeacher(TeacherDO teacherDO);

    JsonMsg removeTeacherById(int id);

    JsonMsg editTeacher(TeacherDO teacherDO);

    TeacherDO findTeacherById(int id);

    Page<TeacherDO> findAllTeacher(Paging paging, TeacherDO teacherDO);

    JsonMsg login(TeacherDO teacherDO);
}
