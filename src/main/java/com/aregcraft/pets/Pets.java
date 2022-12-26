package com.aregcraft.pets;

import com.aregcraft.delta.api.DeltaPlugin;
import com.aregcraft.delta.api.json.JsonConfigurationLoader;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Pets extends DeltaPlugin {
    private final JsonConfigurationLoader configurationLoader = new JsonConfigurationLoader(this);
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
        return List.of(configurationLoader.get("pets", PetType[].class));
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
