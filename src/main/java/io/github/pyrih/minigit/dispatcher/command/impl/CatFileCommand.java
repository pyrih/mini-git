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
    public void execute(String... arguments) {
        if (arguments.length != 1 && arguments.length != 2) {
            throw new IllegalArgumentException("Wrong number of arguments.");
        }

        if (arguments.length == 1) {
            String content = this.repository.catFile(arguments[0], null);
            System.out.println(content);
        }

        if (arguments.length == 2) {
            String content = this.repository.catFile(arguments[0], arguments[1]);
            System.out.println(content);
        }

    }
}
