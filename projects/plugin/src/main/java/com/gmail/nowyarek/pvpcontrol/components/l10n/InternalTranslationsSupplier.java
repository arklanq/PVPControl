package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.resources.ResourceBundles;

import com.google.common.base.MoreObjects;
import jakarta.inject.Inject;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class InternalTranslationsSupplier extends TranslationsSupplier {
    private final Locale locale;
    private final String languageCode;

    @Inject
    public InternalTranslationsSupplier(Locale locale, @LanguageCode String languageCode) {
        super(languageCode);
        this.locale = locale;
        this.languageCode = languageCode;
    }

    @Override
    public CompletableFuture<Optional<ResourceBundle>> provideResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            String bundleName = String.format("lang.%s", this.languageCode);

            return ResourceBundles.fromJar(bundleName, this.locale).join();
        });
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("languageCode", this.languageCode)
            .toString();
    }
}
