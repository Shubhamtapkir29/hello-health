package com.hellohealth.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtil {

    private static final Logger logger = LoggerFactory.getLogger(DBUtil.class);

    // ✅ Use environment variables (DevOps friendly)
    private static final String URL = System.getenv().getOrDefault(
            "DB_URL",
            "jdbc:mysql://localhost:3306/hellohealth?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
    );

    private static final String USER = System.getenv().getOrDefault(
            "DB_USER",
            "root"
    );

    private static final String PASSWORD = System.getenv().getOrDefault(
            "DB_PASSWORD",
            "password"
    );

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            logger.info("MySQL Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            logger.error("Failed to load MySQL Driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            logger.info("Database connection established");
            return conn;
        } catch (SQLException e) {
            logger.error("Database connection failed", e);
            throw e;
        }
    }
}
