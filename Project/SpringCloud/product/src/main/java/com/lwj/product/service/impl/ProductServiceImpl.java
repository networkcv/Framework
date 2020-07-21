package com.lwj.product.service.impl;

import com.lwj.product.enums.ProductStatusEnum;
import com.lwj.product.pojo.ProductInfo;
import com.lwj.product.repository.ProductInfoRepository;
import com.lwj.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.util.List;

/**
 * create by lwj on 2020/7/22
 */
@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductInfoRepository productInfoRepository;
    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.DOWN.getCode());
    }
}
