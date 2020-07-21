package com.lwj.product.enums;

import lombok.Getter;

/**
 * 商品上下架状态
 * create by lwj on 2020/7/22
 */
@Getter
public enum ProductStatusEnum {
    UP(0,"在架"),
    DOWN(1,"下架");
    private Integer code;
    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
