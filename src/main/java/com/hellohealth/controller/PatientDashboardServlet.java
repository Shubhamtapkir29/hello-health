package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;
import javax.servlet.*;        // changed jakarta -> javax
import javax.servlet.http.*;  // changed jakarta -> javax
import java.io.IOException;
import java.sql.*;

public class PatientDashboardServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession s = req.getSession(false);
        if (s == null || s.getAttribute("email") == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String userEmail = (String) s.getAttribute("email");

        try (Connection c = DBUtil.getConnection()) {
            // get patient info
            PreparedStatement ps = c.prepareStatement(
                "SELECT p.patient_id,u.name,p.age,p.gender " +
                "FROM patients p JOIN users u ON p.user_id=u.user_id WHERE u.email=?");
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                req.setAttribute("patientId", rs.getInt("patient_id"));
                req.setAttribute("name", rs.getString("name"));
                req.setAttribute("age", rs.getInt("age"));
                req.setAttribute("gender", rs.getString("gender"));
            }

            // doctors list for dropdown (convert ResultSet to a List)
            PreparedStatement ps2 = c.prepareStatement(
                "SELECT d.doctor_id,u.name AS dname,d.specialization " +
                "FROM doctors d JOIN users u ON d.user_id=u.user_id");
            ResultSet rs2 = ps2.executeQuery();
            req.setAttribute("doctorsResultSet", rs2); // or map to List<User/Doctor>

        } catch (Exception e) {
            e.printStackTrace();
        }

        req.getRequestDispatcher("views/patient-dashboard.jsp").forward(req, res);
    }
}
