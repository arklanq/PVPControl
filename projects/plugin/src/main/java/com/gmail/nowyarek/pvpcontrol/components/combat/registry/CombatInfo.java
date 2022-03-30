package com.gmail.nowyarek.pvpcontrol.components.combat.registry;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.entity.Entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

public class CombatInfo {
    private final long startTimestamp, endTimestamp;
    private final ImmutableMap<Entity, Long> aggressors;

    CombatInfo(long startTimestamp, long endTimestamp, Map<Entity, Long> aggressors) {
        this.startTimestamp = startTimestamp;
        this.endTimestamp = endTimestamp;
        this.aggressors = ImmutableMap.copyOf(aggressors);
    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

    public LocalDateTime getStartTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(this.startTimestamp), ZoneId.systemDefault());
    }

    public long getEndTimestamp() {
        return this.endTimestamp;
    }

    public LocalDateTime getEndTime() {
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(endTimestamp), ZoneId.systemDefault());
    }

    public ImmutableMap<Entity, Long> getAggressorsMap() {
        return this.aggressors;
    }

    public ImmutableList<Entity> getAggressorsList() {
        return this.aggressors.keySet().asList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CombatInfo that = (CombatInfo) o;
        return startTimestamp == that.startTimestamp && endTimestamp == that.endTimestamp && Objects.equal(aggressors, that.aggressors);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(startTimestamp, endTimestamp, aggressors);
    }

    @Override
    public String toString() {
        return "CombatInfo{" +
            "startTimestamp=" + startTimestamp +
            ", endTimestamp=" + endTimestamp +
            ", aggressors=" + aggressors +
            '}';
    }
}
