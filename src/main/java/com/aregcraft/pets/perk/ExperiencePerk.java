package com.aregcraft.pets.perk;

import net.objecthunter.exp4j.Expression;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

/**
 * Boosts the amount of experience that the owner receives
 */
public class ExperiencePerk extends Perk implements Listener {
    /**
     * The amount of experience to add to the earned ones (x)
     */
    private Expression bonus;

    @Override
    public void apply(Player player) {
        setPlayerApplied(player);
    }

    @Override
    public void unapply(Player player) {
        unsetPlayerApplied(player);
    }

    @EventHandler
    public void onPlayerExpChange(PlayerExpChangeEvent event) {
        if (!isPlayerApplied(event.getPlayer())) {
            return;
        }
        var amount = event.getAmount();
        bonus.setVariable("x",  amount);
        event.setAmount(amount + (int) bonus.evaluate());
    }
}
