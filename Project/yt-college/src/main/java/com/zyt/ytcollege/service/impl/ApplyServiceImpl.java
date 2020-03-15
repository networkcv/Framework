package com.zyt.ytcollege.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zyt.ytcollege.dao.DO.ApplyDO;
import com.zyt.ytcollege.dao.DO.StudentDO;
import com.zyt.ytcollege.dao.DO.SubjectDO;
import com.zyt.ytcollege.dao.mapper.ApplyMapper;
import com.zyt.ytcollege.service.ApplyService;
import com.zyt.ytcollege.service.StudentService;
import com.zyt.ytcollege.service.SubjectService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;
import com.zyt.ytcollege.util.TimesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * create by lwj on 2020/3/14
 */
@Service
@Slf4j
@Transactional
public class ApplyServiceImpl implements ApplyService {
    @Autowired
    private ApplyMapper applyDao;
    @Autowired
    private StudentService studentService;
    @Autowired
    private SubjectService subjectService;

    @Override
    public JsonMsg saveApply(ApplyDO applyDO) {
        try {
            //判断该学员是否已存在
            if (applyDO.getStudentId() == null) {
                StudentDO student = new StudentDO();
                student.setName(applyDO.getStudentName());
                student.setAge(applyDO.getStudentAge());
                student.setPhone(applyDO.getStudentPhone());
                StudentDO res = studentService.findStudent(student);
                if (res == null) {
                    //不存在，则需要先录入学员基本信息
                    student.setReferrer(applyDO.getReferrer());
                    int saveRes = studentService.saveStudent(student);
                    if (saveRes > 0){
                        applyDO.setStudentId(student.getId());
                    }
                }
            }
            //填充课程信息
            SubjectDO subject = subjectService.findSubjectById(applyDO.getSubjectId());
            applyDO.setSubjectName(subject.getName());
            applyDO.setSubjectLevel(subject.getSubjectLevel());
            applyDO.setSubjectCost(subject.getCost());
            applyDO.setDate(TimesUtil.currentDate());
            applyDO.setState(0);
            int res = applyDao.insertApply(applyDO);
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
    public JsonMsg editApply(ApplyDO applyDO) {
        try {
            int res = applyDao.updateApply(applyDO);
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
    public JsonMsg removeApplyById(int id) {
        try {
            int res = applyDao.removeApplyById(id);
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
    public ApplyDO findApplyById(int id) {
        return applyDao.selectApplyById(id);
    }

    @Override
    public Page<ApplyDO> findAllApply(Paging paging, ApplyDO applyDO) {
        PageHelper.startPage(paging.getPageNum(), paging.getPageSize());
        return applyDao.selectAll(applyDO);
    }
}
