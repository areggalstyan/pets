package com.aregcraft.pets.perk;

import com.aregcraft.delta.api.entity.Entities;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPotionEffectEvent;
import org.bukkit.potion.PotionEffectType;

/**
 * Adds an effect on the owner
 */
public class EffectPerk extends Perk implements Listener {
    /**
     * The effect type
     */
    private PotionEffectType type;
    /**
     * The effect amplifier
     */
    private int amplifier;
    /**
     * Whether to hide the effect particles
     */
    private boolean hideParticles;

    @Override
    public void apply(Player player) {
        setPlayerApplied(player);
        Entities.addPotionEffect(player, type, Integer.MAX_VALUE, amplifier, hideParticles);
    }

    @Override
    public void unapply(Player player) {
        unsetPlayerApplied(player);
        player.removePotionEffect(type);
    }

    @EventHandler
    public void onEntityPotionEffect(EntityPotionEffectEvent event) {
        var oldEffect = event.getOldEffect();
        event.setCancelled(oldEffect != null && oldEffect.getType() == type && isPlayerApplied(event.getEntity()));
    }
}
