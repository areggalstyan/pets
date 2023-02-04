package com.aregcraft.pets;

import com.aregcraft.delta.api.DeltaPlugin;
import com.aregcraft.delta.api.Identifiable;
import com.aregcraft.delta.api.UpdateChecker;
import com.aregcraft.delta.api.json.JsonConfigurationLoader;
import com.aregcraft.pets.perk.Perk;
import com.google.gson.reflect.TypeToken;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.*;

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
    private final Updater updater = new Updater(this);

    @Override
    public void onEnable() {
        super.onEnable();
        load();
        Bukkit.getOnlinePlayers().forEach(this::addPetOwner);
        configurationLoader.get(UpdateChecker.class).scheduleChecks(this);
        new Metrics(this, 17178);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        owners.values().forEach(PetOwner::removeArmorStand);
    }

    public Updater getUpdater() {
        return updater;
    }

    public PetMenu getPetMenu() {
        return configurationLoader.get(PetMenu.class);
    }

    public PetType getPetType(String id) {
        return Identifiable.findAny(configurationLoader.get(PET_TYPES_TYPE), id);
    }

    public List<String> getPetTypeIds() {
        return configurationLoader.get(PET_TYPES_TYPE).stream().map(Identifiable::getId).toList();
    }

    public ExperienceBooster getExperienceBooster(String id) {
        return Identifiable.findAny(configurationLoader.get(EXPERIENCE_BOOSTERS_TYPE), id);
    }

    public List<String> getExperienceBoosterIds() {
        return configurationLoader.get(EXPERIENCE_BOOSTERS_TYPE).stream().map(Identifiable::getId).toList();
    }

    public Candy getCandy(String id) {
        return Identifiable.findAny(configurationLoader.get(CANDIES_TYPE), id);
    }

    public List<String> getCandyIds() {
        return configurationLoader.get(CANDIES_TYPE).stream().map(Identifiable::getId).toList();
    }

    public Perk getPerk(String id) {
        return Identifiable.findAny(configurationLoader.get(PERKS_TYPE), id);
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
        configurationLoader.get(PET_TYPES_TYPE).forEach(it -> it.register(this));
        configurationLoader.get(EXPERIENCE_BOOSTERS_TYPE).forEach(it -> it.register(this));
        configurationLoader.get(CANDIES_TYPE).forEach(it -> it.register(this));
    }

    public String[] getPetsInfoUsage() {
        return configurationLoader.get("petsinfo_usage", String[].class);
    }
}
