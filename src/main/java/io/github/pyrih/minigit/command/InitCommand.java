package io.github.pyrih.minigit.command;

import io.github.pyrih.minigit.component.Repository;

public class InitCommand implements Command {
    private final Repository repository;

    public InitCommand(Repository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(String... parameters) {
        this.repository.init();
    }

    @Override
    public String getName() {
        return "init";
    }
}
