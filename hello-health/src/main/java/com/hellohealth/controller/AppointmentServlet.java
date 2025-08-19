package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

public class AppointmentServlet extends HttpServlet {
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String action = req.getParameter("action");
    try (Connection c = DBUtil.getConnection()) {
      if ("book".equals(action)) {
        int doctorId = Integer.parseInt(req.getParameter("doctorId"));
        int patientId = Integer.parseInt(req.getParameter("patientId"));
        String dt = req.getParameter("apptDateTime"); // "2025-08-22T14:30"
        PreparedStatement ps = c.prepareStatement(
          "INSERT INTO appointments(doctor_id,patient_id,appt_date,status) VALUES(?,?,?, 'BOOKED')");
        ps.setInt(1, doctorId);
        ps.setInt(2, patientId);
        ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.parse(dt)));
        ps.executeUpdate();
      } else if ("cancel".equals(action)) {
        int apptId = Integer.parseInt(req.getParameter("apptId"));
        PreparedStatement ps = c.prepareStatement("UPDATE appointments SET status='CANCELLED' WHERE appt_id=?");
        ps.setInt(1, apptId);
        ps.executeUpdate();
      }
    } catch (Exception e) { e.printStackTrace(); }
    res.sendRedirect("PatientDashboard");
  }
}
