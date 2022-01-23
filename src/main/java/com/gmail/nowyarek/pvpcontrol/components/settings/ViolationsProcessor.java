package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;

import java.util.List;

public class ViolationsProcessor {
    private final PluginLogger logger;
    private final int configVersion;
    private final int defaultConfigVersion;

    ViolationsProcessor(PluginLogger logger, int configVersion, int defaultConfigVersion) {
        this.logger = logger;
        this.configVersion = configVersion;
        this.defaultConfigVersion = defaultConfigVersion;
    }

    public void process(List<String> violations) {
        int MAX_VIOLATIONS_IN_CONSOLE = 10;

        if (this.configVersion != this.defaultConfigVersion) {
            this.logger.warn(
                String.format(
                    "You are running PVPControl with an outdated config (vesion %s). " +
                        "Please follow the guidelines presented below to adjust your config to newest version (version %s).",
                    this.configVersion,
                    this.defaultConfigVersion
                )
            );

            violations.add(0, String.format("Update `General.configVersion` to %s.", this.defaultConfigVersion));
            MAX_VIOLATIONS_IN_CONSOLE++;
        } else if (violations.size() > 0)
            this.logger.warn(
                String.format(
                    "There was %s during validation of `settings.yml` configuration file.",
                    violations.size() == 1 ? "an issue" : "several issues"
                )
            );

        for (int i = 0; i < MAX_VIOLATIONS_IN_CONSOLE && i < violations.size(); i++) {
            String violation = violations.get(i);
            this.logger.warn("- " + violation);
        }

        if (violations.size() > MAX_VIOLATIONS_IN_CONSOLE)
            this.logger.warn(String.format("... and %s more.", violations.size() - MAX_VIOLATIONS_IN_CONSOLE));
    }

}
