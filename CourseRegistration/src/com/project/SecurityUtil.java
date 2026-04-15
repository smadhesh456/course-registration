package com.project;

import java.util.regex.Pattern;

/**
 * Utility class for security-related functions.
 */
public class SecurityUtil {

    private static final Pattern XSS_PATTERN = Pattern.compile(
        "<script|javascript:|onerror=|onload=|onclick=|<iframe|<embed|<object",
        Pattern.CASE_INSENSITIVE);

    /**
     * Sanitize input to prevent XSS attacks.
     * @param input The input string to sanitize
     * @return Sanitized string
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        
        if (XSS_PATTERN.matcher(input).find()) {
            throw new IllegalArgumentException("Invalid input detected");
        }
        
        return input.replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;")
                    .replaceAll("'", "&#x27;")
                    .replaceAll("&", "&amp;");
    }

    /**
     * Validate email format using RFC 5322 simplified pattern.
     * @param email The email to validate
     * @return true if email is valid
     */
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Pattern.compile(
            "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$"
        ).matcher(email).matches();
    }

    /**
     * Check if input contains potentially dangerous SQL patterns.
     * Note: Use PreparedStatements for SQL injection prevention instead.
     * @param input The input to check
     * @return true if suspicious patterns are found
     */
    public static boolean containsSuspiciousSQLPatterns(String input) {
        if (input == null) {
            return false;
        }
        
        String lowerInput = input.toLowerCase();
        String[] sqlKeywords = {"drop", "delete", "insert", "update", "union", 
                               "select", "exec", "execute", "--", "/*", "*/"};
        
        for (String keyword : sqlKeywords) {
            if (lowerInput.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}
