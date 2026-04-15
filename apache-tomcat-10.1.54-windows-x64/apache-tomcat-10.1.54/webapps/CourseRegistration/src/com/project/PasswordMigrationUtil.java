package com.project;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Utility class for password migration from plaintext to hashed format.
 * Run this once to migrate existing passwords in the database.
 */
public class PasswordMigrationUtil {

    /**
     * Migrate all plaintext passwords to hashed format.
     * This should be run once after deploying the new security updates.
     * @throws Exception if migration fails
     */
    public static void migratePasswords() throws Exception {
        Connection con = DBConnection.getConnection();
        
        try {
            // Fetch all students with their current passwords
            String selectQuery = "SELECT id, password FROM students";
            try (PreparedStatement selectPs = con.prepareStatement(selectQuery);
                 ResultSet rs = selectPs.executeQuery()) {
                
                while (rs.next()) {
                    String id = rs.getString("id");
                    String plainPassword = rs.getString("password");
                    
                    // Skip if already hashed (contains $)
                    if (plainPassword.contains("$")) {
                        System.out.println("Password for ID " + id + " already hashed. Skipping...");
                        continue;
                    }
                    
                    // Hash the plaintext password
                    String hashedPassword = PasswordUtil.hashPassword(plainPassword);
                    
                    // Update the database with hashed password
                    String updateQuery = "UPDATE students SET password=? WHERE id=?";
                    try (PreparedStatement updatePs = con.prepareStatement(updateQuery)) {
                        updatePs.setString(1, hashedPassword);
                        updatePs.setString(2, id);
                        int updated = updatePs.executeUpdate();
                        if (updated > 0) {
                            System.out.println("Password for ID " + id + " hashed successfully.");
                        }
                    }
                }
            }
            
            System.out.println("Password migration completed successfully!");
            
        } catch (SQLException e) {
            throw new Exception("Password migration failed: " + e.getMessage(), e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Main method to run migration manually.
     * Usage: java com.project.PasswordMigrationUtil
     */
    public static void main(String[] args) {
        try {
            System.out.println("Starting password migration...");
            migratePasswords();
            System.out.println("Migration completed!");
        } catch (Exception e) {
            System.err.println("Migration failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
