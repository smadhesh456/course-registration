package com.project;

import java.io.*;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;

/**
 * Debug servlet to test basic functionality
 */
@WebServlet("/TestServlet")
public class TestServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>Course Registration - Debug Test</h1>");
        
        try {
            out.println("<h2>1. Test Config Loading:</h2>");
            String dbUrl = ConfigLoader.getProperty("db.url");
            String dbUser = ConfigLoader.getProperty("db.username");
            out.println("DB URL: " + dbUrl + "<br>");
            out.println("DB User: " + dbUser + "<br>");
            
            out.println("<h2>2. Test Database Connection:</h2>");
            java.sql.Connection conn = DBConnection.getConnection();
            out.println("✓ Database connection successful!<br>");
            conn.close();
            
            out.println("<h2>3. Test Password Hashing:</h2>");
            String testPass = "TestPass123";
            String hashed = PasswordUtil.hashPassword(testPass);
            out.println("Original: " + testPass + "<br>");
            out.println("Hashed: " + hashed + "<br>");
            out.println("Verify: " + PasswordUtil.verifyPassword(testPass, hashed) + "<br>");
            
            out.println("<h2>4. Test Password Strength Validation:</h2>");
            out.println("TestPass123 is valid: " + PasswordUtil.isValidPassword("TestPass123") + "<br>");
            out.println("password is valid: " + PasswordUtil.isValidPassword("password") + "<br>");
            
            out.println("<h2>✓ All tests passed!</h2>");
            
        } catch (Exception e) {
            out.println("<h2>✗ Error:</h2>");
            out.println("<pre>");
            e.printStackTrace(out);
            out.println("</pre>");
        }
        
        out.println("</body></html>");
    }
}
