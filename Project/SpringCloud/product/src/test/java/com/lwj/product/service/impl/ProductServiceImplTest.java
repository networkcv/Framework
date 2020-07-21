package com.lwj.product.service.impl;

import com.lwj.product.ProductApplicationTests;
import com.lwj.product.pojo.ProductInfo;
import com.lwj.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * create by lwj on 2020/7/22
 */
@Component
class ProductServiceImplTest extends ProductApplicationTests {

    @Autowired
    private ProductService productService;
    @Test
    void findUpAll() {
        List<ProductInfo> list = productService.findUpAll();
        System.out.println(list);
    }
}