package io.github.pyrih.minigit.command;

import io.github.pyrih.minigit.component.Repository;

public class HashObjectCommand implements Command {
    private final Repository repository;

    public HashObjectCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String... parameters) {
        if (parameters.length != 1) {
            throw new IllegalArgumentException("Wrong number of parameters.");
        }

        String parameter = parameters[0];

        this.repository.hashObject(parameter);
    }

    @Override
    public String getName() {
        return "hash-object";
    }
}
