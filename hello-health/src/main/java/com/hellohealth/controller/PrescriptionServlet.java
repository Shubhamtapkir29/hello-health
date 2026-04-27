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

@WebServlet("/Prescription")   // ✅ mapping added
public class PrescriptionServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(PrescriptionServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try (Connection c = DBUtil.getConnection()) {

            if ("add".equalsIgnoreCase(action)) {

                int apptId = Integer.parseInt(req.getParameter("apptId"));
                int doctorId = Integer.parseInt(req.getParameter("doctorId"));
                int patientId = Integer.parseInt(req.getParameter("patientId"));

                String medicine = req.getParameter("medicine");
                String notes = req.getParameter("notes");

                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO prescriptions(appt_id, doctor_id, patient_id, medicine, notes) VALUES (?, ?, ?, ?, ?)")) {

                    ps.setInt(1, apptId);
                    ps.setInt(2, doctorId);
                    ps.setInt(3, patientId);
                    ps.setString(4, medicine);
                    ps.setString(5, notes);

                    ps.executeUpdate();
                }
            }

        } catch (Exception e) {
            logger.error("Error adding prescription", e);
        }

        res.sendRedirect("doctor-dashboard"); // ✅ fixed URL
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        List<Map<String, String>> prescriptions = new ArrayList<>();

        try {
            String pid = req.getParameter("patientId");

            if (pid == null) {
                res.sendRedirect("login.jsp");
                return;
            }

            int patientId = Integer.parseInt(pid);

            try (Connection c = DBUtil.getConnection();
                 PreparedStatement ps = c.prepareStatement(
                         "SELECT pr.presc_id, pr.medicine, pr.notes, pr.appt_id, a.appt_date " +
                         "FROM prescriptions pr " +
                         "JOIN appointments a ON pr.appt_id=a.appt_id " +
                         "WHERE pr.patient_id=? ORDER BY pr.presc_id DESC")) {

                ps.setInt(1, patientId);

                try (ResultSet rs = ps.executeQuery()) {

                    while (rs.next()) {
                        Map<String, String> p = new HashMap<>();
                        p.put("id", rs.getString("presc_id"));
                        p.put("medicine", rs.getString("medicine"));
                        p.put("notes", rs.getString("notes"));
                        p.put("apptId", rs.getString("appt_id"));
                        p.put("date", rs.getString("appt_date"));
                        prescriptions.add(p);
                    }
                }
            }

        } catch (Exception e) {
            logger.error("Error fetching prescriptions", e);
            req.setAttribute("error", "Unable to load prescriptions");
        }

        req.setAttribute("prescriptions", prescriptions);

        req.getRequestDispatcher("views/prescriptions.jsp").forward(req, res);
    }
}
