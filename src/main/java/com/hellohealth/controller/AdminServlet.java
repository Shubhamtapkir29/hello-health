package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class AdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Map<String, String>> users = new ArrayList<>();
        List<Map<String, String>> doctors = new ArrayList<>();
        List<Map<String, String>> patients = new ArrayList<>();

        try (Connection c = DBUtil.getConnection()) {
            // Users
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT user_id, name, email, role FROM users");
            while (rs.next()) {
                Map<String, String> u = new HashMap<>();
                u.put("id", rs.getString("user_id"));
                u.put("name", rs.getString("name"));
                u.put("email", rs.getString("email"));
                u.put("role", rs.getString("role"));
                users.add(u);
            }

            // Doctors
            rs = st.executeQuery(
                "SELECT d.doctor_id, u.name, d.specialization FROM doctors d JOIN users u ON d.user_id=u.user_id");
            while (rs.next()) {
                Map<String, String> d = new HashMap<>();
                d.put("id", rs.getString("doctor_id"));
                d.put("name", rs.getString("name"));
                d.put("specialization", rs.getString("specialization"));
                doctors.add(d);
            }

            // Patients
            rs = st.executeQuery(
                "SELECT p.patient_id, u.name, p.age, p.gender FROM patients p JOIN users u ON p.user_id=u.user_id");
            while (rs.next()) {
                Map<String, String> p = new HashMap<>();
                p.put("id", rs.getString("patient_id"));
                p.put("name", rs.getString("name"));
                p.put("age", rs.getString("age"));
                p.put("gender", rs.getString("gender"));
                patients.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Send lists to JSP
        req.setAttribute("users", users);
        req.setAttribute("doctors", doctors);
        req.setAttribute("patients", patients);

        req.getRequestDispatcher("views/adminDashboard.jsp").forward(req, res);
    }
}
