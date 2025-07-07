package util;

import java.security.MessageDigest;

public class PasswordHasher {

    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // ✅ Add this method for comparison
    public static boolean verifyPassword(String rawPassword, String hashedPasswordFromDB) {
        String hashedInput = hashPassword(rawPassword);
        return hashedInput.equals(hashedPasswordFromDB);
    }
}
