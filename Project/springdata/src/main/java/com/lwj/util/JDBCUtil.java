package com.lwj.util;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * create by lwj on 2020/4/8
 * JDBC 工具类
 */
public class JDBCUtil {
    /**
     * 获取 Connection
     */
    public static Connection getConnection() {
        Connection connection = null;
        try {
            InputStream inputStream = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(inputStream);
            String url = (String) properties.get("jdbc.url");
            String user = (String) properties.get("jdbc.user");
            String password = (String) properties.get("jdbc.password");
            String driverClass = (String) properties.get("jdbc.driverClass");
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void release(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }
}
