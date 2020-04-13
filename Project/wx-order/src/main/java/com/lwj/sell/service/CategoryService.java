package com.lwj.sell.service;

import com.lwj.sell.dataobject.ProductCategory;

import java.util.List;

/**
 * 类目
 * Created by lwj
 * 2020-04-1009 10:12
 */
public interface CategoryService {

    ProductCategory findOne(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);
}
