package com.gmail.nowyarek.pvpcontrol.core;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.io.*;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;

public class PvpPlayer implements MessagesSender, HasMessagesBuffor {
    public static final String ADMIN_PERMISSION = "pvpcontrol.admin";
    private final PVPControl plugin;
    private final Player p;

    private final List<AbstractMap.SimpleEntry<OutputType, QueueableMessage>> messagesBuffer = new ArrayList<>();
    private boolean bufferLocked = true;

    public PvpPlayer(PVPControl plugin, Player p) {
        this.plugin = plugin;
        this.p = p;
        FixedMetadataValue meta = new FixedMetadataValue(plugin, new PvpPlayerMetadataValue(this));
        this.p.setMetadata(PvpPlayerMetadataValue.METADATA_KEY, meta);
    }

    public Player getPlayerEntity() {
        return p;
    }

    @NotNull
    public static PvpPlayer getFromMeta(Player p) {
        return getPvpMeta(p).pvpPlayer;
    }
    @NotNull
    private static PvpPlayerMetadataValue getPvpMeta(Player p) {
        @NotNull List<MetadataValue> metadataStore = p.getMetadata(PvpPlayerMetadataValue.METADATA_KEY);
        if(metadataStore.size() == 0 || metadataStore.get(0) == null) {
            throw new PvpMetadataException(p, PvpMetadataCorruptionCause.NO_ENTRY);
        }

        MetadataValue metadataEntry = metadataStore.get(0);
        Object value = metadataEntry.value();

        if(value == null)
            throw new PvpMetadataException(p, PvpMetadataCorruptionCause.NULL_VALUE);

        if(!(value instanceof PvpPlayerMetadataValue))
            throw new PvpMetadataException(p, PvpMetadataCorruptionCause.CANNOT_CAST);

        return (PvpPlayerMetadataValue) value;
    }

    // This method can also update start time.
    public void putIntoFight() {
        plugin.getPvpHandler().playersInCombat.put(p.getUniqueId(), System.currentTimeMillis());
        Console.debug(String.format("%s entering comat..", getPlayerEntity().getName()));
    }
    public boolean getOutOfTheFight() {
        Console.debug(String.format("%s exiting comat..", getPlayerEntity().getName()));
        return plugin.getPvpHandler().playersInCombat.remove(p.getUniqueId()) != null;
    }
    public boolean isFightingNow() {
        return plugin.getPvpHandler().playersInCombat.containsKey(p.getUniqueId());
    }
    @Nullable
    public Long getFightStartTime() {
        return plugin.getPvpHandler().playersInCombat.get(p.getUniqueId());
    }
    @Nullable
    public Long getFightEstimatedEndTime() {
        Long startTime = plugin.getPvpHandler().playersInCombat.get(p.getUniqueId());
        if(startTime != null) {
            return startTime + (plugin.getPvpHandler().pvpDuration * 1000);
        } else {
            return null;
        }
    }
    public boolean isPluginAdmin() {
        return p.hasPermission(ADMIN_PERMISSION);
    }

    /* HasMessagesBuffor */
    @Override
    public void lockBuffer() {
        bufferLocked = true;
    }
    @Override
    public void releaseBuffer() {
        bufferLocked = false;
        messagesBuffer.forEach((keyValuePair) -> send(keyValuePair.getKey(), keyValuePair.getValue()));
        messagesBuffer.clear();
    }

    /* MessagesSender */
    public void debug(String ...messages) {
        String prefixes = Prefixes.getForOutputType(OutputType.DEBUG);
        for(String message : messages) {
            p.sendMessage(prefixes + message);
        }
    }
    @Override
    public void error(Text text) {
        this.send(OutputType.ERROR, new QueueableMessage(text));
    }
    @Override
    public void error(Text text, Variables var) {
        this.send(OutputType.ERROR, new QueueableMessage(text, var));
    }
    @Override
    public void error(QueueableMessage message) {
        this.send(OutputType.ERROR, message);
    }
    @Override
    public void warning(Text text) {
        this.send(OutputType.WARNING, new QueueableMessage(text));
    }
    @Override
    public void warning(Text text, Variables var) {
        this.send(OutputType.WARNING, new QueueableMessage(text, var));
    }

    @Override
    public void warning(QueueableMessage message) {
        this.send(OutputType.WARNING, message);
    }
    @Override
    public void info(Text text) {
        this.send(OutputType.INFO, new QueueableMessage(text));
    }
    @Override
    public void info(Text text, Variables var) {
        this.send(OutputType.INFO, new QueueableMessage(text, var));
    }
    @Override
    public void info(QueueableMessage message) {
        this.send(OutputType.INFO, message);
    }

    @Override
    public void annoucement(Text text) {
        this.send(OutputType.ANNOUCEMENT, new QueueableMessage(text));
    }
    @Override
    public void annoucement(Text text, Variables var) {
        this.send(OutputType.ANNOUCEMENT, new QueueableMessage(text, var));
    }
    @Override
    public void annoucement(QueueableMessage message) {
        this.send(OutputType.ANNOUCEMENT, message);
    }

    private void send(OutputType outputType, QueueableMessage ...messages) {
        String prefixes = Prefixes.getForOutputType(outputType);
        for(QueueableMessage message : messages) {
            p.sendMessage(prefixes + Localization.translate(message.getText(), message.getVariables()));
        }
    }

    public void dispose() {
        p.removeMetadata(PvpPlayerMetadataValue.METADATA_KEY, plugin);
    }

}
