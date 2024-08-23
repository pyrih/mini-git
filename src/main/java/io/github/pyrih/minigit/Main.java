package io.github.pyrih.minigit;

import io.github.pyrih.minigit.component.Repository;
import io.github.pyrih.minigit.dispatcher.CommandDispatcher;
import io.github.pyrih.minigit.util.ArgumentUtils;

public class Main {
    public static void main(String[] args) {
        ArgumentUtils.validateArguments(args);

        Repository repository = new Repository();
        CommandDispatcher dispatcher = new CommandDispatcher(repository);

        String commandDefinition = args[0].toLowerCase();

        if (args.length == 1) {
            dispatcher.dispatch(commandDefinition);
        } else {
            String[] arguments = ArgumentUtils.extractRemainingArguments(args);
            dispatcher.dispatch(commandDefinition, arguments);
        }
    }
}
