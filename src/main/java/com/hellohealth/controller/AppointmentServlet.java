package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class AppointmentServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String patientId = req.getParameter("patientId");
        String doctorId = req.getParameter("doctorId");
        String date = req.getParameter("date");

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)");
            ps.setInt(1, Integer.parseInt(patientId));
            ps.setInt(2, Integer.parseInt(doctorId));
            ps.setString(3, date);
            ps.executeUpdate();
            res.sendRedirect("patientDashboard.jsp?success=1");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("patientDashboard.jsp?error=1");
        }
    }
}
