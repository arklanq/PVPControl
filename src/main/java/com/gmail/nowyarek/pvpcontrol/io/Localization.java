package com.gmail.nowyarek.pvpcontrol.io;

import com.gmail.nowyarek.pvpcontrol.configuration.TranslationsConfig;

public class Localization {
    private static TranslationsConfig translationsConfig;

    // Not static to hide from usage on class
    public void provideTranslations(TranslationsConfig tConfig) {
        Localization.translationsConfig = tConfig;
    }

    public static String translate(Text text) {
        return Localization.translationsConfig.getEntry(text.name());
    }

    public static String translate(Text text, Variables var) {
        String plainText = Localization.translationsConfig.getEntry(text.name());
        return Variables.formatMessage(plainText, var);
    }

}
