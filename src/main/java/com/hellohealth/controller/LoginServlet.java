package com.hellohealth.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/hello_health";
    private static final String JDBC_USER = "root";      // update with your DB user
    private static final String JDBC_PASS = "root";      // update with your DB password

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // load MySQL driver

            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
                String sql = "SELECT * FROM users WHERE email=? AND password=?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, email);
                    ps.setString(2, password);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            // ✅ Valid user
                            String role = rs.getString("role");
                            String name = rs.getString("name");

                            HttpSession session = request.getSession();
                            session.setAttribute("userName", name);
                            session.setAttribute("userEmail", email);
                            session.setAttribute("userRole", role);

                            // Redirect based on role
                            if ("admin".equalsIgnoreCase(role)) {
                                response.sendRedirect("admin-dashboard.jsp");
                            } else if ("doctor".equalsIgnoreCase(role)) {
                                response.sendRedirect("doctor-dashboard.jsp");
                            } else {
                                response.sendRedirect("patient-dashboard.jsp");
                            }
                        } else {
                            // ❌ Invalid login
                            response.sendRedirect("login.jsp?error=1");
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=1");
        }
    }
}
