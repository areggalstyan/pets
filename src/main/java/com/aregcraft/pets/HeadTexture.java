package com.aregcraft.pets;

import com.aregcraft.delta.api.item.ItemWrapper;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Base64;
import java.util.UUID;

public class HeadTexture {
    private static final String BASE64_TEMPLATE =
            "{\"textures\":{\"SKIN\":{\"url\":\"http://textures.minecraft.net/texture/%s\"}}}";

    private final String base64;

    public HeadTexture(String url) {
        base64 = Base64.getEncoder().encodeToString(BASE64_TEMPLATE.formatted(url).getBytes());
    }

    public void apply(ItemWrapper item) {
        item.<SkullMeta>editMeta(it -> {
            var profile = new GameProfile(UUID.randomUUID(), "");
            profile.getProperties().put("textures", new Property("textures", base64));
            try {
                var profileField = it.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(it, profile);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
