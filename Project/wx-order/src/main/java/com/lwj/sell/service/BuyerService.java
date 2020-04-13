package com.lwj.sell.service;

import com.lwj.sell.dto.OrderDTO;

/**
 * 买家
 * Created by lwj
 * 2020-06-22 00:11
 */
public interface BuyerService {

    //查询一个订单
    OrderDTO findOrderOne(String openid, String orderId);

    //取消订单
    OrderDTO cancelOrder(String openid, String orderId);
}
