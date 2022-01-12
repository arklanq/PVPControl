package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.gmail.nowyarek.pvpcontrol.interfaces.Detector;
import com.google.inject.Stage;

import javax.annotation.Nullable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PluginStageDetector implements Detector<Stage> {
    private final Logger logger;

    public PluginStageDetector(Logger logger) {
        this.logger = logger;
    }

    public Stage detect() {
        @Nullable String stageStringValue = System.getenv("JAVA_ENV");

        if (stageStringValue == null)
            return Stage.PRODUCTION;

        try {
            return Stage.valueOf(stageStringValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.log(
                Level.WARNING,
                String.format("Unable to determine application stage based on `JAVA_ENV=%s` variable.", stageStringValue)
            );
            return Stage.DEVELOPMENT;
        }
    }

}
