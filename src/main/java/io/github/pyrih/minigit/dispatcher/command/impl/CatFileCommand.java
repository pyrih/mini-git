package io.github.pyrih.minigit.dispatcher.command.impl;

import io.github.pyrih.minigit.component.Repository;
import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;

@CommandDefinition(name = "cat-file")
public class CatFileCommand implements Command {
    private final Repository repository;

    public CatFileCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String... parameters) {
        if (parameters.length != 1) {
            throw new IllegalArgumentException("Wrong number of parameters.");
        }

        String parameter = parameters[0];

        this.repository.catFile(parameter);
    }
}
