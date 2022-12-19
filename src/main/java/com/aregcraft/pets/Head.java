package com.aregcraft.pets;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.UUID;

public class Head {
    private String url;
    private transient ItemStack cache;

    public ItemStack toItemStack() {
        if (cache != null) {
            return cache;
        }
        var item = new ItemStack(Material.PLAYER_HEAD);
        var profile = Bukkit.createPlayerProfile(UUID.randomUUID());
        try {
            profile.getTextures().setSkin(new URL("https://textures.minecraft.net/texture/" + url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        var meta = (SkullMeta) Objects.requireNonNull(item.getItemMeta());
        meta.setOwnerProfile(profile);
        item.setItemMeta(meta);
        cache = item;
        return item;
    }
}
