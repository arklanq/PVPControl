package com.gmail.nowyarek.pvpcontrol.components.commands.prototype;

public class CommandExecutionException extends RuntimeException {

    public CommandExecutionException(String commandName, Throwable previous) {
        super(String.format("An error occurred during the execution of `%s` command.", commandName));
    }

}
