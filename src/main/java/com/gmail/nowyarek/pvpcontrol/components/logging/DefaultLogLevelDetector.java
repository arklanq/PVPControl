package com.gmail.nowyarek.pvpcontrol.components.logging;

import com.gmail.nowyarek.pvpcontrol.interfaces.Detector;
import com.google.inject.Stage;

import javax.inject.Inject;
import java.util.logging.Level;

public class DefaultLogLevelDetector implements Detector<Level> {
    private final Stage stage;

    @Inject
    public DefaultLogLevelDetector(Stage stage) {
        this.stage = stage;
    }

    @Override
    public Level detect() {
        //TODO: Checkout config first & take precedence if defined
        return stage == Stage.PRODUCTION ? Level.INFO : Level.ALL;
    }

}
