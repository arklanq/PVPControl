package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.utils.StringVariable;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

public class Localization {
    private final Provider<Optional<ResourceBundle>> externalRB, internalRB;
    private final Provider<ResourceBundle> defaultRB;

    @Inject
    Localization(
        @ExternalLangResourceBundle Provider<Optional<ResourceBundle>> externalRB,
        @InternalLangResourceBundle Provider<Optional<ResourceBundle>> internalRB,
        @DefaultLangResourceBundle Provider<ResourceBundle> defaultRB
    ) {
        this.externalRB = externalRB;
        this.internalRB = internalRB;
        this.defaultRB = defaultRB;
    }

    public String t(String key) {
        return this.getString(key);
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
        Optional<ResourceBundle> externalTranslations = externalRB.get(), internalTranslations = internalRB.get();
        ResourceBundle defaultTranslations = defaultRB.get();

        try {
            if(externalTranslations.isPresent() && externalTranslations.get().containsKey(key))
                return externalTranslations.get().getString(key);

            if(internalTranslations.isPresent() && internalTranslations.get().containsKey(key))
                return internalTranslations.get().getString(key);

            if(defaultTranslations.containsKey(key))
                return defaultTranslations.getString(key);

            throw new MissingResourceException(String.format("Could not find the translation for key: %s.", key), "lang.en", key);
        } catch(MissingResourceException e) {
            throw new MissingTranslationException(key);
        }
    }

}
