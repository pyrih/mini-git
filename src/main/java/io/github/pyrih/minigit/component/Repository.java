package io.github.pyrih.minigit.component;

import io.github.pyrih.minigit.logger.ConsoleLogger;
import io.github.pyrih.minigit.util.FileUtils;
import io.github.pyrih.minigit.util.HashingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class Repository {

    private static final String GIT_DIRECTORY = ".minigit";
    private static final String OBJECTS_DIRECTORY = "objects";

    public void init() {
        System.out.println("Initialize a new repository...");

        try {
            Path gitDirectoryPath = Path.of(GIT_DIRECTORY);
            File gitDirectory = gitDirectoryPath.toFile();

            if (!gitDirectory.exists()) {
                Path gitDirectoryPathCreated = Files.createDirectory(gitDirectoryPath);
                System.out.println("Initialized an empty repository in " + gitDirectoryPathCreated.getFileName().toString() + " directory");
            } else {
                System.out.println("A " + GIT_DIRECTORY + " repository directory has been already created.");
            }

            Path objectsDirectoryPath = Path.of(GIT_DIRECTORY, OBJECTS_DIRECTORY);
            File objectsDirectory = objectsDirectoryPath.toFile();

            if (!objectsDirectory.exists()) {
                Path objectsDirectoryPathCreated = Files.createDirectory(objectsDirectoryPath);
                System.out.println("An " + objectsDirectoryPathCreated.getFileName().toString() + " directory has been created in " + GIT_DIRECTORY + " repository directory.");
            } else {
                System.out.println("An " + OBJECTS_DIRECTORY + " directory has been already created.");
            }

        } catch (IOException e) {
            throw new RuntimeException("Error while creating of a directory", e);
        }
    }

    public void hashObject(String parameter, String type) {
        // 1. Get the path of the file to store
        Path toStoreFilePath = Path.of(parameter);

        // 2. Read the file
        String content = FileUtils.readFileContent(toStoreFilePath);

        // 3. Hash the content of the file using SHA-1
        String oid = HashingUtils.generateSHA1Hash(content);

        // 4. Store the file under ".minigit/objects/{hash}"
        Path targetPath = Path.of(GIT_DIRECTORY, OBJECTS_DIRECTORY, oid);
        System.out.println("A " + toStoreFilePath + " will be stored under the following path: " + targetPath);

        if (type == null) {
            type = "blob";
        }

        byte[] header = (type + "\0").getBytes();
        byte[] object = new byte[header.length + content.getBytes().length];

        System.arraycopy(header, 0, object, 0, header.length);
        System.arraycopy(content.getBytes(), 0, object, header.length, content.getBytes().length);

        if (targetPath.toFile().exists()) {
            System.out.println("An existing " + targetPath + " file will be replaced...");
        }

        try {
            Files.write(targetPath, object, StandardOpenOption.CREATE);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while copying a file to an object database");
        }
    }

    public String catFile(String oid, String expectedType) {
        Path path = Path.of(GIT_DIRECTORY, OBJECTS_DIRECTORY, oid);

        if (!path.toFile().exists()) {
            throw new IllegalArgumentException("A file under the " + path + " path doesn't exist");
        }

        byte[] content;

        try {
            byte[] object = Files.readAllBytes(path);

            int nullIndex = -1;

            for (int i = 0; i < object.length; i++) {
                if (object[i] == 0) {
                    nullIndex = i;
                    break;
                }
            }

            if (nullIndex == -1) {
                throw new IllegalArgumentException("Object format is invalid.");
            }

            String actualType = new String(object, 0, nullIndex);

            if (expectedType != null && !actualType.equals(expectedType)) {
                throw new IllegalArgumentException("Expected " + expectedType + ", got " + actualType);
            }

            content = new byte[object.length - nullIndex - 1];
            System.arraycopy(object, nullIndex + 1, content, 0, content.length);

            return new String(content);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean isNotSymbolicLink(File entry) throws IOException {
        File canonicalFile = entry.getCanonicalFile();
        File absoluteFile = entry.getAbsoluteFile();
        return canonicalFile.equals(absoluteFile);
    }

    public void writeTree(String directoryPathName) {
        File directory;

        try {
            directory = new File(directoryPathName).getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File[] files = directory.listFiles();

        if (files != null) {
            try {
                for (File file : files) {
                    String fullPath = directoryPathName + File.separator + file.getName();

                    if (file.isFile() && isNotSymbolicLink(file)) {
                        // TODO write the file to the object database store
                        ConsoleLogger.info(fullPath);
                    } else if (file.isDirectory() && isNotSymbolicLink(file)) {
                        writeTree(fullPath);
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while writing tree", e);
            }
        }
    }
}
