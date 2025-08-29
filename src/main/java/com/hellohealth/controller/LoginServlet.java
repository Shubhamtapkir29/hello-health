package com.hellohealth.controller;

import com.hellohealth.dao.UserDAO;
import com.hellohealth.model.User;
import javax.servlet.*;        // changed jakarta -> javax
import javax.servlet.http.*;  // changed jakarta -> javax
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
            session.setAttribute("email", user.getEmail()); // needed for PatientDashboardServlet

            switch (user.getRole().toLowerCase()) {
                case "doctor":
                    response.sendRedirect("doctorDashboard.jsp");
                    break;
                case "patient":
                    response.sendRedirect("patientDashboard.jsp");
                    break;
                case "admin":
                    response.sendRedirect("adminDashboard.jsp");
                    break;
                default:
                    response.sendRedirect("login.jsp?error=invalid");
                    break;
            }
        } else {
            response.sendRedirect("login.jsp?error=invalid");
        }
    }
}
