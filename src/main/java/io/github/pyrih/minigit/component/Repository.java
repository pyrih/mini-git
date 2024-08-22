package io.github.pyrih.minigit.component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

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

}
