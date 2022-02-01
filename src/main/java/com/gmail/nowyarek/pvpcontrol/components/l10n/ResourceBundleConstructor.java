package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDataFolder;

import javax.inject.Inject;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class ResourceBundleConstructor implements EventListener {
    private final Locale locale;
    private final String languageCode;
    private final File dataFolder;

    @Inject
    public ResourceBundleConstructor(
        Locale locale,
        @LanguageCode String languageCode,
        @PluginDataFolder File dataFolder
    ) {
        this.locale = locale;
        this.languageCode = languageCode;
        this.dataFolder = dataFolder;
    }

    public CompletableFuture<Optional<ResourceBundle>> constructExternalResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String bundleName = String.format("%s.properties", this.languageCode);
                File bundleFile = new File(new File(this.dataFolder, "lang"), bundleName);
                FileInputStream fileInputStream = new FileInputStream(bundleFile);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, StandardCharsets.UTF_8);
                ResourceBundle resourceBundle = new PropertyResourceBundle(inputStreamReader);
                return Optional.of(resourceBundle);
            } catch(FileNotFoundException e) {
                return Optional.empty();
            } catch(Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    // Try to load the built-in ResourceBundle
    // If not built-in ResourceBundle can be found then fallback to the ResourceBundle with english translations

    public CompletableFuture<Optional<ResourceBundle>> constructBuiltInResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String bundlePackage = String.format("lang.%s", this.languageCode);
                ResourceBundle resourceBundle = ResourceBundle.getBundle(bundlePackage, this.locale, new ResourceBundleUTF8Control());
                return Optional.of(resourceBundle);
            } catch(MissingResourceException e) {
                return Optional.empty();
            }
        });
    }

    public CompletableFuture<Optional<ResourceBundle>> constructDefaultResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            String bundlePackage = "lang.en";
            ResourceBundle resourceBundle = ResourceBundle.getBundle(bundlePackage, this.locale, new ResourceBundleUTF8Control());
            return Optional.of(resourceBundle);
        });
    }

}
