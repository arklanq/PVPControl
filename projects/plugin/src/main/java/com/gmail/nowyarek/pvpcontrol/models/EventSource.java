package com.gmail.nowyarek.pvpcontrol.models;

import com.google.common.eventbus.EventBus;

public interface EventSource {

    EventBus getEventBus();

}
