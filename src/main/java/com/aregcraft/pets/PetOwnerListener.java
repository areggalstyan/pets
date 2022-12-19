package com.aregcraft.pets;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PetOwnerListener implements Listener {
    private final Pets plugin;

    public PetOwnerListener(Pets plugin) {
        this.plugin = plugin;
        Bukkit.getOnlinePlayers().forEach(it -> PetOwner.registerPlayer(it, plugin));
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        PetOwner.registerPlayer(event.getPlayer(), plugin);
    }
}
