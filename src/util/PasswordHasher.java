package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for password hashing using SHA-256
 */
public class PasswordHasher {

    /**
     * Hash a password using SHA-256
     * @param password The plain text password
     * @return The hashed password as hexadecimal string
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            
            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    /**
     * Verify if a password matches its hash
     * @param password The plain text password to verify
     * @param hash The stored hash to compare against
     * @return true if password matches, false otherwise
     */
    public static boolean verifyPassword(String password, String hash) {
        String hashedInput = hashPassword(password);
        return hashedInput.equals(hash);
    }
}
