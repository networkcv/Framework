package com.lwj.sell.repository;

import com.lwj.sell.dataobject.ProductCategory;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * create by lwj on 2020/4/9
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@Transactional
@Rollback(false)
public class ProductCategoryRepositoryTest {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;


    @Test
    public void getAll(){
        List<ProductCategory> all = productCategoryRepository.findAll();
        all.forEach(System.out::println);
    }
    @Test
    public void getOne() {
        ProductCategory one = productCategoryRepository.getOne(1);
        System.out.println(one.toString());
    }

    @Test
    public void save(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("游戏");
        productCategory.setCategoryType(2);
        productCategoryRepository.save(productCategory);
        getAll();
    }

    @Test
    public void update(){
        ProductCategory one = productCategoryRepository.getOne(5);
        one.setCategoryType(1);
        productCategoryRepository.save(one);

    }
    @Test
    public void  findByCategoryTypeIn(){
        List<ProductCategory> list = productCategoryRepository.findByCategoryTypeIn(Arrays.asList(1,2,3));
        Assert.assertNotEquals(0,list.size());
        list.forEach(System.out::println);
    }
}