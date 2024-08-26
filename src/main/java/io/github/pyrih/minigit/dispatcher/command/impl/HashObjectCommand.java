package io.github.pyrih.minigit.dispatcher.command.impl;

import io.github.pyrih.minigit.component.Repository;
import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;

@CommandDefinition(name = "hash-object")
public class HashObjectCommand implements Command {
    private final Repository repository;

    public HashObjectCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String... arguments) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }

        this.repository.hashObject(arguments[0], "blob");
    }
}
