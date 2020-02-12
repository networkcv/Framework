package com.lwj.mysql;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * create by lwj on 2020/2/7
 */
public class Driver implements java.sql.Driver {

    static {
        try {
            DriverManager.registerDriver(new com.lwj.mysql.Driver());
            System.out.println("-----------------------");
        } catch (SQLException e) {
            throw new RuntimeException("register driver fail");
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        return null;
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return false;
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 0;
    }

    @Override
    public int getMinorVersion() {
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}
