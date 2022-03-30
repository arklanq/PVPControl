package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.PvPControlPlugin;
import com.gmail.nowyarek.pvpcontrol.annotations.Blocking;
import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDataFolder;
import com.gmail.nowyarek.pvpcontrol.components.resources.ResourceBundleConstructor;
import com.google.inject.Provider;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.File;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Future;

import static com.google.common.base.Preconditions.checkState;

@Singleton
public class LangResourceBundlesManager {
    private final PvPControlPlugin plugin;
    private final PluginLogger logger;
    private final String defaultLanguageCode;
    private final Provider<String> languageCodeProvider;
    private final File dataFolder;
    private final Locale locale;

    public volatile Optional<ResourceBundle> externalResourceBundle = Optional.empty(), internalResourceBundle = Optional.empty();
    public volatile ResourceBundle defaultResourceBundle;

    @Inject @Blocking
    public LangResourceBundlesManager(
        PvPControlPlugin plugin,
        PluginLogger logger,
        @DefaultLanguageCode String defaultLanguageCode,
        @LanguageCode Provider<String> languageCodeProvider,
        @PluginDataFolder File dataFolder,
        Locale locale
    ) {
        this.plugin = plugin;
        this.logger = logger;
        this.defaultLanguageCode = defaultLanguageCode;
        this.languageCodeProvider = languageCodeProvider;
        this.dataFolder = dataFolder;
        this.locale = locale;

        this.initializeSync();
    }

    public CompletableFuture<Void> reinitialize() {
        return this.initializeAsync();
    }

    private CompletableFuture<Void> initializeAsync() {
        return CompletableFuture.runAsync(() -> {
            try {
                this.externalResourceBundle = this.createExternalResourceBundle().get();
                this.internalResourceBundle = this.createInternalResourceBundle().get();
                this.defaultResourceBundle = this.createDefaultResourceBundle().get();

                this.logger.debug("Language resource bundles have been constructed.");
            } catch(Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    @Blocking
    private void initializeSync() {
        try {
            this.initializeAsync().get();
        } catch (Exception e) {
            e.printStackTrace();
            this.plugin.onDisable();
        }
    }

    private Future<Optional<ResourceBundle>> createExternalResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            Optional<ResourceBundle> resourceBundle = Optional.empty();
            try {
                String bundleName = String.format("%s.properties", this.languageCodeProvider.get());
                File bundleFile = new File(new File(this.dataFolder, "lang"), bundleName);
                return resourceBundle = ResourceBundleConstructor.constructExternalResourceBundle(bundleFile).get();
            } catch (Exception e) {
                throw new CompletionException(e);
            } finally {
                this.logger.debug(String.format("External translations ResourceBundle (%s) %s.", this.languageCodeProvider.get(), resourceBundle.isPresent() ? "loaded" : "not available"));
            }
        });
    }

    private Future<Optional<ResourceBundle>> createInternalResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            Optional<ResourceBundle> resourceBundle = Optional.empty();
            try {
                String bundleName = String.format("lang.%s", this.languageCodeProvider.get());
                return resourceBundle = ResourceBundleConstructor.constructInternalResourceBundle(bundleName, this.locale).get();
            } catch (Exception e) {
                throw new CompletionException(e);
            } finally {
                this.logger.debug(String.format("Internal translations ResourceBundle (%s) %s.", this.languageCodeProvider.get(), resourceBundle.isPresent() ? "loaded" : "not available"));
            }
        });
    }

    private Future<ResourceBundle> createDefaultResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String bundleName = "lang.en";
                Optional<ResourceBundle> resourceBundle = ResourceBundleConstructor.constructInternalResourceBundle(bundleName, this.locale).get();
                checkState(resourceBundle.isPresent(), String.format("Default translations ResourceBundle (%s) must be available.", defaultLanguageCode));
                logger.debug(String.format("Default translations ResourceBundle (%s) loaded.", defaultLanguageCode));
                return resourceBundle.get();
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

}
