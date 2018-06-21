package ru.geekbrains.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class AuthUtils {
    public boolean checkLoginMistake(String login) {
        return !login.isEmpty() && checkChar(login);
    }

    public boolean checkPasswordMistake(String password) {
        return !password.isEmpty();
    }

    public boolean checkEmailMistake(String email) {
        String tempEmail = email;
        return tempEmail.contains("@") &&
                tempEmail.contains(".") &&
                checkChar(tempEmail.replace("@", "").replace(".", ""));
    }

    public String encryptSHA256(String text) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte aHash : hash) {
            String hex = Integer.toHexString(0xff & aHash);
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private boolean checkChar(String value) {
        return value.matches("[a-zA-Z0-9]*");
    }
}
