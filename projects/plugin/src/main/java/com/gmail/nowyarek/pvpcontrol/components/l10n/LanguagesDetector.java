package com.gmail.nowyarek.pvpcontrol.components.l10n;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;
import com.gmail.nowyarek.pvpcontrol.components.plugin.PluginDataFolder;
import com.gmail.nowyarek.pvpcontrol.utils.JarResourcesUtils;
import com.google.common.collect.ImmutableList;

import javax.inject.Inject;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class LanguagesDetector {
    private final File dataFolder;
    private final PluginLogger logger;
    private final Localization localization;
    private final Pattern validFileNamePattern = Pattern.compile("^\\w{2}.properties$");

    @Inject
    public LanguagesDetector(@PluginDataFolder File dataFolder, PluginLogger logger, Localization localization) {
        this.dataFolder = dataFolder;
        this.logger = logger;
        this.localization = localization;
    }

    public CompletableFuture<ImmutableList<String>> detectExternalLanguages() {
        return CompletableFuture.supplyAsync(() -> {
            File langDir = new File(this.dataFolder, "lang");

            // Safely return empty list if there isn't a valid `lang` directory
            if (!langDir.exists() || !langDir.isDirectory()) return ImmutableList.of();

            String[] filesListing = this.convertFilesToFileNames(Objects.requireNonNull(langDir.listFiles()));
            List<String> validFilesNames = new ArrayList<>(), invalidFilesNames = new ArrayList<>();
            for (String fileName : filesListing) {
                if (validFileNamePattern.matcher(fileName).matches()) validFilesNames.add(fileName);
                else if (fileName.startsWith(".")) //noinspection UnnecessaryContinue
                    continue; // Filter out hidden files like .DS_Store etc.
                else invalidFilesNames.add(fileName);
            }

            if (invalidFilesNames.size() > 0) {
                this.logger.warn(
                    this.localization.builder("localization.detected_invalid_custom_translations")
                        .addVariable("%directory%", "PVPControl/lang")
                        .addVariable("%invalid_translations_files%", String.join(", ", invalidFilesNames))
                        .addVariable("%link%", "https://blabla")
                        .toString()
                );
            }

            List<String> langCodes = this.convertFileNamesToLanguagesCodes(validFilesNames);
            return ImmutableList.copyOf(langCodes);
        });
    }

    public CompletableFuture<ImmutableList<String>> detectInternalLanguages() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String[] filesListing = JarResourcesUtils.getJarResourcesListing("/lang", false);
                List<String> langCodes = this.convertFileNamesToLanguagesCodes(Arrays.asList(filesListing));
                return ImmutableList.copyOf(langCodes);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        });
    }

    private String[] convertFilesToFileNames(File[] files) {
        return Arrays.stream(files).map(File::getName).toArray(String[]::new);
    }

    private List<String> convertFileNamesToLanguagesCodes(Iterable<String> fileNames) {
        return StreamSupport.stream(fileNames.spliterator(), false)
            .map((String fileName) -> fileName.substring(0, fileName.indexOf(".properties")))
            .collect(Collectors.toList());
    }

}
