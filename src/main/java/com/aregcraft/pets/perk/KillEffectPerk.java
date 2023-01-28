package com.aregcraft.pets.perk;

import com.aregcraft.delta.api.entity.Entities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Adds an effect on the owner when they kill
 */
public class KillEffectPerk extends Perk implements Listener {
    /**
     * The effect type
     */
    private PotionEffectType type;
    /**
     * The effect duration in ticks (1 second = 20 ticks)
     */
    private int duration;
    /**
     * The effect amplifier
     */
    private int amplifier;
    /**
     * Whether to hide the effect particles
     */
    private boolean hideParticles;
    /**
     * Whether to add the effect only when killing players
     */
    private boolean onlyPlayers;

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
        if (killer != null && isPlayerApplied(killer) && (!onlyPlayers || entity instanceof Player)) {
            Entities.addPotionEffect(killer, type, duration, amplifier, hideParticles);
        }
    }
}
