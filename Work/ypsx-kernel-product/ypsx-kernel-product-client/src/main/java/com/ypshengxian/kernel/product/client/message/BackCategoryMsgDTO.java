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
 * Description: 类目消息
 *
 * @author liuWangjie
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BackCategoryMsgDTO {
    /**
     * id
     */
    private Long id;
    /**
     * categoryId
     */
    private Long categoryId;
    /**
     * 类目名称
     */
    private String categoryName;
    /**
     * 父类目id
     */
    private Long parentId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 业态
     */
    private Integer businessType;
}

