package com.lwj.orm.core;

import com.lwj.orm.core.model.Configuration;
import com.lwj.orm.core.model.MappedStatement;

import java.sql.SQLException;
import java.util.List;

/**
 * Date: 2021/10/28
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public interface Executor {

    <E> List<E> query(Configuration configuration, MappedStatement mappedStatement, Object[] param) throws Exception;

    void close() throws SQLException;
}
