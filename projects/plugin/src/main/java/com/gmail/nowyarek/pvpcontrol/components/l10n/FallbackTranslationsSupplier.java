package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.resources.ResourceBundles;

import com.google.common.base.MoreObjects;
import jakarta.inject.Inject;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkState;

public class FallbackTranslationsSupplier extends TranslationsSupplier {
    private final String languageCode;

    @Inject
    public FallbackTranslationsSupplier(@DefaultLanguageCode String languageCode) {
        super(languageCode);
        this.languageCode = languageCode;
    }

    @Override
    public CompletableFuture<Optional<ResourceBundle>> provideResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            String bundleName = "lang.en";
            Optional<ResourceBundle> resourceBundle = ResourceBundles.fromJar(bundleName, Locale.ENGLISH).join();

            checkState(resourceBundle.isPresent(), String.format("Fallback translations ResourceBundle (%s) must be available.", languageCode));

            return resourceBundle;
        });
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("languageCode", this.languageCode)
            .toString();
    }
}
