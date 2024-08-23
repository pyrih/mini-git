package io.github.pyrih.minigit.dispatcher.command.impl;

import io.github.pyrih.minigit.component.Repository;
import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;

@CommandDefinition(name = "init")
public class InitCommand implements Command {
    private final Repository repository;

    public InitCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String... parameters) {
        this.repository.init();
    }
}
