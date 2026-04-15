package com.project;

import java.io.InputStream;
import java.util.Properties;

/**
 * Loads application configuration from config.properties file.
 */
public class ConfigLoader {
    private static Properties properties;

    static {
        try {
            properties = new Properties();
            InputStream input = ConfigLoader.class.getClassLoader().getResourceAsStream("config.properties");
            
            if (input != null) {
                properties.load(input);
                input.close();
            } else {
                System.err.println("Warning: config.properties not found in classpath");
            }
        } catch (Exception e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
    }

    /**
     * Get a configuration property value.
     * @param key The property key
     * @param defaultValue The default value if key not found
     * @return The property value or default value
     */
    public static String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    /**
     * Get a configuration property value.
     * @param key The property key
     * @return The property value or null if not found
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
