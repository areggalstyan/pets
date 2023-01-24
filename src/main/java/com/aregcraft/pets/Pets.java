package com.aregcraft.pets;

import com.aregcraft.delta.api.DeltaPlugin;
import com.aregcraft.delta.api.json.JsonConfigurationLoader;
import com.aregcraft.pets.perk.Perk;
import com.google.gson.reflect.TypeToken;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Pets extends DeltaPlugin {
    private static final TypeToken<List<PetType>> PET_TYPES_TYPE = new TypeToken<>() {};
    private static final TypeToken<List<ExperienceBooster>> EXPERIENCE_BOOSTERS_TYPE = new TypeToken<>() {};
    private static final TypeToken<List<Candy>> CANDIES_TYPE = new TypeToken<>() {};
    private static final TypeToken<List<Perk>> PERKS_TYPE = new TypeToken<>() {};

    private final JsonConfigurationLoader configurationLoader = JsonConfigurationLoader.builder()
            .name(PET_TYPES_TYPE, "pets")
            .name(EXPERIENCE_BOOSTERS_TYPE, "experience_boosters")
            .name(CANDIES_TYPE, "candies")
            .name(PERKS_TYPE, "perks")
            .plugin(this)
            .build();
    private final Map<UUID, PetOwner> owners = new HashMap<>();

    @Override
    public void onEnable() {
        super.onEnable();
        load();
        Bukkit.getOnlinePlayers().forEach(this::addPetOwner);
        new Metrics(this, 17178);
    }

    public PetMenu getPetMenu() {
        return configurationLoader.get(PetMenu.class);
    }

    public PetType getPetType(String id) {
        return Identifiable.find(getPetTypes(), id);
    }

    private List<PetType> getPetTypes() {
        return configurationLoader.get(PET_TYPES_TYPE);
    }

    public ExperienceBooster getExperienceBooster(String id) {
        return Identifiable.find(configurationLoader.get(EXPERIENCE_BOOSTERS_TYPE), id);
    }

    public Candy getCandy(String id) {
        return Identifiable.find(configurationLoader.get(CANDIES_TYPE), id);
    }

    public Perk getPerk(String id) {
        return Identifiable.find(configurationLoader.get(PERKS_TYPE), id);
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
        configurationLoader.get(PERKS_TYPE).stream()
                .filter(Listener.class::isInstance)
                .map(Listener.class::cast)
                .forEach(this::unregisterListener);
        configurationLoader.invalidateAll();
    }

    private void load() {
        getPetTypes().forEach(it -> it.register(this));
        configurationLoader.get(EXPERIENCE_BOOSTERS_TYPE).forEach(it -> it.register(this));
        configurationLoader.get(CANDIES_TYPE).forEach(it -> it.register(this));
    }
}
