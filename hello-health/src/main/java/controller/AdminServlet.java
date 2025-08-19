package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.*;

public class AdminServlet extends HttpServlet {
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    try (Connection c = DBUtil.getConnection()) {
      req.setAttribute("users", c.createStatement().executeQuery("SELECT user_id,name,email,role FROM users"));
      req.setAttribute("doctors", c.createStatement().executeQuery(
        "SELECT d.doctor_id,u.name,d.specialization FROM doctors d JOIN users u ON d.user_id=u.user_id"));
      req.setAttribute("patients", c.createStatement().executeQuery(
        "SELECT p.patient_id,u.name,p.age,p.gender FROM patients p JOIN users u ON p.user_id=u.user_id"));
    } catch (Exception e) { e.printStackTrace(); }
    req.getRequestDispatcher("views/admin.jsp").forward(req, res);
  }
}
