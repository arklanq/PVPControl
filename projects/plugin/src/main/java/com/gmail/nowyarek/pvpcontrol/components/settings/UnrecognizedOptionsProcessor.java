package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.utils.ConfigurationSectionUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class UnrecognizedOptionsProcessor {
    private final FileConfiguration config;
    private final FileConfiguration defaults;

    public UnrecognizedOptionsProcessor(FileConfiguration config, FileConfiguration defaults) {
        this.config = config;
        this.defaults = defaults;
    }

    public List<String> process() {
        return this.checkAllSubSections(config, defaults, new ArrayList<>());
    }

    private List<String> checkAllSubSections(ConfigurationSection section, @Nullable ConfigurationSection defaultsSection, List<String> violations) {
        for (String key : section.getKeys(false)) {
            if(section.isConfigurationSection(key)) {
                ConfigurationSection nestedSection = section.getConfigurationSection(key);

                if(nestedSection != null) {
                    if (defaultsSection != null && defaultsSection.isConfigurationSection(key)) {
                        ConfigurationSection nestedDefaultsSection = defaultsSection.getConfigurationSection(key);
                        violations = this.checkAllSubSections(nestedSection, nestedDefaultsSection, violations);
                    } else {
                        violations.add(String.format("Unrecognized option: %s.", ConfigurationSectionUtils.joinPath(section.getCurrentPath(), key)));
                    }
                }
            } else if (section.isSet(key) && (defaultsSection == null || !defaultsSection.isSet(key))) {
                violations.add(String.format("Unrecognized option: %s.", ConfigurationSectionUtils.joinPath(section.getCurrentPath(), key)));
            }
        }

        return violations;
    }

    /*
    // Alternative implementation
    private List<String> checkAllSubSections(ConfigurationSection section, ConfigurationSection defaultsSection, List<String> violations) {
        for (String key : section.getKeys(true)) {
            if(!defaultsSection.isSet(key)*//* && !defaultsSection.isConfigurationSection(key)*//*)
                violations.add(String.format("Unrecognized option: %s.", ConfigurationSectionUtils.joinPath(section.getCurrentPath(), key)));
        }

        return violations;
    }
    */

}
