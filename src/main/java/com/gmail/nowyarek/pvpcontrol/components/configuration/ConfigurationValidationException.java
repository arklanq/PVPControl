package com.gmail.nowyarek.pvpcontrol.components.configuration;

public class ConfigurationValidationException extends Exception {

    public ConfigurationValidationException(Class<?> expectedType, String path) {
        super(String.format("`%s` must be of type `%s`", path, expectedType.getSimpleName()));
    }

    public ConfigurationValidationException(String message, String path) {
        super(message.replaceAll("\\{path}", path));
    }

}
