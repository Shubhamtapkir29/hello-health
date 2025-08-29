package com.hellohealth.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.hellohealth.dao.UserDAO;
import com.hellohealth.model.User;
import com.hellohealth.util.EmailUtil;

@WebServlet("/register")
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
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String role = request.getParameter("role");

        User newUser = new User(username, password, email, role);

        boolean userAdded = userDAO.addUser(newUser);

        if (userAdded) {
            // Send confirmation email
            String subject = "Welcome to HelloHealth";
            String message = "Dear " + username + ",\n\nThank you for registering on HelloHealth!";
            EmailUtil.sendEmail(email, subject, message);

            HttpSession session = request.getSession();
            session.setAttribute("username", username);

            response.sendRedirect("login.jsp");
        } else {
            response.sendRedirect("register.jsp?error=1");
        }
    }
}
