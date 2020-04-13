package com.lwj.sell.service;

import com.lwj.sell.dataobject.SellerInfo;

/**
 * 卖家端
 * Created by lwj
 * 2020-04-10 23:14
 */
public interface SellerService {

    /**
     * 通过openid查询卖家端信息
     * @param openid
     * @return
     */
    SellerInfo findSellerInfoByOpenid(String openid);
}
