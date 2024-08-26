package io.github.pyrih.minigit.dispatcher;

import io.github.pyrih.minigit.component.Repository;
import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;
import io.github.pyrih.minigit.dispatcher.command.impl.*;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ExplicitCommandDispatcher implements CommandDispatcher {
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    public ExplicitCommandDispatcher(Repository repository) {
        register(InitCommand.class, repository);
        register(HashObjectCommand.class, repository);
        register(CatFileCommand.class, repository);
        register(VersionCommand.class);
        register(HelpCommand.class);
    }

    public void dispatch(String commandDefinition, String... arguments) {
        Command command = COMMANDS.getOrDefault(
                commandDefinition,
                COMMANDS.get("--help"));

        if (command != null) {
            command.execute(arguments);
        } else {
            throw new IllegalArgumentException("Unknown command: " + commandDefinition);
        }
    }

    private void register(Class<? extends Command> cls, Repository repository) {
        registerCommand(cls, repository);
    }

    private void register(Class<? extends Command> cls) {
        registerCommand(cls, null);
    }

    private void registerCommand(Class<? extends Command> cls, Repository repository) {
        if (!cls.isAnnotationPresent(CommandDefinition.class)) {
            return;
        }

        CommandDefinition definition = cls.getAnnotation(CommandDefinition.class);

        try {
            Command command = (repository != null) ?
                    cls.getConstructor(Repository.class).newInstance(repository) :
                    cls.getConstructor().newInstance();

            COMMANDS.put(definition.name(), command);
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                 IllegalAccessException e) {
            throw new RuntimeException("Failed to register command: " + cls.getName(), e);
        }
    }
}
