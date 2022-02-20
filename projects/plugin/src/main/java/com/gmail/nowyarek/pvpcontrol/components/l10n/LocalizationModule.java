package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.settings.Settings;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Provides;
import com.google.inject.multibindings.OptionalBinder;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(LangResourceBundlesManager.class).asEagerSingleton();

        bind(ExternalResourceBundleProvider.class);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ResourceBundle.class, ExternalLangResourceBundle.class)).setBinding().toProvider(ExternalResourceBundleProvider.class);
        bind(InternalResourceBundleProvider.class);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ResourceBundle.class, InternalLangResourceBundle.class)).setBinding().toProvider(InternalResourceBundleProvider.class);
        bind(DefaultResourceBundleProvider.class);
        OptionalBinder.newOptionalBinder(binder(), Key.get(ResourceBundle.class, DefaultLangResourceBundle.class)).setBinding().toProvider(DefaultResourceBundleProvider.class);

        bind(Localization.class);
        bind(LanguagesDetector.class);
        bind(LanguagesManager.class).asEagerSingleton();
    }

    /**
     * @return ISO 639 alpha-2 language code
     */
    @Provides
    @LanguageCode
    public String provideLanguageCode(Settings settings) {
        return settings.General().getLanguage();
    }

    /**
     * @return ISO 639 alpha-2 language code
     */
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
