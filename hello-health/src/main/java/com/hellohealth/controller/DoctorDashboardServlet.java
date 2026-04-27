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

@WebServlet("/doctor-dashboard")  // ✅ mapping added
public class DoctorDashboardServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(DoctorDashboardServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // ✅ Session validation
        if (session == null || session.getAttribute("email") == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String email = (String) session.getAttribute("email");

        List<Map<String, String>> appointments = new ArrayList<>();

        try (Connection c = DBUtil.getConnection()) {

            // ✅ Get doctor info
            try (PreparedStatement pd = c.prepareStatement(
                    "SELECT d.doctor_id, u.name, d.specialization " +
                    "FROM doctors d JOIN users u ON d.user_id=u.user_id WHERE u.email=?")) {

                pd.setString(1, email);

                try (ResultSet dr = pd.executeQuery()) {

                    if (!dr.next()) {
                        res.sendRedirect("login.jsp");
                        return;
                    }

                    int doctorId = dr.getInt("doctor_id");

                    req.setAttribute("doctorId", doctorId);
                    req.setAttribute("docName", dr.getString("name"));
                    req.setAttribute("spec", dr.getString("specialization"));

                    // ✅ Fetch appointments
                    try (PreparedStatement ap = c.prepareStatement(
                            "SELECT a.appt_id, a.appt_date, a.status, u.name AS patientName " +
                            "FROM appointments a " +
                            "JOIN patients p ON a.patient_id=p.patient_id " +
                            "JOIN users u ON p.user_id=u.user_id " +
                            "WHERE a.doctor_id=? ORDER BY a.appt_date DESC")) {

                        ap.setInt(1, doctorId);

                        try (ResultSet rs = ap.executeQuery()) {

                            while (rs.next()) {
                                Map<String, String> appt = new HashMap<>();
                                appt.put("id", rs.getString("appt_id"));
                                appt.put("date", rs.getString("appt_date"));
                                appt.put("status", rs.getString("status"));
                                appt.put("patientName", rs.getString("patientName"));
                                appointments.add(appt);
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error loading doctor dashboard", e);
            req.setAttribute("error", "Unable to load dashboard");
        }

        // ✅ Send clean data (NOT ResultSet)
        req.setAttribute("appointments", appointments);

        req.getRequestDispatcher("views/doctor-dashboard.jsp").forward(req, res);
    }
}
