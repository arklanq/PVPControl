package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDataFolder;
import com.gmail.nowyarek.pvpcontrol.components.resources.ResourceBundles;

import javax.inject.Inject;
import java.io.File;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class ExternalTranslationsSupplier extends TranslationsSupplier {
    private final File dataFolder;
    private final String languageCode;

    @Inject
    public ExternalTranslationsSupplier(@PluginDataFolder File dataFolder, @LanguageCode String languageCode) {
        this.dataFolder = dataFolder;
        this.languageCode = languageCode;
    }

    @Override
    public CompletableFuture<Optional<ResourceBundle>> provideResourceBundle() {
        return CompletableFuture.supplyAsync(() -> {
            String bundleName = String.format("%s.properties", this.languageCode);
            File bundleFile = new File(new File(this.dataFolder, "lang"), bundleName);

            return ResourceBundles.fromFileSystem(bundleFile).join();
        });
    }
}
