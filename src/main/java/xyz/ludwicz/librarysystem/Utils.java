package xyz.ludwicz.librarysystem;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {

    public static String hashPassword(char[] password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] byteArray = new byte[password.length * 2];
        for (int i = 0; i < password.length; i++) {
            byteArray[i * 2] = (byte) (password[i] >> 8);
            byteArray[i * 2 + 1] = (byte) password[i];
        }
        byte[] hash = md.digest(byteArray);
        return bytesToHex(hash);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
}