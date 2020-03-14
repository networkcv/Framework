package com.zyt.ytcollege.service;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.TeacherDO;

/**
 * create by lwj on 2020/3/14
 */
public interface TeacherService {
    Page<TeacherDO> list(int pageNum,int pageSize);
}
