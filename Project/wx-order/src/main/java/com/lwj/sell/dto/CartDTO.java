package com.lwj.sell.dto;

import lombok.Data;

/**
 * 购物车
 * Created by lwj
 * 2020-04-10 19:37
 */
@Data
public class CartDTO {

    /** 商品Id. */
    private String productId;

    /** 数量. */
    private Integer productQuantity;

    public CartDTO(String productId, Integer productQuantity) {
        this.productId = productId;
        this.productQuantity = productQuantity;
    }
}
