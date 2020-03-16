package com.zyt.ytcollege.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyt.ytcollege.dao.DO.TeacherDO;
import com.zyt.ytcollege.dao.mapper.TeacherMapper;
import com.zyt.ytcollege.service.TeacherService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.MD5Util;
import com.zyt.ytcollege.util.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * create by lwj on 2020/3/14
 */
@Service
@Slf4j
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    private TeacherMapper teacherDao;

    @Override
    public JsonMsg saveTeacher(TeacherDO teacherDO) {
        try {
            teacherDO.setPassword(MD5Util.encode(teacherDO.getPhone()));
            teacherDO.setIsDelete(0);
            int res = teacherDao.insertTeacher(teacherDO);
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
    public JsonMsg editTeacher(TeacherDO teacherDO) {
        try {
            if (teacherDO.getPassword() != null) {
                teacherDO.setPassword(MD5Util.encode(teacherDO.getPassword()));
            }
            int res = teacherDao.updateTeacher(teacherDO);
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
    public JsonMsg removeTeacherById(int id) {
        try {
            int res = teacherDao.removeTeacherById(id);
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
    public TeacherDO findTeacherById(int id) {
        return teacherDao.selectTeacherById(id);
    }

    @Override
    public Page<TeacherDO> findAllTeacher(Paging paging, TeacherDO teacherDO) {
        PageHelper.startPage(paging.getPageNum(), paging.getPageSize());
        return teacherDao.selectAll(teacherDO);
    }

    @Override
    public JsonMsg login(TeacherDO teacherDO) {
        if (teacherDO.getPhone() != null && teacherDO.getPassword() != null) {
            TeacherDO teacher = teacherDao.selectTeacherByPhone(teacherDO.getPhone());
            if (teacher != null) {
                if (MD5Util.check(teacherDO.getPassword(), teacher.getPassword())) {
                    return new JsonMsg(200, "ok", teacher);
                }
            }
        }
        return new JsonMsg(0, "用户名或密码错误");
    }
}
