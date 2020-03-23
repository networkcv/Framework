package com.zyt.ytcollege.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyt.ytcollege.dao.DO.SubjectDO;
import com.zyt.ytcollege.dao.mapper.SubjectMapper;
import com.zyt.ytcollege.service.SubjectService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * create by lwj on 2020/3/14
 */
@Service
@Slf4j
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectMapper subjectDao;

    @Override
    public JsonMsg saveSubject(SubjectDO subjectDO) {
        try {
            int res = subjectDao.insertSubject(subjectDO);
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
    public JsonMsg editSubject(SubjectDO subjectDO) {
        try {
            int res = subjectDao.updateSubject(subjectDO);
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
    public JsonMsg removeSubjectById(int id) {
        try {
            int res = subjectDao.removeSubjectById(id);
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
    public SubjectDO findSubjectById(int id) {
        return subjectDao.selectSubjectById(id);
    }

    @Override
    public PageInfo<SubjectDO> findAllSubject(Paging paging, SubjectDO subjectDO) {
        PageHelper.startPage(paging.getPageNum(), paging.getPageSize());
        List<SubjectDO> subjectDOS = subjectDao.selectAll(subjectDO);
        return new PageInfo<>(subjectDOS);
    }
}
