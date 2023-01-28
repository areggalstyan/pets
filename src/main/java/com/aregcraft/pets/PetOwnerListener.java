package com.aregcraft.pets;

import com.aregcraft.delta.api.InjectPlugin;
import com.aregcraft.delta.api.RegisteredListener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@RegisteredListener
public class PetOwnerListener implements Listener {
    @InjectPlugin
    private Pets plugin;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.addPetOwner(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.removePetOwner(event.getPlayer());
    }
}
