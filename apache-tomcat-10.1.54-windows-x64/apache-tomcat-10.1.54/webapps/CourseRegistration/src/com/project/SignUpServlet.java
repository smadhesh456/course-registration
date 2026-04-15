package com.project;

import java.io.*;
import java.sql.*;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.regex.Pattern;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

/**
 * Handles user registration with password hashing and input validation.
 */
@WebServlet("/SignUpServlet")
public class SignUpServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(SignUpServlet.class.getName());
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{10}$");

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String phone = req.getParameter("phone");
        String degree = req.getParameter("degree");

        // Trim all inputs
        fullname = fullname != null ? fullname.trim() : "";
        email = email != null ? email.trim() : "";
        username = username != null ? username.trim() : "";
        phone = phone != null ? phone.trim() : "";
        degree = degree != null ? degree.trim() : "";

        // Input validation
        if (fullname.isEmpty() || email.isEmpty() || username.isEmpty() || 
            password == null || phone.isEmpty() || degree.isEmpty()) {
            res.sendRedirect("signup.jsp?error=All fields are required");
            return;
        }

        // Confirm password check
        if (confirmPassword == null || !password.equals(confirmPassword)) {
            res.sendRedirect("signup.jsp?error=Passwords do not match");
            return;
        }

        // Email validation
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            res.sendRedirect("signup.jsp?error=Invalid email format");
            return;
        }

        // Phone validation (10 digits)
        if (!PHONE_PATTERN.matcher(phone).matches()) {
            res.sendRedirect("signup.jsp?error=Phone must be 10 digits");
            return;
        }

        // Username validation (alphanumeric, 3-20 characters)
        if (username.length() < 3 || username.length() > 20 || !username.matches("^[a-zA-Z0-9_]+$")) {
            res.sendRedirect("signup.jsp?error=Username must be 3-20 characters, alphanumeric and underscore only");
            return;
        }

        // Password strength validation
        if (!PasswordUtil.isValidPassword(password)) {
            res.sendRedirect("signup.jsp?error=Password must be at least 8 characters with uppercase, lowercase, and digit");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            // Check if username already exists
            try (PreparedStatement checkPs = con.prepareStatement(
                "SELECT id FROM students WHERE username=?")) {
                checkPs.setString(1, username);
                try (ResultSet checkRs = checkPs.executeQuery()) {
                    if (checkRs.next()) {
                        res.sendRedirect("signup.jsp?error=Username already exists");
                        return;
                    }
                }
            }

            // Check if email already exists
            try (PreparedStatement checkEmailPs = con.prepareStatement(
                "SELECT id FROM students WHERE email=?")) {
                checkEmailPs.setString(1, email);
                try (ResultSet checkEmailRs = checkEmailPs.executeQuery()) {
                    if (checkEmailRs.next()) {
                        res.sendRedirect("signup.jsp?error=Email already registered");
                        return;
                    }
                }
            }

            // Hash password before storing
            String hashedPassword = PasswordUtil.hashPassword(password);

            // Insert new student
            try (PreparedStatement ps = con.prepareStatement(
                "INSERT INTO students (fullname, email, username, password, phone, degree) VALUES (?, ?, ?, ?, ?, ?)")) {

                ps.setString(1, fullname);
                ps.setString(2, email);
                ps.setString(3, username);
                ps.setString(4, hashedPassword);
                ps.setString(5, phone);
                ps.setString(6, degree);

                int result = ps.executeUpdate();

                if (result > 0) {
                    logger.log(Level.INFO, "New user registered: " + username);
                    res.sendRedirect("login.jsp?success=Registration successful. Please login.");
                } else {
                    res.sendRedirect("signup.jsp?error=Registration failed. Please try again.");
                }
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database error during registration for: " + username, e);
            try {
                res.sendRedirect("signup.jsp?error=Database error occurred. Please try again.");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error redirecting after database error", ex);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Unexpected error during registration", e);
            try {
                res.sendRedirect("signup.jsp?error=An error occurred. Please try again.");
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Error redirecting after unexpected error", ex);
            }
        }
    }
}