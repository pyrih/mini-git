package io.github.pyrih.minigit.dispatcher.command.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandDefinition {
    String name();
}
