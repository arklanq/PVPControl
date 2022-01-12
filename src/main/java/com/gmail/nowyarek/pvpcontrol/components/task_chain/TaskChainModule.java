package com.gmail.nowyarek.pvpcontrol.components.task_chain;

import co.aikar.taskchain.TaskChainFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

public class TaskChainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TaskChainFactoryProvider.class).in(Scopes.SINGLETON);
        bind(TaskChainFactory.class).toProvider(TaskChainFactoryProvider.class).in(Scopes.SINGLETON);
    }
}
