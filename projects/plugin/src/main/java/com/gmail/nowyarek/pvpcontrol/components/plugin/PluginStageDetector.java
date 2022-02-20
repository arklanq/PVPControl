package com.gmail.nowyarek.pvpcontrol.components.plugin;

import com.google.inject.Stage;

import javax.annotation.Nullable;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class PluginStageDetector implements Supplier<Stage> {
    private final Logger logger;

    public PluginStageDetector(Logger logger) {
        this.logger = logger;
    }

    @Override
    public Stage get() {
        @Nullable String stageStringValue = System.getenv("JAVA_ENV");

        if (stageStringValue == null)
            return Stage.PRODUCTION;

        try {
            return Stage.valueOf(stageStringValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            logger.warning(
                String.format("Unable to determine application stage based on `JAVA_ENV=%s` variable.", stageStringValue)
            );
            return Stage.DEVELOPMENT;
        }
    }

}
