package com.gmail.nowyarek.pvpcontrol.models;

import com.google.common.eventbus.EventBus;

public interface EventsSource {

    EventBus getEventBus();

}
