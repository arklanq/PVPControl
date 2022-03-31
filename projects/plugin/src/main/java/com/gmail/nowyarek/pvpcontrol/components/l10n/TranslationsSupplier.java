package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.google.common.base.Preconditions;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

abstract class TranslationsSupplier {
    @Nullable
    private volatile ResourceBundle resourceBundle;

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

    Optional<String> getString(String key) {
        Preconditions.checkNotNull(this.resourceBundle, new UnsupportedOperationException("TranslationsSupplier is not available."));
        assert this.resourceBundle != null;

        if(Objects.requireNonNull(this.resourceBundle).containsKey(key))
            return Optional.of(Objects.requireNonNull(this.resourceBundle).getString(key));
        else
            return Optional.empty();
    }

    @Nullable
    ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    @Override
    public String toString() {
        return "TranslationsSupplier{" +
            "resourceBundle=" + resourceBundle +
            '}';
    }
}
