package com.gmail.nowyarek.pvpcontrol.components.configuration;

import javax.annotation.Nullable;
import java.util.List;

public class ViolationMessageBuilder {
    private final String path;
    private @Nullable
    Class<?> expectedType;
    private @Nullable
    Object actualValue;
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

    public ViolationMessageBuilder actualValue(@Nullable Object actualValue) {
        this.actualValue = actualValue;
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

            if (this.expectedType != null)
                violationMessage = violationMessage.replaceAll("\\{expectedType}", this.stringifyType(this.expectedType));

            if (actualValue != null) {
                violationMessage = violationMessage
                    .replaceAll("\\{actualValue}", this.stringifyValue(actualValue))
                    .replaceAll("\\{actualType}", this.stringifyType(actualValue.getClass()));
            }

            if (defaultValue != null)
                violationMessage = violationMessage.replaceAll("\\{defaultValue}", defaultValue.toString());

        } else if (this.expectedType != null) {
            violationMessage = String.format("`%s` must be of type `%s`.", path, this.stringifyType(this.expectedType));

            if (this.actualValue != null)
                violationMessage = violationMessage.concat(String.format(" Instead received: `%s`.", this.stringifyValue(actualValue)));
        } else {
            violationMessage = String.format("Missing value: %s.", path);
        }

        if (defaultValue != null)
            violationMessage = violationMessage.concat(String.format(" Fallback to default value: %s.", defaultValue));

        return violationMessage;
    }

    private String stringifyType(Class<?> type) {
        String typeName = type.getName();

        if (typeName.equals(String.class.getName())) return "text";
        else if (typeName.equals(Integer.class.getName())) return "whole number";
        else if (typeName.equals(Double.class.getName())) return "whole or decimal number";
        else if (typeName.equals(Boolean.class.getName())) return "true or false";
        else if (typeName.equals(List.class.getName())) return "list of elements";
        else return type.getSimpleName();
    }

    private String stringifyValue(Object value) {
        return value.toString();
    }
}
