package com.lwj.orm.core;

import java.sql.SQLException;
import java.util.List;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public interface SqlSession {

    <E> List<E> selectList(String statementId, Object... param) throws Exception;

    <T> T selectOne(String statementId, Object... params) throws Exception;

    void close() throws SQLException;
}
