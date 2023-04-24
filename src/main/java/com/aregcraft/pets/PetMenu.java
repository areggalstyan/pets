package com.aregcraft.pets;

import com.aregcraft.delta.api.FormattingContext;
import com.aregcraft.delta.api.json.annotation.JsonConfiguration;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@JsonConfiguration("menu")
public class PetMenu {
    private String title;
    private int size;

    public Inventory createInventory(Player owner, Pets plugin) {
        return Bukkit.createInventory(owner, size, FormattingContext.withPlugin(plugin).format(title));
    }
}
