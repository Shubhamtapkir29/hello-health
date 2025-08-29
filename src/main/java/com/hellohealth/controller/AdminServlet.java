package com.hellohealth.controller;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        // Load admin data (placeholder logic)
        req.setAttribute("adminMessage", "Welcome to Admin Dashboard!");
        req.getRequestDispatcher("views/admin-dashboard.jsp").forward(req, res);
    }
}
