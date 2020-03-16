package com.zyt.ytcollege.service;

import com.zyt.ytcollege.dao.DO.PaymentDO;
import com.zyt.ytcollege.util.JsonMsg;

/**
 * create by lwj on 2020/3/16
 */
public interface PaymentService {
    JsonMsg savePayment(PaymentDO paymentDO);

    JsonMsg editPayment(PaymentDO paymentDO);

    JsonMsg pay(PaymentDO paymentDO);
}
