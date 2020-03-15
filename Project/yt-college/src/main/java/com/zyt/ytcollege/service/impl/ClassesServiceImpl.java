package com.zyt.ytcollege.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyt.ytcollege.dao.DO.ClassesDO;
import com.zyt.ytcollege.dao.mapper.ClassesMapper;
import com.zyt.ytcollege.service.ClassesService;
import com.zyt.ytcollege.util.Paging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lwj on 2020/3/14
 */
@Service
public class ClassesServiceImpl implements ClassesService {
    @Autowired
    private ClassesMapper classesDao;
    @Override
    public Page<ClassesDO> findByTid(int id, Paging paging) {
        PageHelper.startPage(paging.getPageNum(), paging.getPageSize());
        return classesDao.selectClassesByTid(id);
    }
}
