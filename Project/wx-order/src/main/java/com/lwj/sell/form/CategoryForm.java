package com.lwj.sell.form;

import lombok.Data;

/**
 * Created by lwj

 */
@Data
public class CategoryForm {

    private Integer categoryId;

    /** 类目名字. */
    private String categoryName;

    /** 类目编号. */
    private Integer categoryType;
}
