package com.gmail.nowyarek.pvpcontrol.components.configuration;

import com.gmail.nowyarek.pvpcontrol.utils.ConfigurationSectionUtils;
import com.google.common.collect.ImmutableList;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nullable;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationValidation {
    private final ConfigurationSection config;
    @Nullable
    private final ConfigurationSection defaults;
    private final List<String> violations = new ArrayList<>();

    public ConfigurationValidation(ConfigurationSection config, @Nullable ConfigurationSection defaults) {
        this.config = config;
        this.defaults = defaults;
    }

    public ImmutableList<String> getViolations() {
        return ImmutableList.<String>builder().addAll(this.violations).build();
    }

    public void mergeViolations(ImmutableList<String> newViolations) {
        this.violations.addAll(newViolations);
    }

    @Nullable
    public String requireString(String path) {
        return this.requireString(path, null);
    }

    @Nullable
    public String requireString(String path, @Nullable String message) {
        String val = config.getString(path);
        ViolationMessageBuilder violationBuilder = ViolationMessageBuilder.forPath(this.joinPath(path));

        if (val == null) {
            violationBuilder.message(message);

            if (defaults != null) {
                String defaultVal = defaults.getString(path);
                violationBuilder.defaultValue(defaultVal);
                this.addViolation(violationBuilder.toString());
                return defaultVal;
            }

            this.addViolation(violationBuilder.toString());
            return null;
        }

        return val.trim();
    }

    @Nullable
    public String requireStringMatchingPattern(String path, Pattern pattern) {
        return this.requireStringMatchingPattern(path, pattern, null);
    }

    @Nullable
    public String requireStringMatchingPattern(String path, Pattern pattern, @Nullable String message) {
        String val = this.requireString(path, null);
        if (val == null) return null;

        Matcher matcher = pattern.matcher(val);
        if (!matcher.matches()) {
            ViolationMessageBuilder violationBuilder = ViolationMessageBuilder
                .forPath(this.joinPath(path))
                .actualValue(val);

            @Nullable String defaultVal = null;
            if (defaults != null) {
                defaultVal = defaults.getString(path);
                violationBuilder.defaultValue(defaultVal);
            }

            violationBuilder.message(
                message != null
                    ? message
                    : String.format(
                    "`{path}` should match the pattern: %s. Instead received: `{actualValue}`.",
                    pattern
                )
            );

            this.addViolation(violationBuilder.toString());

            return defaultVal;
        }

        return val;
    }

    @Nullable
    public List<String> requireStringList(String path) {
        return this.requireStringList(path, null);
    }

    @Nullable
    public List<String> requireStringList(String path, String message) {
        ViolationMessageBuilder violationBuilder = ViolationMessageBuilder.forPath(this.joinPath(path));

        if (!config.isList(path)) {
            violationBuilder.expectedType(List.class);

            Object val = config.get(path);
            if (val != null)
                violationBuilder.actualValue(val);

            if (defaults != null) {
                List<String> defaultVal = defaults.getStringList(path);
                violationBuilder.defaultValue(String.format("<list with %s elements>", defaultVal.size()));
            }

            this.addViolation(violationBuilder.toString());
        }

        return config.getStringList(path);
    }

    @Nullable
    public String requireStringEnum(String path, String[] enumValues) {
        return this.requireStringEnum(path, enumValues, null);
    }

    @Nullable
    public String requireStringEnum(String path, String[] enumValues, @Nullable String message) {
        String val = this.requireString(path);
        if (val == null) return null;

        List<String> enumValuesList = Arrays.asList(enumValues);
        if (!enumValuesList.contains(val)) {
            ViolationMessageBuilder violationBuilder = ViolationMessageBuilder
                .forPath(this.joinPath(path))
                .actualValue(val);

            Optional<String> concatenatedValues = enumValuesList.stream().reduce((acc, element) -> acc.concat(", " + element));
            violationBuilder.message(
                message != null
                    ? message
                    : String.format("`{path}` must be one of: %s. Instead received: `{actualValue}`.", concatenatedValues.orElse(""))
            );

            @Nullable String defaultValue = null;
            if (defaults != null) {
                defaultValue = defaults.getString(path);
                violationBuilder.defaultValue(defaultValue);
            }
            this.addViolation(violationBuilder.toString());

            return defaultValue;
        }

        return val;
    }

    public <E extends Enum<E>> E requireEnum(String path, Class<E> enumType) {
        return this.requireEnum(path, enumType, null);
    }

    public <E extends Enum<E>> E requireEnum(String path, Class<E> enumType, @Nullable String message) {
        return Optional
            .ofNullable(
                this.requireStringEnum(
                    path,
                    EnumSet.allOf(enumType).stream().map(Object::toString).toArray(String[]::new),
                    message
                )
            ).map((String value) -> Enum.valueOf(enumType, value)).orElse(null);
    }

    public int requireInt(String path) {
        return this.requireInt(path, null);
    }

    public int requireInt(String path, @Nullable String message) {
        String val = this.requireString(path);
        if (val == null) return 0;

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            ViolationMessageBuilder violationBuilder = ViolationMessageBuilder
                .forPath(this.joinPath(path))
                .expectedType(Integer.class)
                .actualValue(val)
                .message(message);

            if (defaults != null) {
                int defaultVal = defaults.getInt(path);
                violationBuilder.defaultValue(defaultVal);
                this.addViolation(violationBuilder.toString());
                return defaultVal;
            }

            this.addViolation(violationBuilder.toString());
            return 0;
        }
    }

    public double requireDouble(String path) {
        return this.requireDouble(path, null);
    }

    public double requireDouble(String path, @Nullable String message) {
        String val = this.requireString(path);
        if (val == null) return 0;

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException | NullPointerException e) {
            ViolationMessageBuilder violationBuilder = ViolationMessageBuilder
                .forPath(this.joinPath(path))
                .expectedType(Double.class)
                .actualValue(val)
                .message(message);

            if (defaults != null) {
                int defaultVal = defaults.getInt(path);
                violationBuilder.defaultValue(defaultVal);
                this.addViolation(violationBuilder.toString());
                return defaultVal;
            }

            this.addViolation(violationBuilder.toString());
            return 0;
        }
    }

    public boolean requireBoolean(String path) {
        return this.requireBoolean(path, null);
    }

    public boolean requireBoolean(String path, @Nullable String message) {
        String val = this.requireString(path);
        if (val == null) return false;
        else val = val.toLowerCase();

        List<String> allowedValues = Arrays.asList("true", "false");
        if (!allowedValues.contains(val)) {
            ViolationMessageBuilder violationBuilder = ViolationMessageBuilder
                .forPath(this.joinPath(path))
                .expectedType(Boolean.class)
                .actualValue(val)
                .message(message);

            if (defaults != null) {
                boolean defaultVal = defaults.getBoolean(path);
                violationBuilder.defaultValue(defaultVal);
                this.addViolation(violationBuilder.toString());
                return defaultVal;
            }

            this.addViolation(violationBuilder.toString());
            return false;
        }
        return Boolean.parseBoolean(val);
    }

    private String joinPath(String path) {
        return ConfigurationSectionUtils.joinPath(this.config.getCurrentPath(), path);
    }

    public void addViolation(String violationMessage) {
        this.violations.add(violationMessage);
    }

    public boolean removeViolation(String violationMessage) {
        return this.violations.remove(violationMessage);
    }

}
