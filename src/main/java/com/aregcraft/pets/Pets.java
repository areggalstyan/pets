package com.aregcraft.pets;

import com.aregcraft.delta.api.DeltaPlugin;
import com.aregcraft.delta.api.Language;
import com.aregcraft.delta.api.json.JsonConfigurationLoader;
import com.aregcraft.delta.api.registry.RegistrableRegistry;
import com.aregcraft.delta.api.registry.Registry;
import com.aregcraft.delta.api.update.UpdateChecker;
import com.aregcraft.delta.api.update.Updater;
import com.aregcraft.pets.perk.Perk;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Pets extends DeltaPlugin {
    private final JsonConfigurationLoader configurationLoader = new JsonConfigurationLoader(this);
    private final Registry<String, Language> languages =
            new Registry<>("languages", Language.class, configurationLoader, this::initializeLanguage);
    private final Registry<String, Perk> perks = new Registry<>("perks", Perk.class, configurationLoader);
    private final Registry<String, Rarity> rarities =
            new Registry<>("rarities", Rarity.class, configurationLoader);
    private final Registry<String, PetType> pets =
            new RegistrableRegistry<>("pets", PetType.class, configurationLoader);
    private final Registry<String, ExperienceBooster> experienceBoosters =
            new RegistrableRegistry<>("experience_boosters", ExperienceBooster.class, configurationLoader);
    private final Registry<String, Candy> candies =
            new RegistrableRegistry<>("candies", Candy.class, configurationLoader);
    private final Registry<String, Upgrade> upgrades =
            new RegistrableRegistry<>("upgrades", Upgrade.class, configurationLoader);
    private final Map<UUID, PetOwner> owners = new HashMap<>();
    private final Updater updater = new Updater(this);

    @Override
    public void onEnable() {
        super.onEnable();
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

    public Vector getPetPosition() {
        return configurationLoader.get("position", Vector.class);
    }

    public String[] getPetsInfoUsage() {
        return configurationLoader.get("petsinfo_usage", String[].class);
    }

    public Registry<String, Perk> getPerks() {
        return perks;
    }

    public Registry<String, Rarity> getRarities() {
        return rarities;
    }

    public Rarity getDefaultRarity() {
        return rarities.getValues().stream().sorted().findFirst().orElseThrow();
    }

    public Registry<String, PetType> getPets() {
        return pets;
    }

    public Registry<String, ExperienceBooster> getExperienceBoosters() {
        return experienceBoosters;
    }

    public Registry<String, Candy> getCandies() {
        return candies;
    }

    public Registry<String, Upgrade> getUpgrades() {
        return upgrades;
    }

    public Registry<String, Language> getLanguages() {
        return languages;
    }

    @Override
    public void setLanguage(Language language) {
        super.setLanguage(language);
        configurationLoader.set("selected_language", language.getId());
        reload();
    }

    private void initializeLanguage() {
        super.setLanguage(languages.findAny(configurationLoader.get("selected_language", String.class)));
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
        perks.invalidateAll();
        rarities.invalidateAll();
        pets.invalidateAll();
        experienceBoosters.invalidateAll();
        candies.invalidateAll();
        upgrades.invalidateAll();
        configurationLoader.invalidateAll();
    }

    public void giveSelectFeedback(Player player, Pet pet) {
        configurationLoader.get(SelectDeselect.class).getSelect()
                .give(player, pet.getName(player, this), this);
    }

    public void giveDeselectFeedback(Player player, Pet pet) {
        if (pet == null) {
            return;
        }
        configurationLoader.get(SelectDeselect.class).getDeselect()
                .give(player, pet.getName(player, this), this);
    }
}
