package io.github.pyrih.minigit.component;

import io.github.pyrih.minigit.logger.ConsoleLogger;
import io.github.pyrih.minigit.util.FileUtils;
import io.github.pyrih.minigit.util.HashingUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

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

    private static boolean isNotSymbolicLink(File entry) throws IOException {
        File canonicalFile = entry.getCanonicalFile();
        File absoluteFile = entry.getAbsoluteFile();
        return canonicalFile.equals(absoluteFile);
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

    private static boolean isIgnoredEntry(String path) {
        return path.contains(".git/")
                || path.contains(".minigit/")
                || path.contains(".idea/")
                || path.contains("target/");
    }

    public String hashObject(String parameter, String type) {
        // 1. Get the path of the file to store
        Path toStoreFilePath = Path.of(parameter);

        // 2. Read the file
        String content = null;
        if (type == null) {
            type = "blob";
        }

        if (type.equals("blob")) {
            content = FileUtils.readFileContent(toStoreFilePath);
        } else if (type.equals("tree")) {
            content = parameter;
        }

        // 3. Hash the content of the file using SHA-1
        String oid = HashingUtils.generateSHA1Hash(content);

        // 4. Store the file under ".minigit/objects/{hash}"
        Path targetPath = Path.of(GIT_DIRECTORY, OBJECTS_DIRECTORY, oid);
        System.out.println("A " + toStoreFilePath + " will be stored under the following path: " + targetPath);

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
        return oid;
    }

    public String writeTree(String directoryPathName) {
        List<Entry> entries = new ArrayList<>();
        File directory;

        try {
            directory = new File(directoryPathName).getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File[] files = directory.listFiles();

        if (files != null) {
            try {
                for (File entry : files) {
                    String fullPath = directoryPathName + File.separator + entry.getName();

                    if (isIgnoredEntry(fullPath)) {
                        continue;
                    }

                    String type;
                    String oid = null;

                    if (entry.isFile() && isNotSymbolicLink(entry)) {
                        // Write the file to the object database store
                        type = "blob";
                        oid = this.hashObject(fullPath, type);
                    } else if (entry.isDirectory() && isNotSymbolicLink(entry)) {
                        type = "tree";
                        oid = this.writeTree(fullPath);
                    } else {
                        ConsoleLogger.warn("Unknown entry: " + entry.getName());
                        continue;
                    }

                    entries.add(new Entry(entry.getName(), oid, type));
                }
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while writing tree", e);
            }
        }

        entries.sort(Comparator.comparing(element -> element.name));

        StringBuilder tree = new StringBuilder();
        for (Entry entry : entries) {
            tree.append(String.format("%s %s %s%n", entry.type, entry.oid, entry.name));
        }

        return this.hashObject(tree.toString(), "tree");
    }

    private static void emptyCurrentDirectory() throws IOException {
        Path currentDirectory = Paths.get(".");

        Files.walkFileTree(
                currentDirectory,
                new SimpleFileVisitor<>() {

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                        String relativePath = currentDirectory.relativize(file).toString();

                        if (!isIgnoredEntry(relativePath) && Files.isRegularFile(file)) {
                            Files.deleteIfExists(file);
                        }
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                        String relativePath = currentDirectory.relativize(dir).toString();

                        if (!isIgnoredEntry(relativePath)) {
                            try {
                                Files.delete(dir);
                            } catch (DirectoryNotEmptyException e) {
                                // Directory might not be empty if it contains ignored files, so we ignore this exception.
                            }
                        }
                        return FileVisitResult.CONTINUE;
                    }

                }
        );
    }

    public void readTree(String treeOid) {
        try {
            emptyCurrentDirectory();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while cleaning the current directory", e);
        }

        Map<String, String> tree = getTree(treeOid, ".");

        if (tree == null) {
            throw new IllegalArgumentException("Tree should not be null");
        }

        for (Map.Entry<String, String> entry : tree.entrySet()) {
            String path = entry.getKey();
            String oid = entry.getValue();

            Path filePath = Paths.get(path);

            try {
                Files.createDirectories(filePath.getParent());
                Files.write(filePath, this.catFile(oid, "blob").getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Error occurred while extracting tree from object database", e);
            }
        }
    }

    private Map<String, String> getTree(String oid, String basePath) {
        Map<String, String> result = new HashMap<>();

        for (Entry entry : iterateTreeEntries(oid)) {
            String name = entry.name();
            String entryOid = entry.oid();
            String type = entry.type();

            if (name.contains("/")) {
                throw new IllegalArgumentException("Name cannot contain '/' character: " + name);
            }

            if (name.equals("..") || name.equals(".")) {
                throw new IllegalArgumentException("Invalid name: " + name);
            }

            String path = basePath + name;

            switch (type) {
                case "blob" -> result.put(path, entryOid);
                case "tree" -> result.putAll(getTree(entryOid, path + "/"));
                default -> throw new IllegalArgumentException("Unknown tree entry type: " + type);
            }
        }

        return result;
    }

    private Iterable<? extends Entry> iterateTreeEntries(String oid) {
        if (oid == null || oid.isBlank()) {
            throw new IllegalArgumentException("OID cannot be null or empty.");
        }

        String tree = this.catFile(oid, "tree");
        String[] entries = tree.split("\n");

        return () -> new Iterator<Entry>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < entries.length;
            }

            @Override
            public Entry next() {
                String[] parts = entries[index++].split(" ", 3);
                return new Entry(parts[2], parts[1], parts[0]);
            }
        };
    }

    private record Entry(String name, String oid, String type) {
    }
}
