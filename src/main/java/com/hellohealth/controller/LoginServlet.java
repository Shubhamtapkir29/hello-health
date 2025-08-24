package com.hellohealth.controller;

import com.hellohealth.dao.UserDAO;
import com.hellohealth.model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class LoginServlet extends HttpServlet {
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = userDAO.validateUser(email, password);
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if ("doctor".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("doctorDashboard.jsp");
            } else if ("patient".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("patientDashboard.jsp");
            } else if ("admin".equalsIgnoreCase(user.getRole())) {
                response.sendRedirect("adminDashboard.jsp");
            } else {
                response.sendRedirect("login.jsp?error=invalid");
            }
        } else {
            response.sendRedirect("login.jsp?error=invalid");
        }
    }
}
