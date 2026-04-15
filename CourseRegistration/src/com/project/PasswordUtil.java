package com.project;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for password hashing and verification using PBKDF2.
 * This provides secure password storage without external dependencies.
 */
public class PasswordUtil {

    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 256;
    private static final int SALT_LENGTH = 16;

    /**
     * Hash a password with a randomly generated salt.
     * @param password The plain text password
     * @return A salted hash in format: salt$hash
     */
    public static String hashPassword(String password) {
        try {
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);

            byte[] hash = hashPassword(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);

            String saltStr = Base64.getEncoder().encodeToString(salt);
            String hashStr = Base64.getEncoder().encodeToString(hash);

            return saltStr + "$" + hashStr;
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify a plain text password against a stored hash.
     * @param password The plain text password to verify
     * @param storedHash The stored salted hash
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String storedHash) {
        try {
            String[] parts = storedHash.split("\\$");
            if (parts.length != 2) {
                return false;
            }

            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] hash = Base64.getDecoder().decode(parts[1]);

            byte[] computedHash = hashPassword(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);

            return MessageDigest.isEqual(hash, computedHash);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Compute PBKDF2 hash using Java's built-in javax.crypto.
     */
    private static byte[] hashPassword(char[] password, byte[] salt, int iterations, int keyLength) {
        try {
            javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                password, salt, iterations, keyLength);
            javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("Error in PBKDF2 hashing", e);
        }
    }

    /**
     * Validate password strength.
     * @param password The password to validate
     * @return true if password meets minimum requirements
     */
    public static boolean isValidPassword(String password) {
        // Minimum 8 characters, at least one uppercase, one lowercase, one digit
        return password != null && 
               password.length() >= 8 &&
               password.matches(".*[A-Z].*") &&
               password.matches(".*[a-z].*") &&
               password.matches(".*\\d.*");
    }
}
