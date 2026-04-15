package com.project;

import java.io.*;
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

/**
 * Handles user login with password verification using secure hashing.
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        // Input validation
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            res.sendRedirect("login.jsp?error=invalid_input");
            return;
        }

        username = username.trim();

        try {
            Connection con = DBConnection.getConnection();

            // Use try-with-resources to ensure proper resource closure
            try (PreparedStatement ps = con.prepareStatement(
                "SELECT id, fullname, email, phone, degree, username, password FROM students WHERE username=?")) {

                ps.setString(1, username);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String storedHash = rs.getString("password");
                        
                        // Verify password using secure hash comparison
                        if (PasswordUtil.verifyPassword(password, storedHash)) {
                            // Get session
                            HttpSession session = req.getSession();
                            
                            // Store student details in session
                            session.setAttribute("userId", rs.getString("id"));
                            session.setAttribute("fullname", rs.getString("fullname"));
                            session.setAttribute("email", rs.getString("email"));
                            session.setAttribute("phone", rs.getString("phone"));
                            session.setAttribute("degree", rs.getString("degree"));
                            session.setAttribute("username", rs.getString("username"));
                            
                            logger.log(Level.INFO, "User login successful: " + username);
                            
                            // Redirect to dashboard
                            res.sendRedirect("dashboard.jsp");
                        } else {
                            logger.log(Level.WARNING, "Invalid password for user: " + username);
                            res.sendRedirect("login.jsp?error=invalid_credentials");
                        }
                    } else {
                        logger.log(Level.WARNING, "User not found: " + username);
                        res.sendRedirect("login.jsp?error=invalid_credentials");
                    }
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during login", e);
            try {
                res.sendRedirect("login.jsp?error=database_error");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error redirecting after database error", ex);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during login", e);
            try {
                res.sendRedirect("login.jsp?error=unknown_error");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error redirecting after unexpected error", ex);
            }
        }
    }
}