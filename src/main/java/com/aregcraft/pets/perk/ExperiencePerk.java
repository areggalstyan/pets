package com.aregcraft.pets.perk;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.mariuszgromada.math.mxparser.Expression;

public class ExperiencePerk extends Perk implements Listener {
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
        bonus.setArgumentValue("x",  amount);
        event.setAmount(amount + (int) bonus.calculate());
    }
}
