package com.gmail.nowyarek.pvpcontrol.utils;

import java.util.HashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class StringVariable {
    private String pattern;
    private String value;

    public StringVariable(String pattern, String value) {
        this.pattern = checkNotNull(pattern, "Argument `pattern` cannot be null.");
        this.value = checkNotNull(value, "Argument `value` cannot be null.");
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public HashMap<String, String> toHashMap() {
        return new HashMap<String, String>() {{ put(pattern, value); }};
    }
}
