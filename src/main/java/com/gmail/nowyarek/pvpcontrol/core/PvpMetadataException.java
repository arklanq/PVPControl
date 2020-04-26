package com.gmail.nowyarek.pvpcontrol.core;

import org.bukkit.entity.Player;

// Not really used right now..
public class PvpMetadataException extends RuntimeException {

    public PvpMetadataException(Player player, PvpMetadataCorruptionCause cause) {
        super(
            String.format(
                "PvpPlayer (Name:%s | UUID:%s) has corrupted metadata. Cause: `%s`.",
                player.getName(),
                player.getUniqueId(),
                cause.name()
            )
        );
    }

}

enum PvpMetadataCorruptionCause {
    NO_ENTRY, NULL_VALUE, CANNOT_CAST
}
