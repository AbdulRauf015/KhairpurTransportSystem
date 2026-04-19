package com.khairpur.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility for hashing passwords with SHA-256.
 * Passwords are never stored or compared in plain text.
 */
public class PasswordUtil {

    private PasswordUtil() {}

    /**
     * Returns the SHA-256 hex digest of the given plain-text password.
     *
     * @param plainText the raw password provided by the user
     * @return hex-encoded SHA-256 hash
     */
    public static String hash(String plainText) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // SHA-256 is guaranteed to be available in every JRE
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Checks whether a plain-text password matches a stored hash.
     *
     * @param plainText  raw password to verify
     * @param storedHash previously hashed password from the database
     * @return true if the password matches
     */
    public static boolean verify(String plainText, String storedHash) {
        return hash(plainText).equals(storedHash);
    }
}
