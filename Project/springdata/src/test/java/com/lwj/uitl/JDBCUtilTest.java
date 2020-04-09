package com.lwj.uitl;

import com.lwj.util.JDBCUtil;
import org.junit.Assert;
import org.junit.Test;

import java.sql.Connection;

/**
 * create by lwj on 2020/4/8
 */
public class JDBCUtilTest {

    @Test
    public void testGetConnection() throws Exception {
        Connection connection = JDBCUtil.getConnection();
        Assert.assertNotNull(connection);
    }
}
