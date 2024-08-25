package io.github.pyrih.minigit.dispatcher.command.impl;

import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;
import io.github.pyrih.minigit.logger.ConsoleLogger;

@CommandDefinition(name = "--version")
public class VersionCommand implements Command {
    @Override
    public void execute(String... parameters) {
        String version = VersionCommand.class.getPackage().getImplementationVersion();

        if (version == null) {
            ConsoleLogger.error("Unknown version");
            throw new RuntimeException("Error occurred while extracting version");
        }

        ConsoleLogger.info("minigit version: " + version);
    }
}
