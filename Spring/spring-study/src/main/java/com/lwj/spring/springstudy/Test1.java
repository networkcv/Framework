package com.lwj.spring.springstudy;

import java.sql.SQLException;

/**
 * create by lwj on 2020/1/20
 */
public class Test1 {

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
//        Class.forName("com.mysql.jdbc.Driver");
        Class.forName("java.sql.DriverManager");
//        DriverManager.getConnection("url","username","password");
//        System.out.println(System.getProperty("java.system.class.loader"));
    }
}
