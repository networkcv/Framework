package com.lwj.orm.client._01_JDBC_query;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Date: 2021/10/27
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class JDBCQueryDatabase {
    public static void main(String[] args) throws Exception {
        //1.加载数据库驱动
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取数据库连接会话
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/orm?characterEncoding=utf-8", "root", "root");
        //3.获取预处理statement
        String sql = "select * from user where username = ?";
        PreparedStatement ps = connection.prepareStatement(sql);
        //4.设置参数
        ps.setString(1, "tom");
        //执行语句
        ResultSet resultSet = ps.executeQuery();
        //遍历结果集
        User user = null;
        while (resultSet.next()) {
            user = User.builder()
                    .id(resultSet.getInt("id"))
                    .username(resultSet.getString("username"))
                    .build();
        }
        System.out.println(user);
    }
}
