package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/PatientDashboard")   // ✅ mapping added
public class PatientDashboardServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(PatientDashboardServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // ✅ session validation
        if (session == null || session.getAttribute("email") == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String userEmail = (String) session.getAttribute("email");

        List<Map<String, String>> doctors = new ArrayList<>();

        try (Connection c = DBUtil.getConnection()) {

            // ✅ Patient Info
            try (PreparedStatement ps = c.prepareStatement(
                    "SELECT p.patient_id, u.name, p.age, p.gender " +
                    "FROM patients p JOIN users u ON p.user_id=u.user_id WHERE u.email=?")) {

                ps.setString(1, userEmail);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        req.setAttribute("patientId", rs.getInt("patient_id"));
                        req.setAttribute("name", rs.getString("name"));
                        req.setAttribute("age", rs.getInt("age"));
                        req.setAttribute("gender", rs.getString("gender"));
                    }
                }
            }

            // ✅ Doctors list (FIXED - no ResultSet passed)
            try (Statement st = c.createStatement();
                 ResultSet rs = st.executeQuery(
                        "SELECT d.doctor_id, u.name AS dname, d.specialization " +
                        "FROM doctors d JOIN users u ON d.user_id=u.user_id")) {

                while (rs.next()) {
                    Map<String, String> doc = new HashMap<>();
                    doc.put("id", rs.getString("doctor_id"));
                    doc.put("name", rs.getString("dname"));
                    doc.put("specialization", rs.getString("specialization"));
                    doctors.add(doc);
                }
            }

        } catch (Exception e) {
            logger.error("Error loading patient dashboard", e);
            req.setAttribute("error", "Unable to load dashboard");
        }

        // ✅ send clean data
        req.setAttribute("doctors", doctors);

        req.getRequestDispatcher("views/patient-dashboard.jsp").forward(req, res);
    }
}
