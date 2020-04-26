package com.gmail.nowyarek.pvpcontrol.listeners.dmghandler;

import com.gmail.nowyarek.pvpcontrol.PVPControl;
import com.gmail.nowyarek.pvpcontrol.configuration.MainConfig;
import com.gmail.nowyarek.pvpcontrol.core.PvpPlayer;
import com.gmail.nowyarek.pvpcontrol.listeners.PvpListener;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.projectiles.ProjectileSource;
import org.jetbrains.annotations.Nullable;

public class EntityDamageEntityListener implements PvpListener {
    private final PVPControl plugin;
    private final boolean essentialsHooked = false;
    @SuppressWarnings("FieldMayBeFinal")
    private boolean disableEssentialsGodModeOnHit;

    public EntityDamageEntityListener(PVPControl plugin) {
        this.plugin = plugin;
        MainConfig.Content content = plugin.getConfigurationManager().mainConfig.content;
        disableEssentialsGodModeOnHit = content.Hooks.DisableEssentialsGodModeOnHit;
    }

    @Override
    public void register() {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void unregister() {
        HandlerList.unregisterAll(this);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntityDamageEntity(EntityDamageByEntityEvent e) {
        if(e.isCancelled() && (!essentialsHooked || !disableEssentialsGodModeOnHit))
            return;

        Player victim, damager;
        if(!(e.getEntity() instanceof Player))
            return; // If victim is not of type `Player`
        else
            victim = (Player) e.getEntity();

        if(!(e.getDamager() instanceof Player)) { // If damager is not of type `Player`
            if(!(e.getDamager() instanceof Projectile))
                return; // if damager is not of type `Projectile`
            else {
                @Nullable ProjectileSource shooter = ((Projectile) e.getDamager()).getShooter();
                if(shooter == null)
                    return; // For some reason there can be no source of the projectile
                if(!(shooter instanceof Player))
                    return; // Shooter can even be a mob
                damager = (Player) shooter;
            }
        } else {
            damager = (Player) e.getDamager();
        }
        // Ok, for now we have victim and damager.

        // There can be situation where player is hitted by himself by using ender-pearl
        if(victim.getUniqueId().compareTo(damager.getUniqueId())==0)
            return;

        if(victim.hasMetadata("npc") || damager.hasMetadata("npc"))
            return; // If victim or damager is a Citizens NPC


        PvpPlayer pvpVictim = PvpPlayer.getFromMeta(victim);
        PvpPlayer pvpDamager = PvpPlayer.getFromMeta(damager);

        //TODO: Essentials god mode addon
        //TODO: Fly addon
        //TODO: Invisiblity addon

        pvpVictim.putIntoFight();
        pvpDamager.putIntoFight();
    }

}
