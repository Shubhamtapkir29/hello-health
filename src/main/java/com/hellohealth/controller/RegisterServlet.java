package com.hellohealth.controller;

import com.hellohealth.dao.UserDAO;
import com.hellohealth.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // accept either "name" or "fullname" so your JSP can use either
        String name = request.getParameter("name");
        if (name == null || name.trim().isEmpty()) {
            name = request.getParameter("fullname");
        }
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        if (role == null || role.trim().isEmpty()) {
            // default role if not provided
            role = "patient";
        }

        // basic validation (you can expand)
        if (name == null || email == null || password == null ||
            name.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
            response.sendRedirect("register.jsp?error=1");
            return;
        }

        User user = new User();
        user.setName(name.trim());
        user.setEmail(email.trim());
        user.setPassword(password); // note: plain text — consider hashing in future
        user.setRole(role.trim());

        boolean created = userDAO.registerUser(user); // uses your UserDAO.registerUser(...)
        if (created) {
            // login.jsp checks "registered" parameter in your code
            response.sendRedirect("login.jsp?registered=1");
        } else {
            response.sendRedirect("register.jsp?error=1");
        }
    }
}
