package io.github.pyrih.minigit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while instantiating a message digest", e);
        }

        return Base64.getEncoder().encodeToString(digest.digest(input.getBytes(StandardCharsets.UTF_8)));
    }

    public static String generateSHA1Hash(String input) {
        if (input == null) {
            throw new IllegalArgumentException("An input value cannot be null.");
        }

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");

            byte[] bytes = input.getBytes();
            digest.update(bytes);
            byte[] digested = digest.digest();

            StringBuilder builder = new StringBuilder();
            for (byte b : digested) {
                builder.append(String.format("%02x", b));
            }

            return builder.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-1 algorithm is not available.");
        }
    }

    public static String readFileContent(Path path) {
        File toStoreFile = path.toFile();

        if (!toStoreFile.exists()) {
            throw new IllegalArgumentException(STR."A file \{toStoreFile.getAbsolutePath()} doesn't exist.");
        }

        try {
            return Files.readString(path);

        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading a file content");
        }
    }
}
