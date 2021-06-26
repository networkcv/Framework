/*
 * Copyright (C) 2009-2018 Hangzhou ypsx Technology Co., Ltd. All rights reserved
 */
package com.ypshengxian.kernel.product.client.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Date: 2021/3/8
 * <p>
 * Description: 商品基础数据事件消息
 *
 * @author liuWangjie
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductEventMsg<T> {

    /**
     * 事件类型
     */
    private EventType eventType;

    /**
     * 消息对象
     */
    private T t;
}

