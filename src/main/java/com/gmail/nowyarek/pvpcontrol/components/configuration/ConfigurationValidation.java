package com.gmail.nowyarek.pvpcontrol.components.configuration;

import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class ConfigurationValidation {
    private final ConfigurationSection section;

    public ConfigurationValidation(ConfigurationSection section) {
        this.section = section;
    }

    public String requireString(String path) throws ConfigurationValidationException {
        return this.requireString(path, null);
    }

    public String requireString(String path, @Nullable String message) throws ConfigurationValidationException {
        String val = section.getString(path);
        if (val == null) {
            if (message != null) throw new ConfigurationValidationException(message, this.joinPath(path));
            else throw new ConfigurationValidationException(String.class, this.joinPath(path));
        }
        return val;
    }

    public String requireStringEnum(String path, String[] allowedValues) throws ConfigurationValidationException {
        return this.requireStringEnum(path, allowedValues, null);
    }
    public String requireStringEnum(String path, String[] allowedValues, @Nullable String message) throws ConfigurationValidationException {
        String val = this.requireString(path).trim();
        List<String> allowedValuesList = Arrays.asList(allowedValues);
        if (!allowedValuesList.contains(val)) {
            if (message != null) throw new ConfigurationValidationException(message, this.joinPath(path));
            else throw new ConfigurationValidationException(
                String.format(
                    "`{path}` must be one of: %s.",
                    allowedValuesList.stream().reduce((acc, element) -> acc.concat(", " + element))
                ),
                this.joinPath(path)
            );
        }
        return val;
    }

    public int requireInt(String path) throws ConfigurationValidationException {
        return this.requireInt(path, null);
    }

    public int requireInt(String path, @Nullable String message) throws ConfigurationValidationException {
        String val = this.requireString(path);
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            if (message != null) throw new ConfigurationValidationException(message, this.joinPath(path));
            else throw new ConfigurationValidationException(Integer.class, this.joinPath(path));
        }
    }

    public double requireDouble(String path) throws ConfigurationValidationException {
        return this.requireDouble(path, null);
    }

    public double requireDouble(String path, @Nullable String message) throws ConfigurationValidationException {
        String val = this.requireString(path);
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            if (message != null) throw new ConfigurationValidationException(message, this.joinPath(path));
            else throw new ConfigurationValidationException(Double.class, this.joinPath(path));
        }
    }

    public boolean requireBoolean(String path) throws ConfigurationValidationException {
        return this.requireBoolean(path, null);
    }

    public boolean requireBoolean(String path, @Nullable String message) throws ConfigurationValidationException {
        String val = this.requireString(path).trim().toLowerCase();
        List<String> allowedValues = Arrays.asList("true", "false");
        if (!allowedValues.contains(val)) {
            if (message != null) throw new ConfigurationValidationException(message, this.joinPath(path));
            else throw new ConfigurationValidationException(Boolean.class, this.joinPath(path));
        }
        return Boolean.parseBoolean(val);
    }

    private String joinPath(String path) {
        return String.format("%s.%s", this.section.getCurrentPath(), path).replaceFirst(".", "");
    }

}
