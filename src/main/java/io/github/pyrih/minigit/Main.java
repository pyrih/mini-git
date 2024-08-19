package io.github.pyrih.minigit;

import io.github.pyrih.minigit.command.Command;
import io.github.pyrih.minigit.command.HelpCommand;
import io.github.pyrih.minigit.command.InitCommand;
import io.github.pyrih.minigit.component.Repository;

import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    public static void main(String[] args) {
        Repository repository = new Repository();
        register(new InitCommand(repository));

        Command command = COMMANDS.getOrDefault("init", new HelpCommand());
        command.execute();
    }

    private static void register(Command command) {
        COMMANDS.put(command.getName(), command);
    }
}
