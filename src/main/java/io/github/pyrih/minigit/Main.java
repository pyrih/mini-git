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

        if (args.length < 1) {
            System.out.println("No command provided. Bye!");
            return;
        }

        String name = args[0].toLowerCase();
        Command command = COMMANDS.getOrDefault(name, new HelpCommand());

        if (args.length == 1) {
            command.execute();
        }

        String[] parameters = new String[args.length - 1];
        System.arraycopy(args, 1, parameters, 0, args.length - 1);

        command.execute(parameters);

    }

    private static void register(Command command) {
        COMMANDS.put(
                command.getName().toLowerCase(),
                command
        );
    }
}
