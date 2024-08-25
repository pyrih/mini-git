package io.github.pyrih.minigit.dispatcher;

public interface CommandDispatcher {
    void dispatch(String commandDefinition, String... arguments);
}
