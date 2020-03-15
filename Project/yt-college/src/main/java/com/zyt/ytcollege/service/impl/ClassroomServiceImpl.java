package com.zyt.ytcollege.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyt.ytcollege.dao.DO.ClassroomDO;
import com.zyt.ytcollege.dao.mapper.ClassroomMapper;
import com.zyt.ytcollege.service.ClassroomService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lwj on 2020/3/14
 */
@Service
@Slf4j
public class ClassroomServiceImpl implements ClassroomService {
    @Autowired
    private ClassroomMapper ClassroomDao;

    @Override
    public JsonMsg saveClassroom(ClassroomDO ClassroomDO) {
        try {
            int res = ClassroomDao.insertClassroom(ClassroomDO);
            if (res > 0) {
                return new JsonMsg(200, "ok");
            } else {
                return new JsonMsg(500, "无效操作");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new JsonMsg(500, e.getMessage());
        }
    }

    @Override
    public JsonMsg editClassroom(ClassroomDO ClassroomDO) {
        try {
            int res = ClassroomDao.updateClassroom(ClassroomDO);
            if (res > 0) {
                return new JsonMsg(200, "ok");
            } else {
                return new JsonMsg(500, "无效操作");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new JsonMsg(500, e.getMessage());
        }
    }

    @Override
    public JsonMsg removeClassroomById(int id) {
        try {
            int res = ClassroomDao.removeClassroomById(id);
            if (res > 0) {
                return new JsonMsg(200, "ok");
            } else {
                return new JsonMsg(500, "无效操作");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return new JsonMsg(500, e.getMessage());
        }
    }

    @Override
    public ClassroomDO findClassroomById(int id) {
        return ClassroomDao.selectClassroomById(id);
    }

    @Override
    public Page<ClassroomDO> findAllClassroom(Paging paging, ClassroomDO ClassroomDO) {
        PageHelper.startPage(paging.getPageNum(), paging.getPageSize());
        return ClassroomDao.selectAll(ClassroomDO);
    }
}
