package io.github.pyrih.minigit.command;

public interface Command {
    void execute(String... parameters);

    String getName();
}
