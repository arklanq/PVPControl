package com.gmail.nowyarek.pvpcontrol.io;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Variables {
    public final Map<String, String> variables;

    public Variables() {
        variables = new HashMap<>();
    }

    public Variables(Map<String, String> variables) {
        this.variables = new HashMap<>(variables);
    }

    public Variables(String variable, String toReplace) {
        variables = new HashMap<>();
        variables.put(variable, toReplace);
    }

    public void addVariable(String variable, String toReplace){
        variables.put(variable, toReplace);
    }

    public void removeVariable(String variable) {
        variables.remove(variable);
    }

    public void clearVariables() {
        variables.clear();
    }

    public static String formatMessage(@NotNull String plainText, Variables var) {
        if (var != null) {
            for (String variable : var.variables.keySet()) {
                if (plainText.contains(variable)) {
                    plainText = plainText.replaceAll(variable, (var.variables.get(variable) != null) ? var.variables.get(variable) : "");
                }
            }
        }
        return plainText;
    }
}
