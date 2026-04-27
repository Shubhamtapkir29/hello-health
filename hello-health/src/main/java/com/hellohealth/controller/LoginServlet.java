package com.hellohealth.controller;

import com.hellohealth.dao.UserDAO;
import com.hellohealth.model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet("/login")   // ✅ important
public class LoginServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(LoginServlet.class);
    private final UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            User user = userDAO.validateUser(email, password);

            if (user != null) {

                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("email", user.getEmail()); // ✅ MUST

                logger.info("Login success: {}", email);

                // ✅ Redirect to SERVLETS (correct flow)
                switch (user.getRole().toLowerCase()) {

                    case "doctor":
                        response.sendRedirect("doctor-dashboard");
                        break;

                    case "patient":
                        response.sendRedirect("PatientDashboard"); // ensure servlet exists
                        break;

                    case "admin":
                        response.sendRedirect("admin");
                        break;

                    default:
                        response.sendRedirect("login.jsp?error=role");
                }

            } else {
                logger.warn("Invalid login: {}", email);
                response.sendRedirect("login.jsp?error=invalid");
            }

        } catch (Exception e) {
            logger.error("Login error", e);
            response.sendRedirect("login.jsp?error=server");
        }
    }
}
