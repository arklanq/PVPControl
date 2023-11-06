package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.utils.StringVariable;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import java.util.*;

@Singleton
public class Localization {
    private final TranslationsSuppliersExecutive translationsSuppliersExecutive;

    @Inject
    Localization(TranslationsSuppliersExecutive translationsSuppliersExecutive) {
        this.translationsSuppliersExecutive = translationsSuppliersExecutive;
    }

    public String t(String key) {
        return this.getString(key).replaceAll("&", "§");
    }

    public String t(String key, HashMap<String, String> variables) {
        String translated = this.t(key);

        for(String variablePattern : variables.keySet()) {
            translated = translated.replaceAll(variablePattern, variables.get(variablePattern));
        }

        return translated;
    }

    public String t(String key, StringVariable ...variables) {
        HashMap<String, String> variablesHashMap = Arrays.stream(variables).reduce(
            new HashMap<>(),
            (HashMap<String, String> hashMap, StringVariable variable) -> {
                hashMap.putAll(variable.toHashMap());
                return hashMap;
            },
            (HashMap<String, String> hashMapA, HashMap<String, String> hashMapB) -> {
                hashMapA.putAll(hashMapB);
                return hashMapA;
            }
        );

        return this.t(key, variablesHashMap);
    }

    public LocalizedMessageBuilder builder(String pattern) {
        return new LocalizedMessageBuilder(this, pattern);
    }

    public String getString(String key) {
        return this.translationsSuppliersExecutive.getString(key);
    }

}
