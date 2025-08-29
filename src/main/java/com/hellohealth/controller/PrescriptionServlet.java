package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class PrescriptionServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String appointmentId = req.getParameter("appointmentId");
        String prescriptionText = req.getParameter("prescription");

        try (Connection c = DBUtil.getConnection()) {
            PreparedStatement ps = c.prepareStatement(
                "INSERT INTO prescriptions (appointment_id, prescription_text) VALUES (?, ?)");
            ps.setInt(1, Integer.parseInt(appointmentId));
            ps.setString(2, prescriptionText);
            ps.executeUpdate();
            res.sendRedirect("doctorDashboard.jsp?success=1");
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect("doctorDashboard.jsp?error=1");
        }
    }
}
