package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class DoctorDashboardServlet extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    HttpSession s = req.getSession(false);
    if (s == null) { res.sendRedirect("login.jsp"); return; }
    String email = (String) s.getAttribute("email");
    try (Connection c = DBUtil.getConnection()) {
      PreparedStatement pd = c.prepareStatement(
        "SELECT d.doctor_id,u.name,d.specialization FROM doctors d JOIN users u ON d.user_id=u.user_id WHERE u.email=?");
      pd.setString(1, email);
      ResultSet dr = pd.executeQuery();
      if (!dr.next()) { res.sendRedirect("login.jsp"); return; }
      int doctorId = dr.getInt("doctor_id");
      req.setAttribute("doctorId", doctorId);
      req.setAttribute("docName", dr.getString("name"));
      req.setAttribute("spec", dr.getString("specialization"));
      PreparedStatement ap = c.prepareStatement(
        "SELECT a.appt_id,a.appt_date,a.status,u.name AS patientName " +
        "FROM appointments a JOIN patients p ON a.patient_id=p.patient_id " +
        "JOIN users u ON p.user_id=u.user_id WHERE a.doctor_id=? ORDER BY a.appt_date DESC");
      ap.setInt(1, doctorId);
      req.setAttribute("appointments", ap.executeQuery());
    } catch (Exception e) { e.printStackTrace(); }
    req.getRequestDispatcher("views/doctor-dashboard.jsp").forward(req, res);
  }
}
