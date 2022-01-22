package com.gmail.nowyarek.pvpcontrol.components.configuration;

import javax.annotation.Nullable;

public class ViolationMessageBuilder {
    private final String path;
    private @Nullable
    Class<?> expectedType;
    private @Nullable
    Object defaultValue;
    private @Nullable
    String customMessage;

    public ViolationMessageBuilder(String path) {
        this.path = path;
    }

    public static ViolationMessageBuilder forPath(String path) {
        return new ViolationMessageBuilder(path);
    }

    public ViolationMessageBuilder expectedType(@Nullable Class<?> expectedType) {
        this.expectedType = expectedType;
        return this;
    }

    public ViolationMessageBuilder defaultValue(@Nullable Object defaultValue) {
        this.defaultValue = defaultValue;
        return this;
    }

    public ViolationMessageBuilder message(@Nullable String customMessage) {
        this.customMessage = customMessage;
        return this;
    }

    @Override
    public String toString() {
        String violationMessage;

        if (this.customMessage != null) {
            violationMessage = this.customMessage.replaceAll("\\{path}", path);

            if (defaultValue != null)
                violationMessage = violationMessage.replaceAll("\\{defaultValue}", defaultValue.toString());

        } else if (this.expectedType != null) {
            violationMessage = String.format("`%s` must be of type `%s`.", path, this.formatExpectedTypeToBeHumanReadable());

        } else {
            violationMessage = String.format("Invalid value provided at %s.", path);
        }

        if (defaultValue != null)
            violationMessage = violationMessage.concat(String.format(" Fallback to default value: %s.", defaultValue));

        return violationMessage;
    }

    @Nullable
    private String formatExpectedTypeToBeHumanReadable() {
        if (this.expectedType == null) return null;
        String expectedTypeName = this.expectedType.getName();

        if (expectedTypeName.equals(String.class.getName())) return "text";
        else if (expectedTypeName.equals(Integer.class.getName())) return "whole number";
        else if (expectedTypeName.equals(Double.class.getName())) return "whole or decimal number";
        else if (expectedTypeName.equals(Boolean.class.getName())) return "true or false";
        else return expectedType.getSimpleName();
    }
}
