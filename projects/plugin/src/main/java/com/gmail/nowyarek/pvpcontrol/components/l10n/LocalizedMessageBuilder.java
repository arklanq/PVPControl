package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.utils.StringVariable;

import java.util.ArrayList;

public class LocalizedMessageBuilder {
    private final Localization localization;
    private String key;
    private ArrayList<StringVariable> variables = new ArrayList<>();

    public LocalizedMessageBuilder(Localization localization, String key) {
        this.localization = localization;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public LocalizedMessageBuilder setKey(String key) {
        this.key = key;
        return this;
    }

    public ArrayList<StringVariable> getVariables() {
        return variables;
    }

    public LocalizedMessageBuilder setVariables(ArrayList<StringVariable> variables) {
        this.variables = variables;
        return this;
    }

    public LocalizedMessageBuilder addVariable(StringVariable variable) {
        this.variables.add(variable);
        return this;
    }

    public LocalizedMessageBuilder addVariable(String pattern, String value) {
        this.variables.add(new StringVariable(pattern, value));
        return this;
    }

    public String toString() {
        return this.localization.t(this.key, this.variables.toArray(new StringVariable[0]));
    }

}
