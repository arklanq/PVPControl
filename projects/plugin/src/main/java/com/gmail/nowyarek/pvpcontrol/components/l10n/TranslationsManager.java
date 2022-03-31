package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.annotations.Blocking;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;

@Singleton
public class TranslationsManager {
    private final TranslationsSuppliersExecutive suppliersExecutive;
    private final TranslationsValidator translationsValidator;

    @Inject @Blocking
    TranslationsManager(
        TranslationsSuppliersExecutive suppliersExecutive,
        TranslationsValidator translationsValidator,
        JavaPlugin plugin
    ) {
        this.suppliersExecutive = suppliersExecutive;
        this.translationsValidator = translationsValidator;

        try {
            this.initializeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            plugin.onDisable();
        }
    }

    private CompletableFuture<Void> initializeAsync() {
        return CompletableFuture.runAsync(() -> {
            this.suppliersExecutive.initializeAsync().join();
            this.translationsValidator.validateAsync().join();
        });
    }

    public CompletableFuture<Void> reinitialize() {
        return this.initializeAsync();
    }

}
