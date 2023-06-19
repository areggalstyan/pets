package com.aregcraft.pets.perk;

import net.objecthunter.exp4j.Expression;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.Map;

/**
 * Increase the amount of damage dealt to certain entities
 */
public class DamagePerk extends Perk implements Listener {
    /**
     * The damage bonuses based on the dealt damage (x) with entities
     */
    private Map<EntityType, Expression> bonuses;

    @Override
    public void apply(Player player) {
        setPlayerApplied(player);
    }

    @Override
    public void unapply(Player player) {
        unsetPlayerApplied(player);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        var player = event.getDamager();
        if (!(event.getEntity() instanceof LivingEntity entity) || !isPlayerApplied(player)) {
            return;
        }
        var damage = event.getDamage();
        var bonus = bonuses.get(entity.getType());
        if (bonus == null) {
            return;
        }
        bonus.setVariable("x", damage);
        event.setDamage(damage + bonus.evaluate());
    }
}
