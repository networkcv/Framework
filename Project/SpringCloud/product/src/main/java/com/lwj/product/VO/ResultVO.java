package com.lwj.product.VO;

import lombok.Data;

/**
 * http请求返回的最外层对象
 *
 * create by lwj on 2020/7/22
 */
@Data
public class ResultVO<T> {
    private Integer code;
    private String msg;
    private T data;
}
