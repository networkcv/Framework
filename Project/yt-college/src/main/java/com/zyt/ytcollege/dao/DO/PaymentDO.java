package com.zyt.ytcollege.dao.DO;

import lombok.Data;

/**
 * create by lwj on 2020/3/16
 */
@Data
public class PaymentDO {
    private Integer id;
    private Integer applyId;    //报名id
    private Integer state;      //支付状态  0-未支付 1-已支付 2-已退款
    private Integer type;       //支付类型 0-转账 1-微信在线支付 2-支付宝在线支付
    private Long serialNumber;  //流水号
    private Double money;       //金额
    private Integer operator;   //操作人id 转账时填写
    private String time;        //操作时间
}
