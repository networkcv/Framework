package com.lwj.orm.core.model;

import lombok.Data;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Data
public class MappedStatement {
    //id
    private String id;
    //sql语句
    private String sql;
    //输⼊参数
    private Class<?> parameterType;
    //输出参数
    private Class<?> resultType;
}
