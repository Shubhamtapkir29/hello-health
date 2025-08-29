package com.hellohealth.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnectionTest {
    public static void main(String[] args) {
        String jdbcURL = "jdbc:mysql://hello-health.cn42u8e648n1.ap-south-1.rds.amazonaws.com:3306/hello_health";
        String dbUser = "admin";   // change if needed
        String dbPassword = "yourpassword";  // use same as RDS

        try {
            // Load MySQL driver (needed for Tomcat 9 / Java 8+)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection conn = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            if (conn != null) {
                System.out.println("✅ Database connection successful!");
                conn.close();
            } else {
                System.out.println("❌ Failed to connect.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
