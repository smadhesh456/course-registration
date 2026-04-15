package com.project;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database connection manager with externalized configuration.
 * Credentials are loaded from config.properties file.
 */
public class DBConnection {

    private static final String DB_URL = ConfigLoader.getProperty("db.url", "jdbc:mysql://localhost:3306/course_registration");
    private static final String DB_USER = ConfigLoader.getProperty("db.username", "root");
    private static final String DB_PASSWORD = ConfigLoader.getProperty("db.password", "");
    private static final String DB_DRIVER = ConfigLoader.getProperty("db.driver", "com.mysql.cj.jdbc.Driver");

    /**
     * Get a connection to the database.
     * @return A database connection
     * @throws Exception if connection fails
     */
    public static Connection getConnection() throws Exception {
        try {
            Class.forName(DB_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setAutoCommit(true);
            return conn;
        } catch (ClassNotFoundException e) {
            throw new Exception("MySQL JDBC Driver not found. Ensure mysql-connector-java is in the classpath.", e);
        } catch (Exception e) {
            throw new Exception("Database connection failed. Check server status and credentials in config.properties.", e);
        }
    }
}