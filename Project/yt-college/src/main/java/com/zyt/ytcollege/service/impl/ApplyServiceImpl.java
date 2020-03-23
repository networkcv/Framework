package com.zyt.ytcollege.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zyt.ytcollege.config.enums.ApplyState;
import com.zyt.ytcollege.config.enums.ApplyType;
import com.zyt.ytcollege.config.enums.PaymentState;
import com.zyt.ytcollege.dao.DO.ApplyDO;
import com.zyt.ytcollege.dao.DO.PaymentDO;
import com.zyt.ytcollege.dao.DO.StudentDO;
import com.zyt.ytcollege.dao.DO.SubjectDO;
import com.zyt.ytcollege.dao.mapper.ApplyMapper;
import com.zyt.ytcollege.service.ApplyService;
import com.zyt.ytcollege.service.DTO.ApplyDTO;
import com.zyt.ytcollege.service.PaymentService;
import com.zyt.ytcollege.service.StudentService;
import com.zyt.ytcollege.service.SubjectService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;
import com.zyt.ytcollege.util.TimesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    @Autowired
    private PaymentService paymentService;

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
                    if (saveRes > 0) {
                        applyDO.setStudentId(student.getId());
                    }
                } else {
                    applyDO.setStudentId(res.getId());
                }
            }

            //填充课程信息
            SubjectDO subject = subjectService.findSubjectById(applyDO.getSubjectId());
            applyDO.setSubjectName(subject.getName());
            applyDO.setSubjectLevel(subject.getSubjectLevel());
            applyDO.setSubjectCost(subject.getCost());
            applyDO.setDate(TimesUtil.currentDate());
            applyDO.setState(ApplyState.NEED_DISPOSE.state);
            //如果是付费报名 还需要生成订单信息
            PaymentDO paymentDO = new PaymentDO();
            if (applyDO.getType() == ApplyType.PAYMENT.type) {
                paymentDO.setState(PaymentState.NO_PAY.state);
                paymentService.savePayment(paymentDO);
                applyDO.setPaymentId(paymentDO.getId());
                applyDO.setPaymentState(PaymentState.NO_PAY.state);
            }
            int res = applyDao.insertApply(applyDO);
            //更新payment记录中的apply_id
            if (applyDO.getType() == ApplyType.PAYMENT.type) {
                paymentDO.setApplyId(applyDO.getId());
                paymentService.editPayment(paymentDO);
            }

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
    public PageInfo<ApplyDO> findAllApply(Paging paging, ApplyDTO applyDTO) {
        PageHelper.startPage(paging.getPageNum(), paging.getPageSize());
        List<ApplyDO> applyDOS = applyDao.selectAll(applyDTO);
        return new PageInfo<>(applyDOS);
    }
}
