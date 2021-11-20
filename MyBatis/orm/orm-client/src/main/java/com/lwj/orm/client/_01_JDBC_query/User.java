package com.lwj.orm.client._01_JDBC_query;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Data
@Builder
@ToString
public class User {
    private Integer id;
    private String username;
}
