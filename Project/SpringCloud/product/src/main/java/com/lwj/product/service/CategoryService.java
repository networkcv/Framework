package com.lwj.product.service;

import com.lwj.product.pojo.ProductCategory;

import java.util.List;

/**
 * create by lwj on 2020/7/22
 */
public interface CategoryService {
    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);
}
