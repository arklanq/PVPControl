package com.gmail.nowyarek.pvpcontrol.components.logging;

import com.google.inject.Stage;

import jakarta.inject.Inject;
import java.util.function.Supplier;
import java.util.logging.Level;

public class DefaultLogLevelDetector implements Supplier<Level> {
    private final Stage stage;

    @Inject
    public DefaultLogLevelDetector(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Level get() {
        //TODO: Checkout config first & take precedence if defined
        return stage == Stage.PRODUCTION ? Level.INFO : Level.ALL;
    }

}
