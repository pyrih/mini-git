package io.github.pyrih.minigit.dispatcher.command.impl;

import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;

@CommandDefinition(name = "help")
public class HelpCommand implements Command {
    @Override
    public void execute(String... parameters) {
        System.out.println("Help...");
    }
}
