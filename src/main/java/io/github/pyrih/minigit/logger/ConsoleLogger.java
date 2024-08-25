package io.github.pyrih.minigit.logger;

import java.io.PrintStream;

public class ConsoleLogger {

    private static final String RESET = "\033[0m";
    private static final ConsoleLogger INSTANCE = new ConsoleLogger(System.out);

    private final PrintStream out;

    private ConsoleLogger(PrintStream out) {
        this.out = out;
    }

    public static ConsoleLogger getInstance() {
        return INSTANCE;
    }

    public static void error(String message) {
        getInstance().log(LogLevel.ERROR, message);
    }

    public static void warn(String message) {
        getInstance().log(LogLevel.WARN, message);
    }

    public static void info(String message) {
        getInstance().log(LogLevel.INFO, message);
    }

    private void log(LogLevel level, String message) {
        out.println(level + level.name() + ":" + RESET + " " + message);
    }
}
