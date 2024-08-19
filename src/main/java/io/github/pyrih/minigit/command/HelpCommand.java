package io.github.pyrih.minigit.command;

public class HelpCommand implements Command {
    @Override
    public void execute(String... parameters) {
        System.out.println("Help...");
    }

    @Override
    public String getName() {
        return "help";
    }
}
