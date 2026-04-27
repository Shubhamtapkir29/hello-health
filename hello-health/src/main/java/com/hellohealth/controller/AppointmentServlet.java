package com.hellohealth.controller;

import com.hellohealth.dao.DBUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/appointment")  // ✅ important mapping
public class AppointmentServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentServlet.class);

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try (Connection c = DBUtil.getConnection()) {

            if ("book".equalsIgnoreCase(action)) {

                int doctorId = Integer.parseInt(req.getParameter("doctorId"));
                int patientId = Integer.parseInt(req.getParameter("patientId"));
                String dt = req.getParameter("apptDateTime");

                try (PreparedStatement ps = c.prepareStatement(
                        "INSERT INTO appointments(doctor_id, patient_id, appt_date, status) VALUES (?, ?, ?, 'BOOKED')")) {

                    ps.setInt(1, doctorId);
                    ps.setInt(2, patientId);
                    ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.parse(dt)));

                    ps.executeUpdate();
                }

            } else if ("cancel".equalsIgnoreCase(action)) {

                int apptId = Integer.parseInt(req.getParameter("apptId"));

                try (PreparedStatement ps = c.prepareStatement(
                        "UPDATE appointments SET status='CANCELLED' WHERE appt_id=?")) {

                    ps.setInt(1, apptId);
                    ps.executeUpdate();
                }

            } else {
                logger.warn("Invalid action received: {}", action);
            }

        } catch (Exception e) {
            logger.error("Error processing appointment request", e);
            req.setAttribute("error", "Operation failed. Please try again.");
        }

        // ✅ Redirect after action
        res.sendRedirect("PatientDashboard");
    }
}
