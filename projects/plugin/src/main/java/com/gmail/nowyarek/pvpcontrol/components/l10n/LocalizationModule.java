package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.settings.Settings;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

import java.util.Locale;

public class LocalizationModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TranslationsSupplier.class).annotatedWith(Names.named("External")).to(ExternalTranslationsSupplier.class);
        bind(TranslationsSupplier.class).annotatedWith(Names.named("Internal")).to(InternalTranslationsSupplier.class);
        bind(TranslationsSupplier.class).annotatedWith(Names.named("Fallback")).to(FallbackTranslationsSupplier.class);
        bind(TranslationsSuppliersExecutive.class).in(Scopes.SINGLETON);
        bind(Localization.class).in(Scopes.SINGLETON);
        bind(LanguagesDetector.class);
        bind(TranslationsValidator.class);
        bind(TranslationsManager.class).asEagerSingleton();
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
