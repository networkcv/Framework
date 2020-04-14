package com.lwj.sell.service.impl;

import com.lwj.sell.dataobject.SellerInfo;
import com.lwj.sell.repository.SellerInfoRepository;
import com.lwj.sell.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by lwj

 */
@Service
public class SellerServiceImpl implements SellerService {

    @Autowired
    private SellerInfoRepository repository;

    @Override
    public SellerInfo findSellerInfoByOpenid(String openid) {
        return repository.findByOpenid(openid);
    }
}
