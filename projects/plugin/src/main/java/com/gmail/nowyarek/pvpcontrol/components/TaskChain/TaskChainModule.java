package com.gmail.nowyarek.pvpcontrol.components.TaskChain;

import co.aikar.taskchain.TaskChainFactory;
import com.google.inject.AbstractModule;

public class TaskChainModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TaskChainFactory.class).toProvider(TaskChainFactoryProvider.class).asEagerSingleton();
    }

}
