package com.lwj.orm.core.model;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.util.Map;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Configuration {
    private DataSource dataSource;
    private Map<String, MappedStatement> mapperStatementMap = Maps.newHashMap();
}
