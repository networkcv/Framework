package com.lwj.sell.enums;

import lombok.Getter;

/**
 * 商品状态
 * Created by lwj
 * 2020-04-1009 17:33
 */
@Getter
public enum ProductStatusEnum implements CodeEnum {
    UP(0, "在架"),
    DOWN(1, "下架")
    ;

    private Integer code;

    private String message;

    ProductStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }


}
