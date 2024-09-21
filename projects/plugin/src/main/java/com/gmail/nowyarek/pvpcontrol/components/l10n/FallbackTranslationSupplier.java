package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import jakarta.inject.Inject;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class FallbackTranslationSupplier extends InternalTranslationSupplier {
    private final String languageCode;

    @Inject
    public FallbackTranslationSupplier(Locale locale, @DefaultLanguageCode String languageCode) {
        super(locale, languageCode);
        this.languageCode = languageCode;
    }

    @Override
    public CompletableFuture<Optional<ResourceBundle>> provideResourceBundle() {
        return super.provideResourceBundle().thenApply((Optional<ResourceBundle> resourceBundle) -> {
            Preconditions.checkState(resourceBundle.isPresent(), String.format("Fallback translations ResourceBundle (%s) must be available.", languageCode));
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
