package com.gmail.nowyarek.pvpcontrol.components.l10n;

public class MissingTranslationException extends RuntimeException {

    public MissingTranslationException(String key) {
        super(String.format("Missing translation for key: %s.", key));
    }

}
