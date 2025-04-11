package com.lwj.pojo;

import lombok.Data;

/**
 * Date: 2021/11/16
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Data
public class Order {
    private Integer id;
    private Double account;
    private User user;
}

