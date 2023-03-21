package com.lwj.orm.core;

import com.lwj.orm.core.model.BoundSql;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Date: 2021/11/5
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
@Slf4j
public class SimpleExecutorTest {

    @Test
    public void getBoundSql() {
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        BoundSql boundSql = simpleExecutor.getBoundSql("select * from user where id = #{id};");
        log.info("boundSql:{}",boundSql);
    }
}