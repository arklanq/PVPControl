package com.gmail.nowyarek.pvpcontrol.components.commands;

import com.google.inject.AbstractModule;

public class CommandsModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(HelpCommand.class);
        bind(ReloadCommand.class);
        bind(MainCommand.class);
        bind(CommandsRegistry.class).asEagerSingleton();
    }
}
