package com.aregcraft.pets;

import com.aregcraft.delta.api.DeltaPlugin;
import com.aregcraft.delta.api.json.JsonConfigurationLoader;
import com.google.gson.reflect.TypeToken;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Pets extends DeltaPlugin {
    private static final TypeToken<List<PetType>> PET_TYPE_TYPE = new TypeToken<>() {};

    private final JsonConfigurationLoader configurationLoader = JsonConfigurationLoader.builder()
            .name(PET_TYPE_TYPE, "pets")
            .plugin(this)
            .build();
    private final Map<UUID, PetOwner> owners = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        getPetTypes().forEach(it -> it.register(this));
        Bukkit.getOnlinePlayers().forEach(this::addPetOwner);
        new Metrics(this, 17178);
    }

    public PetMenu getPetMenu() {
        return configurationLoader.get(PetMenu.class);
    }

    public PetType getPetType(String id) {
        return getPetTypes().stream().filter(it -> it.id().equals(id)).findAny().orElseThrow();
    }

    private List<PetType> getPetTypes() {
        return configurationLoader.get(PET_TYPE_TYPE);
    }

    public Vector getPetPosition() {
        return configurationLoader.get("position", Vector.class);
    }

    public PetOwner getPetOwner(Player player) {
        return owners.get(player.getUniqueId());
    }

    public void addPetOwner(Player player) {
        owners.put(player.getUniqueId(), new PetOwner(player, this));
    }

    public void removePetOwner(Player player) {
        owners.remove(player.getUniqueId());
    }

    public void reload() {
        configurationLoader.invalidateAll();
        getPetTypes().forEach(it -> it.register(this));
    }
}
