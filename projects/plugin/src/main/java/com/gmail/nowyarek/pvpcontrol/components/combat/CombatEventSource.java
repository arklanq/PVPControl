package com.gmail.nowyarek.pvpcontrol.components.combat;

import com.gmail.nowyarek.pvpcontrol.models.EventSource;
import com.google.common.eventbus.EventBus;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CombatEventSource implements EventSource {
    private final EventBus eventBus = new EventBus();

    @Inject
    CombatEventSource() {}

    /**
     * Available events:
     * <ul>
     *     <li>{@link PlayerDamagePlayerEvent}</li>
     * </ul>
     */
    @Override
    public EventBus getEventBus() {
        return this.eventBus;
    }

}
