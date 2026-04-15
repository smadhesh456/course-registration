package com.project;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;

public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        String fullname = req.getParameter("fullname");
        String email = req.getParameter("email");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        String degree = req.getParameter("degree");

        // Input validation
        if (fullname == null || fullname.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            username == null || username.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            phone == null || phone.trim().isEmpty() ||
            degree == null || degree.trim().isEmpty()) {
            res.sendRedirect("signup.jsp?error=All fields are required");
            return;
        }

        // Basic email validation
        if (!email.contains("@") || !email.contains(".")) {
            res.sendRedirect("signup.jsp?error=Invalid email format");
            return;
        }

        try {
            Connection con = DBConnection.getConnection();

            // Check if username already exists
            try (PreparedStatement checkPs = con.prepareStatement(
                "SELECT * FROM students WHERE username=?")) {
                checkPs.setString(1, username.trim());
                try (ResultSet checkRs = checkPs.executeQuery()) {
                    if (checkRs.next()) {
                        res.sendRedirect("signup.jsp?error=Username already exists");
                        return;
                    }
                }
            }

            // Check if email already exists
            try (PreparedStatement checkEmailPs = con.prepareStatement(
                "SELECT * FROM students WHERE email=?")) {
                checkEmailPs.setString(1, email.trim());
                try (ResultSet checkEmailRs = checkEmailPs.executeQuery()) {
                    if (checkEmailRs.next()) {
                        res.sendRedirect("signup.jsp?error=Email already registered");
                        return;
                    }
                }
            }

            // Insert new student (matching database schema)
            String query = "INSERT INTO students (fullname, email, username, password, phone, degree) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setString(1, fullname.trim());
                ps.setString(2, email.trim());
                ps.setString(3, username.trim());
                ps.setString(4, password); // Note: Consider hashing passwords
                ps.setString(5, phone.trim());
                ps.setString(6, degree.trim());

                int i = ps.executeUpdate();

                if (i > 0) {
                    res.sendRedirect("login.jsp?success=Registration successful. Please login.");
                } else {
                    res.sendRedirect("signup.jsp?error=Registration failed. Try again.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            try {
                res.sendRedirect("signup.jsp?error=Database error occurred. Please try again.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                res.sendRedirect("signup.jsp?error=An error occurred. Please try again.");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}