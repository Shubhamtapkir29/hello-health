package com.hellohealth;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/testdb")
public class TestDBConnection extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Update these details with your RDS endpoint, username, password
    private static final String jdbcURL = "jdbc:mysql://hello-health.cn42u8e648n1.ap-south-1.rds.amazonaws.com:3306/hello_health";
    private static final String dbUser = "admin";
    private static final String dbPassword = "admin123";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            // Load MySQL Driver (for Tomcat 9, make sure mysql-connector-j.jar is in WEB-INF/lib)
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);

            String sql = "INSERT INTO users (name, email, password, role) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, "Dummy User");
            stmt.setString(2, "dummy@example.com");
            stmt.setString(3, "dummy123");
            stmt.setString(4, "patient");

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                out.println("<h2>✅ Database Connection Successful! Record Inserted.</h2>");
            } else {
                out.println("<h2>⚠️ Connection OK but Insert Failed.</h2>");
            }

            connection.close();
        } catch (Exception e) {
            out.println("<h2>❌ Database Connection Failed!</h2>");
            out.println("<pre>" + e.getMessage() + "</pre>");
            e.printStackTrace();
        }
    }
}
