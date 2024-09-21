package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.annotations.Blocking;
import org.bukkit.plugin.java.JavaPlugin;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.concurrent.CompletableFuture;

@Singleton
public class TranslationsManager {
    private final TranslationSuppliersExecutive suppliersExecutive;
    private final TranslationsValidator translationsValidator;

    @Inject
    @Blocking
    TranslationsManager(
        TranslationSuppliersExecutive suppliersExecutive,
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
            //TODO: THIS DOESN'T WORK, see scratch notes
            System.out.println("Disabling in progress...");
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
