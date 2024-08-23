package io.github.pyrih.minigit.util;

public class ArgumentUtils {
    private ArgumentUtils() {
    }

    public static void validateArguments(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException("No command provided.");
        }

        String commandDefinition = args[0].toLowerCase();
        if (commandDefinition.isBlank()) {
            throw new IllegalArgumentException("Command definition is blank.");
        }
    }

    public static String[] extractRemainingArguments(String[] args) {
        String[] arguments = new String[args.length - 1];
        System.arraycopy(args, 1, arguments, 0, args.length - 1);
        return arguments;
    }
}
