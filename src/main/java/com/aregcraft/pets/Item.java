package com.aregcraft.pets;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Objects;

public class Item {
    private String name;
    private List<String> lore;

    public void withItemStack(ItemStack item) {
        var meta = Objects.requireNonNull(item.getItemMeta());
        meta.setDisplayName(Format.color().format(name));
        meta.setLore(lore.stream().map(Format.color()::format).toList());
        item.setItemMeta(meta);
    }
}
