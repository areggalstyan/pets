package com.aregcraft.pets.perk;

import net.objecthunter.exp4j.Expression;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Allows the owner to reflect a portion of the received damage
 */
public class ThornsPerk extends Perk implements Listener {
    /**
     * How much damage to reflect on the attacker based on the received damage (x)
     */
    private Expression reflect;

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
        if (!(event.getDamager() instanceof LivingEntity entity) || !isPlayerApplied(event.getEntity())) {
            return;
        }
        reflect.setVariable("x", event.getDamage());
        entity.damage(reflect.evaluate());
    }
}
