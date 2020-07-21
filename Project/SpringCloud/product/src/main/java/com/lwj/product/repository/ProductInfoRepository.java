package com.lwj.product.repository;

import com.lwj.product.pojo.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * create by lwj on 2020/7/20
 */
public interface ProductInfoRepository extends JpaRepository<ProductInfo, Integer> {
    List<ProductInfo> findByProductStatus(Integer productStatus);
}
