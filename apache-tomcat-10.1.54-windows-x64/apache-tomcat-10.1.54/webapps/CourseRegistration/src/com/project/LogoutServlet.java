package com.project;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        // Invalidate session
        HttpSession session = req.getSession();
        session.invalidate();
        
        // Redirect to login page
        res.sendRedirect("login.jsp");
    }
}