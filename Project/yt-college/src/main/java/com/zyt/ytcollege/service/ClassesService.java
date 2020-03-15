package com.zyt.ytcollege.service;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ClassesDO;
import com.zyt.ytcollege.util.Paging;

/**
 * create by lwj on 2020/3/14
 */
public interface ClassesService {
    Page<ClassesDO> findByTid(int id, Paging paging);
}
