package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class DoctorDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String doctorEmail = (String) session.getAttribute("email");
        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                "SELECT a.appointment_id, u.name AS patientName, a.appointment_date " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id=p.patient_id " +
                "JOIN users u ON p.user_id=u.user_id " +
                "JOIN doctors d ON a.doctor_id=d.doctor_id " +
                "JOIN users du ON d.user_id=du.user_id " +
                "WHERE du.email=?");
            ps.setString(1, doctorEmail);
            req.setAttribute("appointments", ps.executeQuery());
        } catch (Exception e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("views/doctor-dashboard.jsp").forward(req, res);
    }
}
