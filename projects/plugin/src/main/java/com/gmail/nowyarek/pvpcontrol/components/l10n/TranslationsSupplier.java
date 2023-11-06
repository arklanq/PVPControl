package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public abstract class TranslationsSupplier {
    @Nullable
    private volatile ResourceBundle resourceBundle;
    private final String languageCode;

    public TranslationsSupplier(String languageCode) {
        this.languageCode = languageCode;
    }

    CompletableFuture<Void> initializeAsync() {
        return this.provideResourceBundle().thenAcceptAsync(
            (Optional<ResourceBundle> optionalResourceBundle) -> optionalResourceBundle.ifPresent(
                (ResourceBundle resourceBundle) -> this.resourceBundle = resourceBundle
            )
        );
    }

    protected abstract CompletableFuture<Optional<ResourceBundle>> provideResourceBundle();

    boolean isAvailable() {
        return this.resourceBundle != null;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    Optional<String> getString(String key) {
        ResourceBundle bundle = Preconditions.checkNotNull(this.resourceBundle, new UnsupportedOperationException("TranslationsSupplier is not available."));

        if (bundle.containsKey(key))
            return Optional.of(bundle.getString(key));
        else
            return Optional.empty();
    }

    @Nullable
    ResourceBundle getResourceBundle() {
        return this.resourceBundle;
    }
}
