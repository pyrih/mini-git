package io.github.pyrih.minigit.dispatcher.command.impl;

import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;
import io.github.pyrih.minigit.logger.ConsoleLogger;

@CommandDefinition(name = "--help")
public class HelpCommand implements Command {
    @Override
    public void execute(String... parameters) {
        ConsoleLogger.info("Help command is invoked...");
        ConsoleLogger.warn("Help command is invoked...");
        ConsoleLogger.error("Help command is invoked...");
    }
}
