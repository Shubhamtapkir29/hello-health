package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class PrescriptionServlet extends HttpServlet {
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String action = req.getParameter("action");
    try (Connection c = DBUtil.getConnection()) {
      if ("add".equals(action)) {
        int apptId = Integer.parseInt(req.getParameter("apptId"));
        int doctorId = Integer.parseInt(req.getParameter("doctorId"));
        int patientId = Integer.parseInt(req.getParameter("patientId"));
        String medicine = req.getParameter("medicine");
        String notes = req.getParameter("notes");
        PreparedStatement ps = c.prepareStatement(
          "INSERT INTO prescriptions(appt_id,doctor_id,patient_id,medicine,notes) VALUES(?,?,?,?,?)");
        ps.setInt(1, apptId); ps.setInt(2, doctorId); ps.setInt(3, patientId);
        ps.setString(4, medicine); ps.setString(5, notes);
        ps.executeUpdate();
      }
    } catch (Exception e) { e.printStackTrace(); }
    res.sendRedirect("DoctorDashboard");
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    // patient view: /Prescription?patientId=#
    int patientId = Integer.parseInt(req.getParameter("patientId"));
    try (Connection c = DBUtil.getConnection()) {
      PreparedStatement ps = c.prepareStatement(
        "SELECT pr.presc_id,pr.medicine,pr.notes,pr.appt_id, a.appt_date " +
        "FROM prescriptions pr JOIN appointments a ON pr.appt_id=a.appt_id WHERE pr.patient_id=? ORDER BY pr.presc_id DESC");
      ps.setInt(1, patientId);
      req.setAttribute("prescriptions", ps.executeQuery());
    } catch (Exception e) { e.printStackTrace(); }
    req.getRequestDispatcher("views/prescriptions.jsp").forward(req, res);
  }
}
