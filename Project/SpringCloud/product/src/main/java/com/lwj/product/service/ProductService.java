package com.lwj.product.service;

import com.lwj.product.pojo.ProductInfo;

import java.util.List;

/**
 * create by lwj on 2020/7/22
 */
public interface ProductService {
    /**
     * 查询所有在架商品列表
     */
    List<ProductInfo> findUpAll();
}
