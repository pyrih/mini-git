package io.github.pyrih.minigit.dispatcher;

import io.github.pyrih.minigit.component.Repository;
import io.github.pyrih.minigit.dispatcher.command.Command;
import io.github.pyrih.minigit.dispatcher.command.annotation.CommandDefinition;
import io.github.pyrih.minigit.dispatcher.command.impl.CatFileCommand;
import io.github.pyrih.minigit.dispatcher.command.impl.HashObjectCommand;
import io.github.pyrih.minigit.dispatcher.command.impl.HelpCommand;
import io.github.pyrih.minigit.dispatcher.command.impl.InitCommand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class ExplicitCommandDispatcher implements CommandDispatcher {
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    public ExplicitCommandDispatcher(Repository repository) {
        register(InitCommand.class, repository);
        register(HashObjectCommand.class, repository);
        register(CatFileCommand.class, repository);
    }

    public void dispatch(String commandDefinition, String... arguments) {
        Command command = COMMANDS.getOrDefault(commandDefinition, new HelpCommand());

        if (command != null) {
            command.execute(arguments);
        } else {
            throw new IllegalArgumentException("Unknown command: " + commandDefinition);
        }
    }

    private void register(Class<? extends Command> cls, Repository repository) {
        if (cls.isAnnotationPresent(CommandDefinition.class)) {
            CommandDefinition definition = cls.getAnnotation(CommandDefinition.class);

            try {
                Constructor<? extends Command> constructor = cls.getConstructor(Repository.class);
                Command command = constructor.newInstance(repository);
                COMMANDS.put(definition.name(), command);
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException("Failed to register command: " + cls.getName(), e);
            }
        }
    }
}
