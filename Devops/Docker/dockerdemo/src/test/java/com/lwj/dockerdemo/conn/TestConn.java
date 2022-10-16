package com.lwj.dockerdemo.conn;

import com.lwj.dockerdemo.DockerdemoApplicationTests;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class TestConn extends DockerdemoApplicationTests {

    @Resource
    private DataSource dataSource;

    @Test
    public void testConn() throws SQLException {
        Connection connection = this.dataSource.getConnection();
        System.out.println("connection = " + connection);
    }
}
