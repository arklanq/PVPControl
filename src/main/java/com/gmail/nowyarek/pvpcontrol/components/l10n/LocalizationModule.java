package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.settings.Settings;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.multibindings.OptionalBinder;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class LocalizationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ResourceBundleConstructor.class);
        bind(ExternalResourceBundleProvider.class).asEagerSingleton();
        bind(BuiltInResourceBundleProvider.class).asEagerSingleton();
        bind(DefaultResourceBundleProvider.class).asEagerSingleton();
        bind(LanguagesDetector.class);

        OptionalBinder.newOptionalBinder(binder(), Key.get(ResourceBundle.class, ExternalLangResourceBundle.class)).setBinding().toProvider(ExternalResourceBundleProvider.class);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ResourceBundle.class, BuiltInLangResourceBundle.class)).setBinding().toProvider(BuiltInResourceBundleProvider.class);
        bind(Key.get(ResourceBundle.class, DefaultLangResourceBundle.class)).toProvider(DefaultResourceBundleProvider.class);

        bind(Localization.class);
    }

    /**
     * @return ISO 639 alpha-2 or alpha-3 language code
     */
    @Provides
    @LanguageCode
    public String provideLanguageCode(Optional<Settings> settings) {
        if(!settings.isPresent()) throw new IllegalStateException("Settings object is not constructed yet.");
        return settings.get().General().getLanguage().toLowerCase();
    }

    @Provides
    @DefaultLanguageCode
    public String provideDefaultLanguageCode() {
        return "en";
    }

    @Provides
    public Locale provideLocale(@LanguageCode String langCode) {
        return new Locale(langCode);
    }


}
