package io.github.pyrih.minigit.dispatcher.command.impl;

import io.github.pyrih.minigit.component.Repository;
import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;

@CommandDefinition(name = "read-tree")
public class ReadTreeCommand implements Command {
    private final Repository repository;

    public ReadTreeCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String... arguments) {
        if (arguments.length != 1) {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }

        String treeOid = arguments[0];
        this.repository.readTree(treeOid);
    }
}
