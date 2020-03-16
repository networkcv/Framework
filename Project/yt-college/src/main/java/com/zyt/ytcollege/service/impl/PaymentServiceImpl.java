package com.zyt.ytcollege.service.impl;

import com.zyt.ytcollege.config.enums.PaymentState;
import com.zyt.ytcollege.dao.DO.ApplyDO;
import com.zyt.ytcollege.dao.DO.PaymentDO;
import com.zyt.ytcollege.dao.mapper.PaymentMapper;
import com.zyt.ytcollege.service.ApplyService;
import com.zyt.ytcollege.service.PaymentService;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.TimesUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * create by lwj on 2020/3/16
 */
@Service
@Slf4j
@Transactional
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentMapper paymentDao;
    @Autowired
    private ApplyService applyService;

    @Override
    public JsonMsg savePayment(PaymentDO paymentDO) {
        try {
            int res = paymentDao.insertPayment(paymentDO);
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
    public JsonMsg editPayment(PaymentDO paymentDO) {
        try {
            int res = paymentDao.updatePayment(paymentDO);
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
    public JsonMsg pay(PaymentDO paymentDO) {
        try {
            if (paymentDO.getApplyId()==null){
                paymentDO=paymentDao.findPaymentById(paymentDO.getId());
            }
            paymentDO.setState(PaymentState.PAY.state);
            paymentDO.setTime(TimesUtil.currentTime());
            //TODO
            //paymentDO.getOperator(Session.getUser) //改为从session获取当前用户
            int res = paymentDao.updatePayment(paymentDO);
            ApplyDO applyDO = applyService.findApplyById(paymentDO.getApplyId());
            applyDO.setPaymentState(PaymentState.PAY.state);
            applyDO.setRealCost(paymentDO.getMoney());
            applyService.editApply(applyDO);
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
}
