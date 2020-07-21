package com.lwj.product.service.impl;

import com.lwj.product.ProductApplicationTests;
import com.lwj.product.pojo.ProductCategory;
import com.lwj.product.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * create by lwj on 2020/7/22
 */
@Component
class CategoryServiceImplTest extends ProductApplicationTests {

    @Autowired
    private CategoryService categoryService;
    @Test
    void findByCategoryTypeIn() {
        List<ProductCategory> list = categoryService.findByCategoryTypeIn(Arrays.asList(1, 2));
        System.out.println(list);
    }
}