package com.hellohealth.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    private static final String URL = "jdbc:mysql://hello-health.cn42u8e648n1.ap-south-1.rds.amazonaws.com:3306/hellohealth"; // change DB name if needed
    private static final String USER = "admin";  // your DB username
    private static final String PASSWORD = "admin123"; // your DB password

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // MySQL 8+ driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
