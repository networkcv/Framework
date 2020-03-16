package com.zyt.ytcollege.dao.mapper;

import com.zyt.ytcollege.dao.DO.PaymentDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * create by lwj on 2020/3/16
 */
@Mapper
public interface PaymentMapper {
    int updatePayment(PaymentDO paymentDO);

    int insertPayment(PaymentDO paymentDO);

    @Select("select * from payment where id =#{id}")
    PaymentDO findPaymentById(Integer id);
}
