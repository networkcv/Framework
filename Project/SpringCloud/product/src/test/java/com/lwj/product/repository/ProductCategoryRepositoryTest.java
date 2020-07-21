package com.lwj.product.repository;

import com.lwj.product.pojo.ProductCategory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * create by lwj on 2020/7/20
 */
@RunWith(SpringRunner.class)
@SpringBootTest
class ProductCategoryRepositoryTest {
    ApplicationContext ctx = null;
    @Autowired
    ProductCategoryRepository productCategoryRepository;
//    ProductCategoryRepository productCategoryRepository = null;

//    @Before
//    public void setUp() {
//        ctx = new ClassPathXmlApplicationContext("application.yml");
//        productCategoryRepository = ctx.getBean(ProductCategoryRepository.class);
//    }

    @Test
    public void findByCategoryId() {
        List<ProductCategory> list = productCategoryRepository.findByCategoryTypeIn(Arrays.asList(1));
        System.out.println(list);
    }
}