package io.github.pyrih.minigit.component;

import io.github.pyrih.minigit.Utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

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
                System.out.println(STR."Initialized an empty repository in \{gitDirectoryPathCreated.getFileName().toString()} directory");
            } else {
                System.out.println(STR."A \{GIT_DIRECTORY} repository directory has been already created.");
            }

            Path objectsDirectoryPath = Path.of(GIT_DIRECTORY, OBJECTS_DIRECTORY);
            File objectsDirectory = objectsDirectoryPath.toFile();

            if (!objectsDirectory.exists()) {
                Path objectsDirectoryPathCreated = Files.createDirectory(objectsDirectoryPath);
                System.out.println(STR."An \{objectsDirectoryPathCreated.getFileName().toString()} directory has been created in \{GIT_DIRECTORY} repository directory.");
            } else {
                System.out.println(STR."An \{OBJECTS_DIRECTORY} directory has been already created.");
            }

        } catch (IOException e) {
            throw new RuntimeException("Error while creating of a directory", e);
        }
    }

    public void hashObject(String parameter) {
        // 1. Get the path of the file to store
        Path toStoreFilePath = Path.of(parameter);

        // 2. Read the file
        String content = Utils.readFileContent(toStoreFilePath);

        // 3. Hash the content of the file using SHA-1
        String oid = Utils.generateSHA1Hash(content);

        // 4. Store the file under ".minigit/objects/{hash}"
        Path targetPath = Path.of(GIT_DIRECTORY, OBJECTS_DIRECTORY, oid);
        System.out.println(STR."A \{toStoreFilePath} will be stored under the following path: \{targetPath}");

        if (targetPath.toFile().exists()) {
            System.out.println(STR."An existing \{targetPath} file will be replaced...");
        }

        try {
            Files.copy(toStoreFilePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while copying a file to an object database");
        }
    }

    public void catFile(String parameter) {
        Path path = Path.of(GIT_DIRECTORY, OBJECTS_DIRECTORY, parameter);

        if (!path.toFile().exists()) {
            throw new IllegalArgumentException(STR."A file under the \{path} path doesn't exist");
        }

        String content = Utils.readFileContent(path);
        System.out.println(content);
    }
}
