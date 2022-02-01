package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.utils.StringVariable;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

public class Localization {
    private final Provider<Optional<ResourceBundle>> externalRB;
    private final Provider<Optional<ResourceBundle>> builtInRB;
    private final Provider<ResourceBundle> defaultRB;

    @Inject
    Localization(
        @ExternalLangResourceBundle Provider<Optional<ResourceBundle>> externalRB,
        @BuiltInLangResourceBundle Provider<Optional<ResourceBundle>> builtInRB,
        @DefaultLangResourceBundle Provider<ResourceBundle> defaultRB
    ) {
        this.externalRB = externalRB;
        this.builtInRB = builtInRB;
        this.defaultRB = defaultRB;
    }

    public String t(String key) {
        return this.getString(key);
    }

    public String t(String key, StringVariable ...variables) {
        HashMap<String, String> variablesHashMap = Arrays.stream(variables).reduce(
            new HashMap<>(),
            (HashMap<String, String> hashMap, StringVariable variable) -> variable.toHashMap(),
            (HashMap<String, String> hashMap, HashMap<String, String> variableHashMap) -> {
                hashMap.putAll(variableHashMap);
                return hashMap;
            }
        );

        return this.t(key, variablesHashMap);
    }

    public String t(String key, HashMap<String, String> variables) {
        String translated = this.getString(key);

        for(String variablePattern : variables.keySet()) {
            translated = translated.replaceAll(variablePattern, variables.get(variablePattern));
        }

        return translated;
    }

    public LocalizedMessageBuilder builder(String pattern) {
        return new LocalizedMessageBuilder(this, pattern);
    }

    public String getString(String key) {
        Optional<ResourceBundle> externalTranslations = externalRB.get(), builtInTranslations = builtInRB.get();
        ResourceBundle defaultTranslations = defaultRB.get();

        try {
            if(externalTranslations.isPresent() && externalTranslations.get().containsKey(key))
                return externalTranslations.get().getString(key);

            if(builtInTranslations.isPresent() && builtInTranslations.get().containsKey(key))
                return builtInTranslations.get().getString(key);

            return defaultTranslations.getString(key);
        } catch(MissingResourceException e) {
            throw new MissingTranslationException(key);
        }
    }

}
