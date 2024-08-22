package io.github.pyrih.minigit.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtils {
    private FileUtils() {
    }

    public static String readFileContent(Path path) {
        File toStoreFile = path.toFile();

        if (!toStoreFile.exists()) {
            throw new IllegalArgumentException(STR."A file \{toStoreFile.getAbsolutePath()} doesn't exist.");
        }

        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while reading a file content.");
        }
    }
}
