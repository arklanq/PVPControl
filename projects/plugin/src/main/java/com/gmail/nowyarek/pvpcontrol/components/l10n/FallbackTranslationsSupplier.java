package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.resources.ResourceBundles;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

import static com.google.common.base.Preconditions.checkState;

public class FallbackTranslationsSupplier extends TranslationsSupplier {
    private final String defaultLanguageCode;

    @Inject
    public FallbackTranslationsSupplier(@DefaultLanguageCode String defaultLanguageCode) {
        this.defaultLanguageCode = defaultLanguageCode;
    }

    @Override
    public CompletableFuture<Optional<ResourceBundle>> provideResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            String bundleName = "lang.en";
            Optional<ResourceBundle> resourceBundle = ResourceBundles.fromJar(bundleName, Locale.ENGLISH).join();

            checkState(resourceBundle.isPresent(), String.format("Fallback translations ResourceBundle (%s) must be available.", defaultLanguageCode));

            return resourceBundle;
        });
    }
}
