package io.github.pyrih.minigit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Utils {

    private Utils() {
    }

    public static String encryptToSHA1(String input) {
        if (input == null) {
            throw new IllegalArgumentException("An input value cannot be null.");
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while instantiating a message digest", e);
        }

        return Base64.getEncoder().encodeToString(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
    }
}
