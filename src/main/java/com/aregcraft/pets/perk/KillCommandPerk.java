package com.aregcraft.pets.perk;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

/**
 * Runs a command when the owner kills an entity
 */
public class KillCommandPerk extends Perk implements Listener {
    /**
     * The entity type
     */
    private EntityType type;
    /**
     * The command
     */
    private String command;

    @Override
    public void apply(Player player) {
        setPlayerApplied(player);
    }

    @Override
    public void unapply(Player player) {
        unsetPlayerApplied(player);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        var entity = event.getEntity();
        var killer = entity.getKiller();
        if (killer != null && isPlayerApplied(killer) && entity.getType() == type) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
                    command.replaceAll("%player%", killer.getName()));
        }
    }
}
