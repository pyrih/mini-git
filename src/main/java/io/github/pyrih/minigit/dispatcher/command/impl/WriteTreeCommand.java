package io.github.pyrih.minigit.dispatcher.command.impl;

import io.github.pyrih.minigit.component.Repository;
import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;

@CommandDefinition(name = "write-tree")
public class WriteTreeCommand implements Command {
    private final Repository repository;

    public WriteTreeCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String... arguments) {
        if (arguments.length != 0) {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }

        this.repository.writeTree(".");
    }
}
