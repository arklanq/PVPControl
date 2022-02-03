package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDataFolder;
import com.gmail.nowyarek.pvpcontrol.utils.JarResourcesUtils;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class LanguagesDetector {
    private final File dataFolder;

    @Inject
    public LanguagesDetector(@PluginDataFolder File dataFolder) {
        this.dataFolder = dataFolder;
    }

    public CompletableFuture<ImmutableList<String>> detectBuiltInLanguages() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String[] filesListing = JarResourcesUtils.getJarResourcesListing("/lang", false);
                String[] langCodes = this.convertFileNamesToLanguagesCodes(filesListing);
                return ImmutableList.copyOf(langCodes);
            } catch(Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    public CompletableFuture<ImmutableList<String>> detectExternalLanguages() {
        return CompletableFuture.supplyAsync(() -> {
            File langDir = new File(this.dataFolder, "lang");

            // Safely return empty list if there isn't a valid `lang` directory
            if (!langDir.exists() || !langDir.isDirectory()) return ImmutableList.of();

            String[] filesListing = this.convertFilesToFileNames(Objects.requireNonNull(langDir.listFiles()));
            //TODO: Filter out files based on regexp pattern and warn user about files found that do not conform to this regexp
            String[] langCodes = this.convertFileNamesToLanguagesCodes(filesListing);
            return ImmutableList.copyOf(langCodes);
        });
    }

    private String[] convertFilesToFileNames(File[] files) {
        return Arrays.stream(files).map(File::getName).toArray(String[]::new);
    }

    private String[] convertFileNamesToLanguagesCodes(String[] fileNames) {
        return Arrays.stream(fileNames).map((String fileName) -> fileName.endsWith(".properties") ? fileName.substring(0, fileName.indexOf(".properties")) : fileName).toArray(String[]::new);
    }

}
