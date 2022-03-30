package com.gmail.nowyarek.pvpcontrol.components.commands.prototype;

public class CommandAutoCompletationException extends RuntimeException {

    public CommandAutoCompletationException(String commandName, Throwable previous) {
        super(String.format("An error occurred while collecting tab auto-complete suggestions for `%s` command.", commandName));
    }

}
