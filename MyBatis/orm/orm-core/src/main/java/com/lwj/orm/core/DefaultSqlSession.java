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
public class DefaultSqlSession implements SqlSession {
    private Configuration configuration;
    private Executor simpleExecutor = new SimpleExecutor();

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... param) throws Exception {
        MappedStatement mappedStatement = configuration.getMapperStatementMap().get(statementId);
        return simpleExecutor.query(configuration, mappedStatement, param);
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if (objects.size() == 1) {
            return (T) objects.get(0);
        } else {
            throw new RuntimeException("查询SQL异常");
        }
    }

    @Override
    public void close() throws SQLException {
        simpleExecutor.close();
    }
}
