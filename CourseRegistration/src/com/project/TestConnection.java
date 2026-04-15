package com.project;

import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        System.out.println("=== Database Connection Test ===");
        System.out.println();
        
        try {
            System.out.println("Attempting to connect to database...");
            System.out.println("URL: " + ConfigLoader.getProperty("db.url", "jdbc:mysql://localhost:3306/course_registration"));
            System.out.println("User: " + ConfigLoader.getProperty("db.username", "root"));
            System.out.println();
            
            Connection conn = DBConnection.getConnection();
            
            if (conn != null && !conn.isClosed()) {
                System.out.println("✅ SUCCESS: Database connection established!");
                System.out.println("Connection is valid and ready for use.");
                conn.close();
            } else {
                System.out.println("❌ FAILED: Connection returned null or is closed");
            }
        } catch (Exception e) {
            System.out.println("❌ FAILED: Database connection error");
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
