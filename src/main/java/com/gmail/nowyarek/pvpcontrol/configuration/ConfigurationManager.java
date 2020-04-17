package com.gmail.nowyarek.pvpcontrol.configuration;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import org.bukkit.configuration.ConfigurationSection;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;

public class ConfigurationManager {
    private final PVPControl plugin;
    public final MainConfig mainConfig;
    public TranslationsConfig translationsConfig;
    public String[] builtInTranslations;

    public ConfigurationManager(PVPControl plugin) {
        this.plugin = plugin;
        mainConfig = new MainConfig(plugin);
        mainConfig.initialize();
        determineTranslations();
    }

    public void reload() {
        mainConfig.reload();
        determineTranslations();
    }

    public MainConfig getMainConfig() {
        return this.mainConfig;
    }

    private void determineTranslations() {
        String pickedLang = this.getPickedLanguage();

        this.checkBuiltInTranslations();
        this.exportBuiltInTranslations();

        if(Arrays.asList(builtInTranslations).contains(pickedLang)) { // if picked translation is correct
            translationsConfig = new TranslationsConfig(plugin, String.format("%s.yml", pickedLang));
        } else { // else fallback to english
            translationsConfig = new TranslationsConfig(plugin, "en.yml");
        }
        translationsConfig.initialize();
    }

    private void checkBuiltInTranslations() {
        try {
            String[] langFiles = FilesUtils.getResourceListing(ConfigurationManager.class, "language/");
            builtInTranslations = new String[langFiles.length];
            for(int i=0; i<langFiles.length; i++) {
                String fileName = langFiles[i];
                builtInTranslations[i] = fileName.substring(0, fileName.length() - 4); // cut off .yml extension
            }
        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private void exportBuiltInTranslations() {
        if(builtInTranslations == null) {
            throw new NullPointerException("builtInTranslations == null");
        }
        for(String buildInTranslation : builtInTranslations) {
            String fileName = String.format("language/%s.yml", buildInTranslation);
            File configFile = new File(plugin.getDataFolder(), fileName);
            if (!configFile.exists()) {
                plugin.saveResource(fileName, false);
            }
        }
    }

    private String getPickedLanguage() {
        ConfigurationSection configGeneralSection = mainConfig.configuration.getConfigurationSection("General");
        assert configGeneralSection != null;
        String lang = configGeneralSection.getString("Language");
        assert lang != null;
        return lang.toLowerCase();
    }

}
