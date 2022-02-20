package com.gmail.nowyarek.pvpcontrol.components.configuration;

public class ConfigInitializationException extends Exception {

    public ConfigInitializationException(String configName, Throwable previous) {
        super(String.format("Failed to initailize config `%s`.", configName), previous);
    }

}
