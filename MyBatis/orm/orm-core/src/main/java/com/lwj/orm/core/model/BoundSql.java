package com.lwj.orm.core.model;

import com.lwj.orm.core.parse.ParameterMapping;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * Date: 2021/11/5
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Data
@AllArgsConstructor
public class BoundSql {

    //解析后的sql
    private String sqlParsed;

    private List<ParameterMapping> parameterMappings;

}
