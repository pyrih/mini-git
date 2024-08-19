package io.github.pyrih.minigit.component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Repository {
    public void init() {
        System.out.println("Initialize a new repository...");

        try {
            Path path = Path.of(".minigit");
            File dir = path.toFile();

            if (!dir.exists()) {
                Path directory = Files.createDirectory(path);
                System.out.println("Initialized an empty repository in " + directory.getFileName().toString());
            } else {
                System.out.println("A repository directory has been created already.");
            }


        } catch (IOException e) {
            throw new RuntimeException("Error while creating of a directory", e);
        }
    }

}
