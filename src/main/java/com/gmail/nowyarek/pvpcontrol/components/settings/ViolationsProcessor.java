package com.gmail.nowyarek.pvpcontrol.components.settings;

import com.gmail.nowyarek.pvpcontrol.components.logging.PluginLogger;

import javax.inject.Inject;
import java.util.List;

public class ViolationsProcessor {
    private final PluginLogger logger;

    @Inject
    ViolationsProcessor(PluginLogger logger) {
        this.logger = logger;
    }

    public void process(List<String> violations) {
        int MAX_VIOLATIONS_IN_CONSOLE = 10;

         if (violations.size() > 0)
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
