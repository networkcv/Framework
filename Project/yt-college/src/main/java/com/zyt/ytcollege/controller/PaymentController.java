package com.zyt.ytcollege.controller;

import com.zyt.ytcollege.dao.DO.PaymentDO;
import com.zyt.ytcollege.service.PaymentService;
import com.zyt.ytcollege.util.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * create by lwj on 2020/3/16
 */
@RestController
@RequestMapping("p")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PutMapping("pay")
    public JsonMsg pay(PaymentDO paymentDO){
        return paymentService.pay(paymentDO);
    }


}
