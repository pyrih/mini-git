package io.github.pyrih.minigit.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.PosixFilePermission;
import java.util.Set;

class FileUtilsTest {

    @TempDir
    Path temporaryDirectory;

    private Path testFilePath;

    public static void setFileReadable(Path filePath, boolean readable) throws IOException {
        Set<PosixFilePermission> perms = Files.getPosixFilePermissions(filePath);

        if (readable) {
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.OTHERS_READ);
        } else {
            perms.remove(PosixFilePermission.OWNER_READ);
            perms.remove(PosixFilePermission.GROUP_READ);
            perms.remove(PosixFilePermission.OTHERS_READ);
        }

        Files.setPosixFilePermissions(filePath, perms);
    }

    @BeforeEach
    void setUp() throws IOException {
        testFilePath = temporaryDirectory.resolve("testFile.txt");
        Files.writeString(testFilePath, "Hello, World!");
    }

    @Test
    void readFileContent_Success() {
        String expected = "Hello, World!";
        String actual = FileUtils.readFileContent(testFilePath);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void readFileContent_FileDoesNotExist() {
        Path nonExistingFilePath = temporaryDirectory.resolve("nonExistingFile.txt");
        IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> FileUtils.readFileContent(nonExistingFilePath)
        );

        Assertions.assertTrue(
                exception.getMessage().contains(
                        STR."A file \{nonExistingFilePath.toFile().getAbsolutePath()} doesn't exist."
                )
        );
    }

    @Test
    void readFileContent_ErrorWhileReading() throws IOException {
        setFileReadable(this.testFilePath, false);

        RuntimeException exception = Assertions.assertThrows(
                RuntimeException.class,
                () -> FileUtils.readFileContent(this.testFilePath)
        );

        Assertions.assertEquals("Error occurred while reading a file content.", exception.getMessage());
        setFileReadable(this.testFilePath, true);
    }
}